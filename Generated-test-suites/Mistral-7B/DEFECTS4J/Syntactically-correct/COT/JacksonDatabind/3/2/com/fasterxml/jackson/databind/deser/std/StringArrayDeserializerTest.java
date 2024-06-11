package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeDefault() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[\"abc\",\"def\",\"ghi\"]");
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertEquals("abc", result[0]);
        assertEquals("def", result[1]);
        assertEquals("ghi", result[2]);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[\"abc\",\"def\",\"ghi\"]");
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertEquals("abc", result[0]);
        assertEquals("def", result[1]);
        assertEquals("ghi", result[2]);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[]");
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("null");
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[\"abc\",\"def\",\"ghi\"]");
        Object result = deserializer.deserializeWithType(jp, new TestContext(), null);
        assertArrayEquals(value -> (String) value, new String[] {"abc", "def", "ghi"}, result.getClass().getComponentType());
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("{\"abc\":\"def\"}");
        deserializer.deserialize(jp, new TestContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingStartArrayToken() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("\"abc\"");
        deserializer.deserialize(jp, new TestContext());
    }

    @Test
    public void testCreateContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        DeserializationContext ctxt = new TestContext();
        BeanProperty property = new BeanProperty("testProperty", String[].class);
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(deserializer, result.getClass().getDeclaredField("_elementDeserializer").get(result));
    }

    // Add more test cases for edge cases and exception handling scenarios if necessary

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer(new Object[16]);
        }
    }

}