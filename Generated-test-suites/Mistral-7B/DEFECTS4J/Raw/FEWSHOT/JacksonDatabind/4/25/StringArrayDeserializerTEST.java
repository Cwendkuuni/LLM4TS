package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test cases for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(json, deserializer);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(json, deserializer);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testMultipleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", \"bar\", \"baz\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(json, deserializer);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null, null, null]";
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(json, deserializer);
        assertArrayEquals(new String[]{null, null, null}, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "invalidJson";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(json, deserializer);
        fail();
    }

    @Test
    public void testContextualDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\", null, \"bar\"]";
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = mapper.readerForValueType(String[].class)
                .readValue(json, deserializer);
        assertArrayEquals(new String[]{"foo", null, "bar"}, result);
    }
}