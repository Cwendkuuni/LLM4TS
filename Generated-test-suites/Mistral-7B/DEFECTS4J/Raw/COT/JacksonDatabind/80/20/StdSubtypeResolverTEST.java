package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.BaseMapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.util.AnnotatedClass;
import com.fasterxml.jackson.databind.util.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import com.fasterxml.jackson.databind.util.ConverterUtil;

public class StdSubtypeResolverTest {
    private AnnotationIntrospector _ai;
    private MapperConfig<?> _config;
    private StdSubtypeResolver _resolver;

    @Before
    public void setUp() {
        _ai = mock(AnnotationIntrospector.class);
        _config = new BaseMapperConfig();
        _resolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        _resolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        _resolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));
        Set<NamedType> subtypes = _resolver.getRegisteredSubtypes();
        assertNotNull(subtypes);
        List<NamedType> subtypesList = new ArrayList<>(subtypes);
        assertNotNull(subtypesList.get(0));
        assertNotNull(subtypesList.get(1));
        assertEquals(2, subtypesList.size());
    }

    @Test
    public void testRegisterSubtypesClasses() {
        _resolver.registerSubtypes(String[].class);
        _resolver.registerSubtypes(int[].class);
        Set<NamedType> subtypes = _resolver.getRegisteredSubtypes();
        assertNotNull(subtypes);
        List<NamedType> subtypesList = new ArrayList<>(subtypes);
        assertNotNull(subtypesList.get(0));
        assertNotNull(subtypesList.get(1));
        assertEquals(2, subtypesList.size());
        assertEquals(String[].class, subtypesList.get(0).getType());
        assertEquals(int[].class, subtypesList.get(1).getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        Set<NamedType> subtypes = _resolver.collectAndResolveSubtypesByClass(_config, AnnotatedMember.STUB, null);
        assertNotNull(subtypes);
        List<NamedType> subtypesList = new ArrayList<>(subtypes);
        assertNotNull(subtypesList.get(0));
        assertEquals("ArrayOfString", subtypesList.get(0).getName());
        assertEquals(1, subtypesList.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        Set<NamedType> subtypes = _resolver.collectAndResolveSubtypesByClass(_config, AnnotatedMember.STUB,
                JavaType.Construct.fromClass(String[].class));
        assertNotNull(subtypes);
        List<NamedType> subtypesList = new ArrayList<>(subtypes);
        assertNotNull(subtypesList.get(0));
        assertEquals("ArrayOfString", subtypesList.get(0).getName());
        assertEquals(1, subtypesList.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        Set<NamedType> subtypes = _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedMember.STUB, null);
        assertNotNull(subtypes);
        List<NamedType> subtypesList = new ArrayList<>(subtypes);
        assertNotNull(subtypesList.get(0));
        assertEquals("ArrayOfString", subtypesList.get(0).getName());
        assertEquals(1, subtypesList.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        Set<NamedType> subtypes = _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedMember.STUB,
                JavaType.constructFromAnnotation(Id.CLASS, String[].class));
        assertNotNull(subtypes);
        List<NamedType> subtypesList = new ArrayList<>(subtypes);
        assertNotNull(subtypesList.get(0));
        assertEquals("ArrayOfString", subtypesList.get(0).getName());
        assertEquals(1, subtypesList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseTypeAndNullProperty() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        _resolver.collectAndResolveSubtypesByTypeId(_config, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseTypeAndNullBaseTypeTypeId() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedMember.STUB, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullSubtypeTypeId() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedMember.STUB,
                JavaType.constructFromAnnotation(Id.NONE, String.class));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyTypesHandledAndByNameMap() {
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(Collections.singletonList(
                new NamedType(String[].class, "ArrayOfString")));
        _resolver.registerSubtypes(String[].class);

        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new HashMap<>();
        _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedClass.STUB,
                JavaType.constructFromAnnotation(Id.CLASS, String[].class));
        assertNotNull(typesHandled);
        assertNotNull(byName);
        assertEquals(1, typesHandled.size());
        assertEquals(1, byName.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithPopulatedTypesHandledAndByNameMap() {
        Class<?> baseType = String[].class;
        List<NamedType> subtypes = Collections.singletonList(new NamedType(Integer[].class, "IntArray"));
        when(_ai.findSubtypes(AnnotatedMember.STUB)).thenReturn(Collections.emptyList());
        when(_ai.findSubtypes(AnnotatedClass.STUB)).thenReturn(subtypes);
        _resolver.registerSubtypes(baseType);

        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new HashMap<>();
        _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedClass.STUB,
                JavaType.constructFromAnnotation(Id.CLASS, baseType));
        assertNotNull(typesHandled);
        assertNotNull(byName);
        assertEquals(1, typesHandled.size());
        assertEquals(1, byName.size());
        assertEquals(Integer[].class, typesHandled.iterator().next());
        assertEquals("IntArray", byName.get("IntArray").getName());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        Class<?> baseType = String[].class;
        List<NamedType> namedSubtypes = Collections.singletonList(new NamedType(Integer[].class, "IntArray"));
        List<Class<?>> unnamedSubtypes = Collections.singletonList(Integer[].class);

        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new HashMap<>();
        _resolver.collectAndResolveSubtypesByTypeId(_config, AnnotatedClass.STUB,
                JavaType.constructFromAnnotation(Id.CLASS, baseType));
        _resolver._combineNamedAndUnnamed(baseType, typesHandled, byName);
        assertNotNull(typesHandled);
        assertNotNull(byName);
        assertEquals(2, typesHandled.size());
        assertEquals(1, byName.size());
        assertEquals(Integer[].class, typesHandled.iterator().next());
        assertEquals("IntArray", byName.get("IntArray").getName());
    }
}