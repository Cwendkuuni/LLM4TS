package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeSingletonList = mapper.getTypeFactory().constructType(Collections.singletonList(String.class).getClass());
        JsonDeserializer<?> jsonDeserializerSingletonList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSingletonList);
        assertEquals(List.class, jsonDeserializerSingletonList.getTargetValueType(null));

        JavaType javaTypeSingletonSet = mapper.getTypeFactory().constructType(Collections.singleton(Boolean.TRUE).getClass());
        JsonDeserializer<?> jsonDeserializerSingletonSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeSingletonSet);
        assertEquals(Set.class, jsonDeserializerSingletonSet.getTargetValueType(null));

        JavaType javaTypeUnmodifiableList = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass());
        JsonDeserializer<?> jsonDeserializerUnmodifiableList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeUnmodifiableList);
        assertEquals(List.class, jsonDeserializerUnmodifiableList.getTargetValueType(null));

        JavaType javaTypeUnmodifiableSet = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList(1, 2, 3)).getClass());
        JsonDeserializer<?> jsonDeserializerUnmodifiableSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeUnmodifiableSet);
        assertEquals(Set.class, jsonDeserializerUnmodifiableSet.getTargetValueType(null));

        JavaType javaTypeAsList = mapper.getTypeFactory().constructType(List.class);
        JsonDeserializer<?> jsonDeserializerAsList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeAsList);
        assertEquals(List.class, jsonDeserializerAsList.getTargetValueType(null));

        // Test for null and non-matching types
        JavaType javaTypeInteger = mapper.getTypeFactory().constructType(int.class);
        JsonDeserializer<?> jsonDeserializerInteger = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaTypeInteger);
        assertNull(jsonDeserializerInteger);
    }

    @Test
    public void testFindForMap() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType javaTypeSingletonMap = mapper.getTypeFactory().constructType(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> jsonDeserializerSingletonMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeSingletonMap);
        assertEquals(Map.class, jsonDeserializerSingletonMap.getTargetValueType(null));

        JavaType javaTypeUnmodifiableMap = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Arrays.asList(new AbstractMap.SimpleEntry<>("a", "b"), new AbstractMap.SimpleEntry<>("c", "d"))).getClass());
        JsonDeserializer<?> jsonDeserializerUnmodifiableMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeUnmodifiableMap);
        assertEquals(Map.class, jsonDeserializerUnmodifiableMap.getTargetValueType(null));

        // Test for null and non-matching types
        JavaType javaTypeInteger = mapper.getTypeFactory().constructType(int.class);
        JsonDeserializer<?> jsonDeserializerInteger = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaTypeInteger);
        assertNull(jsonDeserializerInteger);
    }
}