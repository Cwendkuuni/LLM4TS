package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

/**
 * Test cases for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest {

    private static final MapperConfig<?> config = new MapperConfig.Builder().build();

    /**
     * Test registration of subtypes by class.
     */
    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;

        // Register subtypes by class
        resolver.registerSubtypes(baseType, StringSubtype1.class, StringSubtype2.class);

        // Test collection and resolution of subtypes by class
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringSubtype1.class)));
        assertTrue(subtypes.contains(new NamedType(StringSubtype2.class)));

        // Test collection and resolution of subtypes by class with property
        AnnotatedMember property = new AnnotatedMember(String.class.getDeclaredMethod("toString"), null);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringSubtype1.class)));
        assertTrue(subtypes.contains(new NamedType(StringSubtype2.class)));
    }

    /**
     * Test registration of subtypes by annotations.
     */
    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;

        // Register subtypes by annotations
        resolver.registerSubtypes(String.class, StringSubtype1.class, StringSubtype2.class);

        // Test collection and resolution of subtypes by class
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringSubtype1.class)));
        assertTrue(subtypes.contains(new NamedType(StringSubtype2.class)));

        // Test collection and resolution of subtypes by class with property
        AnnotatedMember property = new AnnotatedMember(String.class.getDeclaredMethod("toString"), null);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringSubtype1.class)));
        assertTrue(subtypes.contains(new NamedType(StringSubtype2.class)));
    }

    /**
     * Test collection and resolution of subtypes with empty set.
     */
    @Test
    public void testEmptyRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test collection and resolution of subtypes by class
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, String.class);
        assertTrue(subtypes.isEmpty());

        // Test collection and resolution of subtypes by class with property
        AnnotatedMember property = new AnnotatedMember(String.class.getDeclaredMethod("toString"), null);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, property, String.class);
        assertTrue(subtypes.isEmpty());
    }

    /**
     * Test collection and resolution of subtypes by type id.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;

        // Register subtypes by class
        resolver.registerSubtypes(baseType, StringSubtype1.class, StringSubtype2.class);

        // Test collection and resolution of subtypes by type id
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringSubtype1.class)));
        assertTrue(subtypes.contains(new NamedType(StringSubtype2.class)));

        // Test collection and resolution of subtypes by type id with property
        AnnotatedMember property = new AnnotatedMember(String.class.getDeclaredMethod("toString"), null);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringSubtype1.class)));
        assertTrue(subtypes.contains(new NamedType(StringSubtype2.class)));
    }

    /**
     * Test collection and resolution of subtypes with empty set.
     */
    @Test
    public void testEmptyRegisteredSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test collection and resolution of subtypes by type id
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, String.class);
        assertTrue(subtypes.isEmpty());

        // Test collection and resolution of subtypes by type id with property
        AnnotatedMember property = new AnnotatedMember(String.class.getDeclaredMethod("toString"), null);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, String.class);
        assertTrue(subtypes.isEmpty());
    }

    static class StringSubtype1 {
        @JsonTypeInfo(use = Id.CLASS, include = As.EXTERNAL_PROPERTY, property = "subtype")
        public String toString() {
            return "StringSubtype1";
        }
    }

    static class StringSubtype2 {
        @JsonTypeInfo(use = Id.CLASS, include = As.EXTERNAL_PROPERTY, property = "subtype")
        public String toString() {
            return "StringSubtype2";
        }
    }
}