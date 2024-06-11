package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testSerializeDeserialize() throws IOException {
        String json = "[\"hello\", \"world\", \"jackson\"]";
        JsonParser jsonParser = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertArrayEquals(new String[]{"hello", "world", "jackson"}, result);
    }

    @Test
    public void test_deserializeCustom() throws IOException {
        String json = "[\"hello\", \"world\", \"jackson\"]";
        JsonParser jsonParser = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        String[] result = deserializer._deserializeCustom(jsonParser, new TestContext());
        Assert.assertArrayEquals(new String[]{"hello", "world", "jackson"}, result);
    }

    @Test
    public void testSerializeDeserializeWithType() throws IOException {
        String json = "[\"hello\", \"world\", \"jackson\"]";
        JsonParser jsonParser = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForArray(String[].class);
        String[] result = deserializer.deserializeWithType(jsonParser, new TestContext(), typeDeserializer);
        Assert.assertArrayEquals(new String[]{"hello", "world", "jackson"}, result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        String json = "[]";
        JsonParser jsonParser = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testNullInput() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(null, new TestContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException ex) {
            // Expected
        }
    }

    @Test
    public void testInvalidInput() throws IOException {
        String json = "{\"name\": \"John\"}";
        JsonParser jsonParser = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.deserialize(jsonParser, new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // Expected
        }
    }

    @Test
    public void testEmptyString() throws IOException {
        String json = "\"\"";
        JsonParser jsonParser = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jsonParser, new TestContext());
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testCreateContextualNullContext() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        try {
            deserializer.createContextual(null, new StubProperty());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException ex) {
            // Expected
        }
    }
}