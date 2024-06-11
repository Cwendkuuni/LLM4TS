package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.BaseTest;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

/**
 * Unit tests for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest extends BaseTest {

    /**
     * Test method for {@link StdSubtypeResolver#StdSubtypeResolver()}.
     */
    @Test
    public void testConstructor() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        assertNotNull(resolver);
    }

    /**
     * Test method for {@link StdSubtypeResolver#registerSubtypes(NamedType[])}.
     */
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType[] subtypes = new NamedType[] { new NamedType(String.class), new NamedType(Integer.class) };
        resolver.registerSubtypes(subtypes);
        assertEquals(2, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(subtypes[0]));
        assertTrue(resolver._registeredSubtypes.contains(subtypes[1]));
    }

    /**
     * Test method for {@link StdSubtypeResolver#registerSubtypes(Class[])}.
     */
    @Test
    public void testRegisterSubtypesClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = new Class<?>[] { String.class, Integer.class };
        resolver.registerSubtypes(classes);
        assertEquals(2, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(String.class)));
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(Integer.class)));
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType baseType = mock(JavaType.class);

        // No registered subtypes
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
        assertTrue(result.isEmpty());

        // Register subtypes
        resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class));
        result = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));

        // Test with null base type
        result = resolver.collectAndResolveSubtypesByClass(config, member, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);

        // No registered subtypes
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
        assertTrue(result.isEmpty());

        // Register subtypes
        resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class));
        result = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType baseType = mock(JavaType.class);

        // No registered subtypes
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
        assertTrue(result.isEmpty());

        // Register subtypes
        resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class));
        result = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));
    }

    /**
     * Test method for
     * {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);

        // No registered subtypes
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
        assertTrue(result.isEmpty());

        // Register subtypes
        resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class));
        result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));
    }

    /**
     * Test method for {@link StdSubtypeResolver#_collectAndResolve(AnnotatedClass, NamedType, MapperConfig, AnnotationIntrospector, HashMap)}.
     */
    @Test
    public void testCollectAndResolve() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        NamedType namedType = mock(NamedType.class);

        // Test base type itself
        resolver._collectAndResolve(annotatedClass, namedType, config, new AnnotationIntrospector(), new HashMap<>());
        assertTrue(namedType.isBaseType());

        // Test subtypes
        when(annotatedClass.getType()).thenReturn(Object.class);
        when(annotatedClass.getName()).thenReturn("TestClass");
        when(namedType.getType()).thenReturn(String.class);
        resolver._collectAndResolve(annotatedClass, namedType, config, new AnnotationIntrospector(), new HashMap<>());
        verify(annotatedClass, times(1)).findSubtypes();
    }

    /**
     * Test method for {@link StdSubtypeResolver#_collectAndResolveByTypeId(AnnotatedClass, NamedType, MapperConfig, Set, Map)}.
     */
    @Test
    public void testCollectAndResolveByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        NamedType namedType = mock(NamedType.class);
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();

        // Test base type itself
        resolver._collectAndResolveByTypeId(annotatedClass, namedType, config, typesHandled, byName);
        assertTrue(namedType.isBaseType());
        assertFalse(typesHandled.contains(namedType.getType()));

        // Test subtypes
        when(annotatedClass.getType()).thenReturn(Object.class);
        when(annotatedClass.getName()).thenReturn("TestClass");
        when(namedType.getType()).thenReturn(String.class);
        resolver._collectAndResolveByTypeId(annotatedClass, namedType, config, typesHandled, byName);
        verify(annotatedClass, times(1)).findSubtypes();
        assertTrue(typesHandled.contains(String.class));
        assertEquals(1, byName.size());
        assertTrue(byName.containsKey(namedType.getName()));
    }

    /**
     * Test method for {@link StdSubtypeResolver#_combineNamedAndUnnamed(Class, Set, Map)}.
     */
    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> rawBase = Object.class;
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();

        // Test base type itself
        List<NamedType> result = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertTrue(result.isEmpty());

        // Test subtypes
        when(typesHandled.contains(rawBase)).thenReturn(false);
        when(byName.containsKey(rawBase.getName())).thenReturn(new NamedType(rawBase, rawBase.getName()));
        result = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isBaseType());
        verify(typesHandled, times(1)).add(rawBase);
    }
}