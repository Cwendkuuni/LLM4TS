package com.example.test; // replace with your test package name

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {
    

    @Test
    public void testDefaultSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Hello\",\"World\"]";
        Collection<String> collection = new ArrayList<>();
        collection.add("Hello");
        collection.add("World");
        String expectedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        Collection<String> deserializedCollection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(expectedJson, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedCollection));
    }

    @Test
    public void testNullSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null]";
        Collection<String> collection = new ArrayList<>();
        collection.add(null);
        String expectedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        Collection<String> deserializedCollection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(expectedJson, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedCollection));
    }

    @Test
    public void testEmptySerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<String> collection = new ArrayList<>();
        String expectedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        Collection<String> deserializedCollection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(expectedJson, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedCollection));
    }

    @Test
    public void testSingleValueSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test\"]";
        Collection<String> collection = new ArrayList<>();
        collection.add("Test");
        String expectedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        Collection<String> deserializedCollection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(expectedJson, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedCollection));
    }

    @Test
    public void testSerializationWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> valueDeserializer = new CustomStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, valueDeserializer, null, null);
        String json = "[\"Test1\",\"Test2\"]";
        Collection<String> collection = new ArrayList<>();
        collection.add("Test1");
        collection.add("Test2");
        String expectedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);

        Collection<String> deserializedCollection = mapper.readValue(json, deserializer.getClass());
        Assert.assertEquals(expectedJson, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserializedCollection));
    }

    @Test
    public void testDeserializationWithEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<String> collection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void testDeserializationWithNullValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[null, null]";
        Collection<String> collection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertTrue(collection.containsAll(List.of(null, null)));
    }

    @Test
    public void testDeserializationWithSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test\"]";
        Collection<String> collection = mapper.readValue(json, StringCollectionDeserializer.class);
        Assert.assertEquals(1, collection.size());
        Assert.assertEquals("Test", collection.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2]";
        mapper.readValue(json, StringCollectionDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNullDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        String json = "[\"Test1\",\"Test2\"]";
        deserializer.deserialize(null, null, null);
    }

    

    private static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString() + "_custom";
        }
    }
}