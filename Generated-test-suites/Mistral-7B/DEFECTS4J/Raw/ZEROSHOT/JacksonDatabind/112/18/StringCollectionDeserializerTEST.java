package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testStringCollectionDeserializerEmpty() throws IOException {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringCollectionDeserializerSingleElement() throws IOException {
        String json = "[\"test\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        Assert.assertEquals(Collections.singletonList("test"), result);
    }

    @Test
    public void testStringCollectionDeserializerMultipleElements() throws IOException {
        String json = "[\"test1\", \"test2\", \"test3\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        Assert.assertEquals(Arrays.asList("test1", "test2", "test3"), result);
    }

    @Test
    public void testStringCollectionDeserializerNullValues() throws IOException {
        String json = "[null, \"test2\", null, \"test4\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        Assert.assertEquals(Arrays.asList(null, "test2", null, "test4"), result);
    }

    @Test
    public void testStringCollectionDeserializerCustomDeserializer() throws IOException {
        String json = "[{\"value\":\"test1\"}, {\"value\":\"test2\"}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        MyStringDeserializer valueDeserializer = new MyStringDeserializer();
        deserializer = deserializer.withResolved(null, valueDeserializer, null, null);
        List<String> result = deserializer.deserialize(parser, null);
        Assert.assertEquals(Arrays.asList("test1", "test2"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializerInvalidJson() throws IOException {
        String json = "{\"test\":\"invalid\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(parser, null);
    }

    private static class MyStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            return value.toUpperCase();
        }
    }
}