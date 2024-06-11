package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.CollectionReferringAccumulator;
import com.fasterxml.jackson.databind.util.CollectionReferring;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CollectionDeserializer
 */
public class CollectionDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        List<Integer> result = mapper.readValue(json, deserializer);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSingleElementArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        CollectionDeserializer<List<Integer>> deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        List<Integer> result = mapper.readValue(json, deserializer);
        assertEquals(1, result.size());
        assertEquals((Integer) 1, result.get(0));
    }

    @Test
    public void testDeserializeMultipleElementsArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        CollectionDeserializer<List<Integer>> deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        List<Integer> result = mapper.readValue(json, deserializer);
        assertEquals(3, result.size());
        assertEquals((Integer) 1, result.get(0));
        assertEquals((Integer) 2, result.get(1));
        assertEquals((Integer) 3, result.get(2));
    }

    @Test
    public void testDeserializeWithTypeAndValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class, Integer.class);
        JsonDeserializer<Integer> integerDeserializer = mapper.deserializerFactory().findValueDeserializer(Integer.class);
        CollectionDeserializer<List<Integer>> deserializer = new CollectionDeserializer(javaType, integerDeserializer, null, null, null, null);
        List<Integer> result = mapper.readValue(json, deserializer);
        assertEquals(3, result.size());
        assertEquals((Integer) 1, result.get(0));
        assertEquals((Integer) 2, result.get(1));
        assertEquals((Integer) 3, result.get(2));
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class, String.class);
        TypeDeserializer stringTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class);
        CollectionDeserializer<List<String>> deserializer = new CollectionDeserializer(javaType, null, stringTypeDeserializer, null, null, null);
        List<String> result = mapper.readValue(json, deserializer);
        assertEquals(3, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }

    @Test
    public void testDeserializeWithValueDeserializerAndTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class, Integer.class);
        JsonDeserializer<Integer> integerDeserializer = mapper.deserializerFactory().findValueDeserializer(Integer.class);
        TypeDeserializer stringTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class);
        CollectionDeserializer<List<Integer>> deserializer = new CollectionDeserializer(javaType, integerDeserializer, stringTypeDeserializer, null, null, null);
        List<Integer> result = mapper.readValue(json, deserializer);
        assertEquals(3, result.size());
        assertEquals((Integer) 1, result.get(0));
        assertEquals((Integer) 2, result.get(1));
        assertEquals((Integer) 3, result.get(2));
    }

    @Test
    public void testDeserializeWithValueDeserializerAndTypeDeserializerAndDelegateDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class, Map.class);
        JsonDeserializer<Map<String,Object>> mapDeserializer = mapper.deserializerFactory().findValueDeserializer(Map.class);
        TypeDeserializer stringTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class);
        CollectionDeserializer<List<Map<String,Object>>> deserializer = new CollectionDeserializer(javaType, mapDeserializer, stringTypeDeserializer, null, null, null);
        List<Map<String,Object>> result = mapper.readValue(json, deserializer);
        assertEquals(2, result.size());
        Map<String,Object> first = result.get(0);
        assertEquals("John", first.get("name"));
        assertEquals(30, first.get("age"));
        Map<String,Object> second = result.get(1);
        assertEquals("Jane", second.get("name"));
        assertEquals(25, second.get("age"));
    }

    @Test
    public void testDeserializeWithValueDeserializerAndTypeDeserializerAndDelegateDeserializerAndUnwrappingSingleValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[30]";
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class, Integer.class);
        JsonDeserializer<Integer> integerDeserializer = mapper.deserializerFactory().findValueDeserializer(Integer.class);
        TypeDeserializer stringTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class);
        CollectionDeserializer<List<Integer>> deserializer = new CollectionDeserializer(javaType, integerDeserializer, stringTypeDeserializer, null, null, Boolean.TRUE);
        List<Integer> result = mapper.readValue(json, deserializer);
        assertEquals(1, result.size());
        assertEquals((Integer) 30, result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[";
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        mapper.readValue(json, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJsonWhenUnwrappingSingleValueIsFalse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[";
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        deserializer = deserializer.withResolved(null, null, null, Boolean.FALSE);
        try {
            mapper.readValue(json, deserializer);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException("Unexpected exception", e);
        }
        fail("Expected JsonMappingException to be thrown");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"hello\"";
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        try {
            mapper.readValue(json, deserializer);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException("Unexpected exception", e);
        }
        fail("Expected JsonMappingException to be thrown");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class);
        try {
            mapper.readValue(json, deserializer);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException("Unexpected exception", e);
        }
        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testDeserializeWithUnresolvedForwardReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class, Integer.class);
        CollectionDeserializer<List<Integer>> deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);
        List<Integer> result = mapper.readValue("[]", deserializer);
        Object id = new Object();
        deserializer.handleResolvedForwardReference(id, 42);
        List<Integer> resolvedResult = mapper.readValue("[]", deserializer);
        assertEquals(1 + result.size(), resolvedResult.size());
        assertEquals(result, resolvedResult.subList(0, result.size()));
        assertEquals(42, resolvedResult.get(result.size()));
    }
}