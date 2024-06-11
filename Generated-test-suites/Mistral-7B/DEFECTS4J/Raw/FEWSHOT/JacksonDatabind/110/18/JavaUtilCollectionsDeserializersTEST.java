package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonSet() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, String.class);
        Set<String> set = new HashSet<>(Arrays.asList("foo", "bar"));
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, set.getClass().getCanonicalName());

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeSet);
        Object result = mapper.readValue("\"foo,bar\"", javaTypeSet, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, result.getClass().getCanonicalName());
        assertSame(set, result);
    }

    @Test
    public void testFindForCollectionSingletonList() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, String.class);
        List<String> list = Collections.singletonList("foo");
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, list.getClass().getCanonicalName());

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
        Object result = mapper.readValue("\"foo\"", javaTypeList, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, result.getClass().getCanonicalName());
        assertSame(list, result);
    }

    @Test
    public void testFindForCollectionSingletonMap() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeMap);
        Object result = mapper.readValue("{\"key\":\"value\"}", javaTypeMap, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, result.getClass().getCanonicalName());
        assertSame(map, result);
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, String.class);
        Set<String> set = Collections.unmodifiableSet(Arrays.asList("foo", "bar"));

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeSet);
        Object result = mapper.readValue("\"foo,bar\"", javaTypeSet, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET, result.getClass().getCanonicalName());
        assertSame(set, result);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, String.class);
        List<String> list = Collections.unmodifiableList(Arrays.asList("foo", "bar"));

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
        Object result = mapper.readValue("\"foo,bar\"", javaTypeList, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST, result.getClass().getCanonicalName());
        assertSame(list, result);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = Collections.unmodifiableMap(new HashMap<>());

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeMap);
        Object result = mapper.readValue("{}", javaTypeMap, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, result.getClass().getCanonicalName());
        assertSame(map, result);
    }

    @Test
    public void testFindForCollectionNull() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, String.class);

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
        Object result = mapper.readValue("null", javaTypeList, deserializer);
        assertNull(result);
    }

    @Test
    public void testFindForMapSingletonMap() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
        Object result = mapper.readValue("{\"key\":\"value\"}", javaTypeMap, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, result.getClass().getCanonicalName());
        assertSame(map, result);
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = Collections.unmodifiableMap(new HashMap<>());

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
        Object result = mapper.readValue("{}", javaTypeMap, deserializer);
        assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, result.getClass().getCanonicalName());
        assertSame(map, result);
    }

    @Test
    public void testFindForMapNull() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);

        StdDelegatingDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
        Object result = mapper.readValue("null", javaTypeMap, deserializer);
        assertNull(result);
    }
}