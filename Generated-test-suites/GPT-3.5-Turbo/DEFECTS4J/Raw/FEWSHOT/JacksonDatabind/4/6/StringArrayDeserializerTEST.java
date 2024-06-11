package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"foo\", \"bar\"]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String[] expected = {"foo", "bar"};
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_CustomDeserializer() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"foo\", \"bar\"]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String[] expected = {"foo", "bar"};
        JsonDeserializer<String> customDeserializer = new CustomDeserializer();
        StringArrayDeserializer instance = new StringArrayDeserializer(customDeserializer);
        String[] result = instance.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithNullValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"foo\", null, \"bar\"]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String[] expected = {"foo", null, "bar"};
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithEmptyArray() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String[] expected = {};
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithInvalidToken() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"foo\": \"bar\"}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        try {
            StringArrayDeserializer.instance.deserialize(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unexpected IOException message", "Unexpected token (START_OBJECT), expected START_ARRAY", e.getMessage());
        }
    }

    private static class CustomDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText().toUpperCase();
        }
    }
}