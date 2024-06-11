package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassKey;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listType = List.class;
        Class<ArrayList> arrayListType = ArrayList.class;

        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);

        Map<ClassKey, Class<?>> actualMappings = resolver._mappings;

        assertNotNull(actualMappings);
        assertEquals(2, actualMappings.size());

        ClassKey key1 = new ClassKey(Collection.class);
        assertEquals(ArrayList.class, actualMappings.get(key1));

        ClassKey key2 = new ClassKey(List.class);
        assertEquals(ArrayList.class, actualMappings.get(key2));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listType = List.class;
        Class<ArrayList> arrayListType = ArrayList.class;

        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(listType);

        JavaType actualType = resolver.findTypeMapping(config, type);

        assertNotNull(actualType);
        assertEquals(ArrayList.class, actualType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithInvalidMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listType = List.class;
        Class<LinkedList> linkedListType = LinkedList.class;

        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(listType);

        JavaType actualType = resolver.findTypeMapping(config, type);

        assertNull(actualType);
    }

    @Test
    public void testFindTypeMappingWithNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType nullType = null;

        JavaType actualType = resolver.findTypeMapping(null, nullType);

        assertNull(actualType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listType = List.class;

        JavaType actualType = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(listType));

        assertNull(actualType);
    }
}