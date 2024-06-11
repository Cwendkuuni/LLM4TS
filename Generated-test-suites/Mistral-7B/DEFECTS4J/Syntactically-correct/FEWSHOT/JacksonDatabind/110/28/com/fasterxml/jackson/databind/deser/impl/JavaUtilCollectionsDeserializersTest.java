package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonSet() {
        JavaType type = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionSingletonList() {
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionSingletonMap() {
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() {
        JavaType type = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, Collections.unmodifiableSet( Arrays.asList(Boolean.TRUE) ).getType());
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() {
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, Collections.unmodifiableList( Arrays.asList("foo") ).getType());
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, keyType, valueType);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, Collections.unmodifiableMap( new HashMap<String, String>() {{ put("foo", "bar"); }} ).getType());
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForCollectionAsList() {
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForCollection(null, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMapSingletonMap() {
        JavaType keyType = String.class;
        JavaType valueType = String.class;
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, keyType, valueType);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForMap(null, type);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }

    @Test
    public void testFindForMapUnmodifiableMap() {
        JavaType keyType = String.class;
        JavaType valueType = String.class;
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class, keyType, valueType);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();
        JsonDeserializer<?> deserializer = deserializers.findForMap(null, Collections.unmodifiableMap( new HashMap<String, String>() {{ put("foo", "bar"); }} ).getType());
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);
    }
}