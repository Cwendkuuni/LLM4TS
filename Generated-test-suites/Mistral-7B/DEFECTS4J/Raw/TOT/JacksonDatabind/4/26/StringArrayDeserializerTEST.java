package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArray() throws JsonProcessingException {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws JsonProcessingException {
        JsonParser jp = createParser("[\"hello\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testMultipleElementsArray() throws JsonProcessingException {
        JsonParser jp = createParser("[\"hello\", \"world\", null]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", "world", null}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws JsonProcessingException {
        JsonParser jp = createParser("{\"value\": \"hello\"}");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullElementDeserializer() throws JsonProcessingException {
        JsonDeserializer<String> nullDeserializer = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer(nullDeserializer);
        JsonParser jp = createParser("[null]");
        deserializer.deserialize(jp, new TestContext());
    }

    @Test
    public void testCustomElementDeserializer() throws JsonProcessingException {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonParser jp = createParser("[\"hello\", \"world\"]");
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.parseParser("[1, 2, 3]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(jp, new TestContext(), mapper.getTypeFactory().constructType(String[].class));
        assertArrayEquals(new String[]{"1", "2", "3"}, result);
    }

    private JsonParser createParser(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(mapper.readTree(json).getRawValue());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer();
        }

        @Override
        public void returnObjectBuffer(ObjectBuffer buffer) {
            // no-op
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }
    }

}