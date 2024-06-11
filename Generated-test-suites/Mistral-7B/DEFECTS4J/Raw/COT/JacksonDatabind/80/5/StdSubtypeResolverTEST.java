package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    private MapperConfig<Object> config;
    private StdSubtypeResolver resolver;

    @Before
    public void setUp() {
        config = new MapperConfig.Builder().build();
        resolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(String.class);
        NamedType type2 = new NamedType(Integer.class);
        resolver.registerSubtypes(type1, type2);
        Set<NamedType> subtypes = resolver.getRestSubtypeMap().get(null);
        assertEquals(2, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
    }

    @Test
    public void testRegisterSubtypes_Classes() {
        resolver.registerSubtypes(String[].class, Integer[].class);
        Set<NamedType> subtypes = resolver.getRestSubtypeMap().get(null);
        assertEquals(2, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        NamedType baseType = new NamedType(Object.class);
        NamedType type1 = new NamedType(String.class);
        NamedType type2 = new NamedType(Integer.class);
        NamedType type3 = new NamedType(Number.class);

        resolver.registerSubtypes(type1, type2);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());

        // Test null baseType
        SubtypeResolver nullBaseTypeResolver = new StdSubtypeResolver();
        Set<NamedType> nullBaseSubtypes = nullBaseTypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertNull(nullBaseSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedClass() {
        NamedType baseType = new NamedType(Object.class);
        NamedType type1 = new NamedType(String.class);
        NamedType type2 = new NamedType(Integer.class);
        NamedType type3 = new NamedType(Number.class);

        resolver.registerSubtypes(type1, type2);

        AnnotatedClass ac = new AnnotatedClass(String[].class);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, ac, baseType);
        assertEquals(3, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        NamedType baseType = new NamedType(Object.class);
        NamedType type1 = new NamedType(String.class, "StringType");
        NamedType type2 = new NamedType(Integer.class, "IntegerType");
        NamedType type3 = new NamedType(Number.class, "NumberType");

        resolver.registerSubtypes(type1, type2);

        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(3, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());

        // Test null baseType
        SubtypeResolver nullBaseTypeResolver = new StdSubtypeResolver();
        Set<NamedType> nullBaseSubtypes = nullBaseTypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertNull(nullBaseSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotatedClass() {
        NamedType baseType = new NamedType(Object.class);
        NamedType type1 = new NamedType(String.class, "StringType");
        NamedType type2 = new NamedType(Integer.class, "IntegerType");
        NamedType type3 = new NamedType(Number.class, "NumberType");

        resolver.registerSubtypes(type1, type2);

        AnnotatedClass ac = new AnnotatedClass(String[].class);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, ac, baseType);
        assertEquals(3, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
        assertNotNull(subtypes.iterator().next());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptyList() {
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(0, subtypes.size());
    }
}