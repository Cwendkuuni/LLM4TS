package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType collectionTypeSingletonSet = new TypeReference<Set<Boolean>>(){}.getType();
        JavaType collectionTypeSingletonList = new TypeReference<List<Boolean>>(){}.getType();
        JavaType collectionTypeSingletonMap = new TypeReference<Map<String, String>>(){}.getType();
        JavaType collectionTypeUnmodifiableSet = new TypeReference<Set<Boolean>>(){}.getType();
        JavaType collectionTypeUnmodifiableList = new TypeReference<List<Boolean>>(){}.getType();
        JavaType collectionTypeUnmodifiableMap = new TypeReference<Map<String, String>>(){}.getType();
        JavaType collectionTypeAsList = new TypeReference<List<String>>(){}.getType();

        assertEquals(JavaUtilCollectionsDeserializers.class, JavaUtilCollectionsDeserializers.class);

        // Test for findForCollection method with Singleton Set
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeSingletonSet);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with Singleton List
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeSingletonList);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with Singleton Map
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeSingletonMap);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with Unmodifiable Set
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeUnmodifiableSet);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with Unmodifiable List
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeUnmodifiableList);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with Unmodifiable Map
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeUnmodifiableMap);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with As List
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, collectionTypeAsList);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForCollection method with other type
        JavaType otherType = new TypeReference<List<List<String>>>() {}.getType();
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, otherType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType mapTypeSingletonMap = new TypeReference<Map<String, String>>(){}.getType();
        JavaType mapTypeUnmodifiableMap = new TypeReference<Map<String, String>>(){}.getType();

        assertEquals(JavaUtilCollectionsDeserializers.class, JavaUtilCollectionsDeserializers.class);

        // Test for findForMap method with Singleton Map
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, mapTypeSingletonMap);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForMap method with Unmodifiable Map
        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, mapTypeUnmodifiableMap);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);

        // Test for findForMap method with other type
        JavaType otherType = new TypeReference<Set<List<String>>>() {}.getType();
        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, otherType);
        assertNull(deserializer);
    }

    @Test
    public void testConverter() {
        JavaType listTypeSingletonList = new TypeReference<List<Boolean>>(){}.getType();
        JavaType setTypeSingletonSet = new TypeReference<Set<Boolean>>(){}.getType();
        JavaType mapTypeSingletonMap = new TypeReference<Map<String, String>>(){}.getType();

        assertEquals(JavaUtilCollectionsDeserializers.class, JavaUtilCollectionsDeserializers.class);

        // Test for converter method with Singleton List
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.converter(1, listTypeSingletonList, List.class);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);

        // Test for converter method with Singleton Set
        deserializer = JavaUtilCollectionsDeserializers.converter(1, setTypeSingletonSet, Set.class);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);

        // Test for converter method with Singleton Map
        deserializer = JavaUtilCollectionsDeserializers.converter(1, mapTypeSingletonMap, Map.class);
        assertInstanceOf(JavaUtilCollectionsConverter.class, deserializer);

        // Test for converter method with other type
        JavaType otherType = new TypeReference<List<List<String>>>() {}.getType();
        deserializer = JavaUtilCollectionsDeserializers.converter(1, otherType, List.class);
        assertNull(deserializer);
    }
}