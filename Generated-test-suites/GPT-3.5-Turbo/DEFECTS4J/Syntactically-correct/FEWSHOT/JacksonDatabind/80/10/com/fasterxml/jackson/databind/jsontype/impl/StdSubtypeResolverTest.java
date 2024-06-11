package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdSubtypeResolverTest {
    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> config;
    private AnnotationIntrospector ai;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        config = new MapperConfigStub();
        ai = new AnnotationIntrospectorStub();
    }

    @Test
    public void testRegisterSubtypes1() {
        NamedType[] types = new NamedType[] { new NamedType(Object.class), new NamedType(Number.class),
                new NamedType(Integer.class) };
        subtypeResolver.registerSubtypes(types);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null);
        assertEquals(types.length, subtypes.size());
        assertTrue(subtypes.containsAll(List.of(types)));
    }

    @Test
    public void testRegisterSubtypes2() {
        subtypeResolver.registerSubtypes(Object.class, Number.class, Integer.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.containsAll(List.of(new NamedType(Object.class), new NamedType(Number.class),
                new NamedType(Integer.class))));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        subclassResolver.registerSubtypes(Subtype1.class, Subtype2.class, Subtype3.class);

        Collection<NamedType> subtypes = subclassResolver.collectAndResolveSubtypesByClass(config, null);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.containsAll(List.of(new NamedType(Subtype1.class), new NamedType(Subtype2.class),
                new NamedType(Subtype3.class))));
    }

    private static class Subtype1 {
    }

    private static class Subtype2 {
    }

    private static class Subtype3 {
    }

    private static class MapperConfigStub implements MapperConfig<Object> {
        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return ai;
        }

        @Override
        public JavaType constructType(java.lang.reflect.Type type) {
            return TypeFactory.defaultInstance().constructType(type);
        }
    }

    private static class AnnotationIntrospectorStub implements AnnotationIntrospector {
        @Override
        public Collection<NamedType> findSubtypes(AnnotatedClass annotated) {
            Map<String, NamedType> subtypes = new LinkedHashMap<>();
            subtypes.put("Subtype1", new NamedType(Subtype1.class));
            subtypes.put("Subtype2", new NamedType(Subtype2.class));
            subtypes.put("Subtype3", new NamedType(Subtype3.class));
            return subtypes.values();
        }
    }
}