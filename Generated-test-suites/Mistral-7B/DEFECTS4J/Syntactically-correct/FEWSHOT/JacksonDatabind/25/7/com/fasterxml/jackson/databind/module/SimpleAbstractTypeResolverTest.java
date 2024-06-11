package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = JavaType.construct(List.class, String.class);

        JavaType resolvedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_EmptyMapping() {
        SimpleAbstractTypeResolver emptyResolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(List.class, String.class);

        JavaType resolvedType = emptyResolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMapping_NoMapping() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = JavaType.construct(Map.class, String.class, Integer.class);

        JavaType resolvedType = resolver.findTypeMapping(config, javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMapping_DifferentType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = JavaType.construct(ArrayList.class, String.class);

        JavaType resolvedType = resolver.findTypeMapping(config, javaType);
        assertNull(resolvedType);
    }
}