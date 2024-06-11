package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Test adding a mapping for a Collection to a LinkedList
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        Map<ClassKey, Class<?>> mapping = resolver._mappings;
        assertEquals(2, mapping.size());
        assertEquals(Collection.class, mapping.keySet().iterator().next().getRawClass());
        assertEquals(LinkedList.class, mapping.values().iterator().next());
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Test adding a mapping for a Collection to a LinkedList
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = config.constructType(Collection.class);
        JavaType expectedType = config.constructType(LinkedList.class);

        JavaType actualType = resolver.findTypeMapping(config, javaType);
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMapping_Null() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = null;

        JavaType actualType = resolver.findTypeMapping(config, javaType);
        assertNull(actualType);
    }

    @Test
    public void testFindTypeMapping_NonAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = config.constructType(String.class);

        JavaType actualType = resolver.findTypeMapping(config, javaType);
        assertNull(actualType);
    }

    @Test
    public void testFindTypeMapping_NonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType superType = config.constructType(Collection.class);
        JavaType subType = config.constructType(ArrayList.class);

        resolver.addMapping(Collection.class, LinkedList.class);

        JavaType actualType = resolver.findTypeMapping(config, superType);
        assertNull(actualType);

        JavaType expectedType = config.constructType(LinkedList.class);
        JavaType actualType2 = resolver.findTypeMapping(config, subType);
        assertNull(actualType2);

        JavaType actualType3 = resolver.findTypeMapping(config, expectedType);
        assertEquals(expectedType, actualType3);
    }

    @Test
    public void testFindTypeMapping_Cycle() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType superType = config.constructType(A.class);
        JavaType subType = config.constructType(B.class);

        // Add mapping A -> B
        resolver.addMapping(A.class, B.class);

        // Add mapping B -> A
        resolver.addMapping(B.class, A.class);

        JavaType actualType = resolver.findTypeMapping(config, superType);
        assertNull(actualType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = config.constructType(Collection.class);

        JavaType actualType = resolver.resolveAbstractType(config, javaType);
        assertNull(actualType);
    }

    static class A {}

    static class B extends A {}

    static class C extends List<Object> {
    }

    static class D extends ArrayList<Object> implements Collection<Object> {
    }

    static class LinkedList<T> extends ArrayList<T> implements List<T> {
    }
}