package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"hello\",\"world\",\"test\"]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        String json = "null";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "[\"\"]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertArrayEquals(new String[]{""}, result);
    }

    @Test(expected = IOException.class)
    public void testMalformedJsonDeserialization() throws IOException {
        String json = "{\"abc\": [1, 2]}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, StringArrayDeserializer.class);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        JsonDeserializer<String> elementDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String json = "[\"hello\",\"world\",\"test\"]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, deserializer.getClass());
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }
}