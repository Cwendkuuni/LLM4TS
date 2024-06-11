package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class for StringCollectionDeserializer.
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"hello\", \"world\"]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(objectMapper.readTree(json), null);
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        String json = "[{\"value\":\"hello\"},{\"value\":\"world\"}]";
        JsonDeserializer<String> customValueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, customValueDeserializer, null, null);
        List<String> result = deserializer.deserialize(objectMapper.readTree(json), null);
        assertEquals(Arrays.asList("hello", "world"), result);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        String json = "[null]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, new JsonNullValueProvider());
        List<String> result = deserializer.deserialize(objectMapper.readTree(json), null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        String json = "[]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(objectMapper.readTree(json), null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testInvalidJsonDeserialization() throws IOException {
        String json = "{\"invalid\":\"json\"}";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        try {
            deserializer.deserialize(objectMapper.readTree(json), null);
            fail();
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "[1, 2, 3]";
        TypeDeserializer typeDeserializer = new StandardTypeDeserializer();
        List<Integer> result = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class), null, null, null, null, null).deserializeWithType(
                objectMapper.readTree(json), null, typeDeserializer);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

}

class StdDeserializer<T> extends JsonDeserializer<T> {}