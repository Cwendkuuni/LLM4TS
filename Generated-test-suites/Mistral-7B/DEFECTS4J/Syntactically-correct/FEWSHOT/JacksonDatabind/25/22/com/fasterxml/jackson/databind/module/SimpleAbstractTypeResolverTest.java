package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @After
    public void teardown() {
        resolver = null;
    }

    @Test
    public void testFindTypeMapping() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(List.class);
        resolvedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertNull(resolver.findTypeMapping(config, javaType));
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType_List() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType_MappedType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
        assertNotNull(resolvedType);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_InvalidArgs() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SubTypeNotSubclass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, HashMap.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_AbstractClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
        List<Number> list = Arrays.asList(1, 2, 3);
        JavaType javaType = TypeFactory.defaultInstance().constructType(list.getClass());
        resolver.addMapping((Class<?>) List.class, (Class<? extends List<?>>) Collections.class);
    }
}