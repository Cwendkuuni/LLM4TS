package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        Map<ClassKey, Class<?>> mappingMap = resolver._mappings;
        assertEquals(2, mappingMap.size());

        ClassKey listKey = new ClassKey(List.class);
        assertEquals(LinkedList.class, mappingMap.get(listKey));

        ClassKey collectionKey = new ClassKey(Collection.class);
        assertEquals(LinkedList.class, mappingMap.get(collectionKey));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = null;
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        JavaType resolvedType = resolver.findTypeMapping(config, javaType);

        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_NullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = null;
        JavaType resolvedType = resolver.findTypeMapping(null, javaType);

        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMapping_InvalidType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        DeserializationConfig config = null;
        JavaType resolvedType = resolver.findTypeMapping(config, javaType);

        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;

        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        JavaType resolvedType = resolver.resolveAbstractType(null, javaType);

        assertNull(resolvedType);
    }
}