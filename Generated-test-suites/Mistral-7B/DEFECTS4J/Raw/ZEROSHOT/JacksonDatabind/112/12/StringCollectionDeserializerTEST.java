package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<String> expected = new ArrayList<>();
        Collection<String> actual = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testSingleElementCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("test");
        Collection<String> actual = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testMultipleElementsCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test1\",\"test2\",\"test3\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("test1");
        expected.add("test2");
        expected.add("test3");
        Collection<String> actual = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        serializer._valueDeserializer = customDeserializer;
        String json = "[\"test1\",\"test2\",\"test3\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("test1");
        expected.add("test2");
        expected.add("test3");
        Collection<String> actual = mapper.readValue(json, serializer.getClass());
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"test\": \"value\"}";
        mapper.readValue(json, StringCollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullPointerException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer._nullProvider = null;
        Collection<String> actual = deserializer.deserialize(null, new JsonFactory(), null);
    }
}