package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Collection> collectionType = Collection.class;
        Class<LinkedList> linkedListType = LinkedList.class;

        resolver.addMapping(collectionType, linkedListType);
        resolver.addMapping(collectionType, linkedListType); // Verify that adding duplicates does not throw an exception

        assertNotNull(resolver._mappings);
        assertEquals(1, resolver._mappings.size());
        Class<?> mappedType = resolver._mappings.get(new ClassKey(collectionType));
        assertNotNull(mappedType);
        assertEquals(linkedListType.getName(), mappedType.getName());
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Collection> collectionType = Collection.class;
        Class<LinkedList> linkedListType = LinkedList.class;

        resolver.addMapping(collectionType, linkedListType);

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new TypeReference<Collection>() {}.getType();

        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedType);
        assertEquals(linkedListType, mappedType.getRawType());
    }

    @Test
    public void testFindTypeMapping_NonExistentMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Collection> collectionType = Collection.class;

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new TypeReference<Collection>() {}.getType();

        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Collection> collectionType = Collection.class;

        DeserializationConfig config = new DeserializationConfig();
        JavaType javaType = new TypeReference<Collection>() {}.getType();

        JavaType mappedType = resolver.resolveAbstractType(config, javaType);
        assertNull(mappedType);
    }
}