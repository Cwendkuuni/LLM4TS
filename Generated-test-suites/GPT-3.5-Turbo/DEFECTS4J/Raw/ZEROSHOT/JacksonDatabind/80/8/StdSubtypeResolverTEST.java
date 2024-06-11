package com.fasterxml.jackson.databind.jsontype.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(Foo.class);
        NamedType type2 = new NamedType(Bar.class);

        subtypeResolver.registerSubtypes(type1, type2);

        Set<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertTrue(registeredSubtypes.contains(type1));
        assertTrue(registeredSubtypes.contains(type2));
    }

    @Test
    public void testRegisterSubtypesFromClass() {
        subtypeResolver.registerSubtypes(Foo.class, Bar.class);

        Set<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertTrue(registeredSubtypes.contains(new NamedType(Foo.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(Bar.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withRegisteredSubtypes() {
        NamedType type1 = new NamedType(Foo.class);
        NamedType type2 = new NamedType(Bar.class);
        subtypeResolver.registerSubtypes(type1, type2);

        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withAnnotation() {
        MapperConfig<?> config = null;
        AnnotatedMember property = new AnnotatedMember(null, null, null) {}; // Placeholder
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withRegisteredSubtypesAndAnnotation() {
        NamedType type1 = new NamedType(Foo.class);
        NamedType type2 = new NamedType(Bar.class);
        subtypeResolver.registerSubtypes(type1, type2);

        MapperConfig<?> config = null;
        AnnotatedMember property = new AnnotatedMember(null, null, null) {}; // Placeholder
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withRegisteredSubtypes() {
        NamedType type1 = new NamedType(Foo.class);
        NamedType type2 = new NamedType(Bar.class);
        subtypeResolver.registerSubtypes(type1, type2);

        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withAnnotation() {
        MapperConfig<?> config = null;
        AnnotatedMember property = new AnnotatedMember(null, null, null) {}; // Placeholder
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withRegisteredSubtypesAndAnnotation() {
        NamedType type1 = new NamedType(Foo.class);
        NamedType type2 = new NamedType(Bar.class);
        subtypeResolver.registerSubtypes(type1, type2);

        MapperConfig<?> config = null;
        AnnotatedMember property = new AnnotatedMember(null, null, null) {}; // Placeholder
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    private static class Foo {}
    private static class Bar {}

}