package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        assertNotNull(resolver._mappings);
        assertEquals(2, resolver._mappings.size());
    }

    @Test
    public void testFindTypeMapping_CollectionToLinkedList() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType linkedListType = TypeFactory.defaultInstance().constructType(LinkedList.class);

        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), collectionType);

        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_ListToLinkedList() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType linkedListType = TypeFactory.defaultInstance().constructType(LinkedList.class);

        resolver.addMapping(List.class, LinkedList.class);
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), listType);

        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_InvalidMapping() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        resolver.addMapping(Collection.class, String.class);
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), collectionType);

        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMapping_NullType() {
        JavaType nullType = null;
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), nullType);

        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMapping_NoMapping() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), stringType);

        assertNull(resolvedType);
    }
}