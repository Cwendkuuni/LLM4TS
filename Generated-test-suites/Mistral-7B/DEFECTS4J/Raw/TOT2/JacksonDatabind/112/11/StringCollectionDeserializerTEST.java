package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Test;

public class StringCollectionDeserializerTest {
    @Test
    public void testStringCollectionDeserializerEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, mapper.getSerializationConfig(), null);
        assertEquals(0, result.size());
    }

    @Test
    public void testStringCollectionDeserializerSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, mapper.getSerializationConfig(), null);
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0));
    }

    @Test
    public void testStringCollectionDeserializerMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test1\", \"Test2\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, mapper.getSerializationConfig(), null);
        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0));
        assertEquals("Test2", result.get(1));
    }

    @Test(expected = IOException.class)
    public void testStringCollectionDeserializerInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalid\":\"json\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);
        List<String> result = deserializer.deserialize(parser, mapper.getSerializationConfig(), null);
    }
}