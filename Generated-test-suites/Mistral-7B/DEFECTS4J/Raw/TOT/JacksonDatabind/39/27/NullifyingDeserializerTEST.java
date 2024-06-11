package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test cases for NullifyingDeserializer class.
 */
public class NullifyingDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test nullifying deserialization of empty JSON object.
     *
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testEmptyJsonObjectDeserialization() throws JsonProcessingException {
        String json = "{}";
        Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
        assertNull(result);
    }

    /**
     * Test nullifying deserialization of empty JSON array.
     *
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testEmptyJsonArrayDeserialization() throws JsonProcessingException {
        String json = "[{}]";
        Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
        assertNull(result);
    }

    /**
     * Test nullifying deserialization of JSON object with fields.
     *
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testJsonObjectDeserialization() throws JsonProcessingException {
        String json = "{\"field1\": 1, \"field2\": [1, 2, 3]}";
        Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
        assertNull(result);
    }

    /**
     * Test nullifying deserialization of JSON array.
     *
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testJsonArrayDeserialization() throws JsonProcessingException {
        String json = "[1, 2, 3]";
        Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
        assertNull(result);
    }

    /**
     * Test nullifying deserialization of JSON with invalid token.
     *
     * @throws JsonProcessingException if deserialization fails
     */
    @Test(expected = IOException.class)
    public void testInvalidJsonDeserialization() throws JsonProcessingException {
        String json = "{'field1': 1, 'field2': [1, 2, 3]}"; // missing leading '}' in the first JSON token
        Object result = objectMapper.readValue(json, NullifyingDeserializer.class);
        assertNull(result);
    }

    /**
     * Test nullifying deserialization with custom TypeDeserializer.
     *
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testDeserializationWithTypeDeserializer() throws JsonProcessingException {
        String json = "{\"className\": \"com.MyClass\", \"data\": [1, 2, 3]}";
        TypeDeserializer typeDeserializer = new CustomTypeDeserializer(); // replace this with your custom TypeDeserializer
        Object result = objectMapper.readValue(json, NullifyingDeserializer.class, typeDeserializer);
        assertNull(result);
    }
}