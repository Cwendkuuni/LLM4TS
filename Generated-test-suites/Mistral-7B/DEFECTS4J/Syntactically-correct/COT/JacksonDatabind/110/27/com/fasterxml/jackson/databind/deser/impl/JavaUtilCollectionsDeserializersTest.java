package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
        Set<Boolean> set = Collections.singleton(Boolean.TRUE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(set);
        Set<Boolean> resultSet = (Set<Boolean>) result;
        assertEquals(set, resultSet);
    }

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
        List<Boolean> list = Collections.singletonList(Boolean.TRUE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(list);
        List<Boolean> resultList = (List<Boolean>) result;
        assertEquals(list, resultList);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, Boolean.class, String.class);
        Map<Boolean, String> map = Collections.singletonMap(Boolean.TRUE, "Value");
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(map);
        Map<Boolean, String> resultMap = (Map<Boolean, String>) result;
        assertEquals(map, resultMap);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
        Set<Boolean> set = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(set);
        Set<Boolean> resultSet = (Set<Boolean>) result;
        assertEquals(set, resultSet);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
        List<Boolean> list = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(list);
        List<Boolean> resultList = (List<Boolean>) result;
        assertEquals(list, resultList);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, Boolean.class, String.class);
        Map<Boolean, String> map = Collections.unmodifiableMap(Collections.singletonMap(Boolean.TRUE, "Value"));
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(map);
        Map<Boolean, String> resultMap = (Map<Boolean, String>) result;
        assertEquals(map, resultMap);
    }

    @Test
    public void testFindForCollection_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
        Map<String, Integer> map = Collections.singletonMap("Key", 1);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(map);
        Map<String, Integer> resultMap = (Map<String, Integer>) result;
        assertEquals(map, resultMap);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
        Map<String, Integer> map = Collections.unmodifiableMap(Collections.singletonMap("Key", 1));
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertNotNull(deserializer);
        StdDelegatingDeserializer<Object> stdDelegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object, Object> converter = stdDelegatingDeserializer.getDelegate();
        Object result = converter.convert(map);
        Map<String, Integer> resultMap = (Map<String, Integer>) result;
        assertEquals(map, resultMap);
    }

    @Test
    public void testFindForMap_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertNull(deserializer);
    }
}