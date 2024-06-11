package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));
        resolver.registerSubtypes(baseType, subtype1);
        resolver.registerSubtypes(baseType, subtype2);

        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(4, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(subtype1)));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype2)));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype1, Id.CLASS)));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype2, Id.CLASS)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedMember property = null; // not used
        JavaType baseType = Object.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        registerSubtypesForTest(resolver);

        AnnotatedMember property = null; // not used
        JavaType baseType = Integer.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, property, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Integer.TYPE)));
        assertTrue(subtypes.contains(new NamedType(Short.TYPE, Id.CLASS)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_PropertySubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        registerSubtypesForTest(resolver);

        AnnotatedMember property = mock(AnnotatedMember.class);
        when(property.getType()).thenReturn(String.class);

        JavaType baseType = Integer.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_BaseTypeSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        registerSubtypesForTest(resolver);

        AnnotatedMember property = null; // not used
        JavaType baseType = Object.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, property, baseType);
        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Integer.TYPE)));
        assertTrue(subtypes.contains(new NamedType(String.class, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(Integer.TYPE, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(String.class, Id.CLASS)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedMember property = null; // not used
        JavaType baseType = Object.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        registerSubtypesForTest(resolver);

        AnnotatedMember property = null; // not used
        JavaType baseType = Integer.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Integer.TYPE)));
        assertTrue(subtypes.contains(new NamedType(Short.TYPE, Id.CLASS)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_PropertySubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        registerSubtypesForTest(resolver);

        AnnotatedMember property = mock(AnnotatedMember.class);
        when(property.getType()).thenReturn(String.class);

        JavaType baseType = Integer.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_BaseTypeSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        registerSubtypesForTest(resolver);

        AnnotatedMember property = null; // not used
        JavaType baseType = Object.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Integer.TYPE)));
        assertTrue(subtypes.contains(new NamedType(String.class, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(Integer.TYPE, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(String.class, Id.CLASS)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeId_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedMember property = null; // not used

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByClass_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedMember property = null; // not used

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, property, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeId_NullProperty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = Object.class;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByClass_NullBaseTypeProperty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
    }

    private void registerSubtypesForTest(StdSubtypeResolver resolver) {
        MapperConfig<?> config = new MapperConfig<>();
        resolver.registerSubtypes(new NamedType(Integer.TYPE));
        resolver.registerSubtypes(new NamedType(Short.TYPE, Id.CLASS));
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
        AnnotatedClass subtypeClass1 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Short.class);
        resolver.registerSubtypes(baseType, new NamedType(subtypeClass1));
        AnnotatedClass subtypeClass2 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        resolver.registerSubtypes(baseType, new NamedType(subtypeClass2));
    }
}