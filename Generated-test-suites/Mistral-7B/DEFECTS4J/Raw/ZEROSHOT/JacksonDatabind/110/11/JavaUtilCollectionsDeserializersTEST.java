package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeForSingletonList = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializerForSingletonList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeForSingletonList);
        assertNotNull(deserializerForSingletonList);

        JavaType javaTypeForSingletonSet = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializerForSingletonSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeForSingletonSet);
        assertNotNull(deserializerForSingletonSet);

        JavaType javaTypeForUnmodifiableList = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializerForUnmodifiableList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeForUnmodifiableList);
        assertNotNull(deserializerForUnmodifiableList);

        JavaType javaTypeForUnmodifiableSet = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializerForUnmodifiableSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeForUnmodifiableSet);
        assertNotNull(deserializerForUnmodifiableSet);

        JavaType javaTypeForAsList = mapper.getTypeFactory().constructType(List.class, Integer[].class);
        JsonDeserializer<?> deserializerForAsList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeForAsList);
        assertNotNull(deserializerForAsList);

        JavaType javaTypeForEmptyList = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<?> deserializerForEmptyList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeForEmptyList);
        assertNull(deserializerForEmptyList);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeForSingletonMap = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializerForSingletonMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeForSingletonMap);
        assertNotNull(deserializerForSingletonMap);

        JavaType javaTypeForUnmodifiableMap = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializerForUnmodifiableMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeForUnmodifiableMap);
        assertNotNull(deserializerForUnmodifiableMap);

        JavaType javaTypeForEmptyMap = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializerForEmptyMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeForEmptyMap);
        assertNull(deserializerForEmptyMap);
    }

    @Test
    public void testDeserializeCollections() throws JsonMappingException {
        String jsonForSingletonSet = "{\"class\":\"java.util.Collections$SingletonSet\",\"elements\":[{\"c\":1,\"v\":true}]}";
        String jsonForSingletonList = "{\"class\":\"java.util.Collections$SingletonList\",\"elements\":[{\"c\":1,\"v\":1}]}";
        String jsonForUnmodifiableSet = "{\"class\":\"java.util.Collections$UnmodifiableSet\",\"elements\":[{\"c\":1,\"v\":true}]}";
        String jsonForUnmodifiableList = "{\"class\":\"java.util.Collections$UnmodifiableList\",\"elements\":[{\"c\":1,\"v\":1}]}";
        String jsonForAsList = "[1,2,3]";

        JsonDeserializer<?> deserializerForSingletonSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE));
        Set<Boolean> singletonSet = mapper.readValue(jsonForSingletonSet, mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE), deserializerForSingletonSet);
        assertEquals(1, singletonSet.size());
        assertEquals(true, singletonSet.iterator().next());

        JsonDeserializer<?> deserializerForSingletonList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, mapper.getTypeFactory().constructType(List.class, Boolean.TYPE));
        List<Boolean> singletonList = mapper.readValue(jsonForSingletonList, mapper.getTypeFactory().constructType(List.class, Boolean.TYPE), deserializerForSingletonList);
        assertEquals(1, singletonList.size());
        assertEquals(true, singletonList.get(0));

        JsonDeserializer<?> deserializerForUnmodifiableSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE));
        Set<Boolean> unmodifiableSet = mapper.readValue(jsonForUnmodifiableSet, mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE), deserializerForUnmodifiableSet);
        assertEquals(1, unmodifiableSet.size());
        assertEquals(true, unmodifiableSet.iterator().next());

        JsonDeserializer<?> deserializerForUnmodifiableList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, mapper.getTypeFactory().constructType(List.class, Boolean.TYPE));
        List<Boolean> unmodifiableList = mapper.readValue(jsonForUnmodifiableList, mapper.getTypeFactory().constructType(List.class, Boolean.TYPE), deserializerForUnmodifiableList);
        assertEquals(1, unmodifiableList.size());
        assertEquals(true, unmodifiableList.get(0));

        JsonDeserializer<?> deserializerForAsList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, mapper.getTypeFactory().constructType(List.class, Integer[].class));
        List<Integer> asList = mapper.readValue(jsonForAsList, mapper.getTypeFactory().constructType(List.class, Integer[].class), deserializerForAsList);
        assertEquals(3, asList.size());
        assertEquals(Arrays.asList(1, 2, 3), asList);
    }

    @Test
    public void testDeserializeMaps() throws JsonMappingException {
        String jsonForSingletonMap = "{\"class\":\"java.util.Collections$SingletonMap\",\"entries\":[{\"k\":\"a\",\"v\":1}]}";
        String jsonForUnmodifiableMap = "{\"class\":\"java.util.Collections$UnmodifiableMap\",\"entries\":[{\"k\":\"a\",\"v\":1}]}";

        JsonDeserializer<?> deserializerForSingletonMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class));
        Map<String, Integer> singletonMap = mapper.readValue(jsonForSingletonMap, mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class), deserializerForSingletonMap);
        assertEquals(1, singletonMap.size());
        assertEquals("a", singletonMap.keySet().iterator().next());
        assertEquals(1, singletonMap.values().iterator().next());

        JsonDeserializer<?> deserializerForUnmodifiableMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class));
        Map<String, Integer> unmodifiableMap = mapper.readValue(jsonForUnmodifiableMap, mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class), deserializerForUnmodifiableMap);
        assertEquals(1, unmodifiableMap.size());
        assertEquals("a", unmodifiableMap.keySet().iterator().next());
        assertEquals(1, unmodifiableMap.values().iterator().next());
    }
}