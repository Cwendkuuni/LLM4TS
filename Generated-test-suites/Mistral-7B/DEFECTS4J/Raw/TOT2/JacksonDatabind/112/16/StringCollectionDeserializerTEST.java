package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testStringCollectionDeserializer() throws IOException, JsonMappingException {
        // Test deserializing an empty collection
        JsonNode jsonEmpty = mapper.readTree("[]");
        Collection<String> emptyCollection = mapper.readerForUpdating(new ArrayList<>())
                .readValue(jsonEmpty);
        assertTrue(emptyCollection.isEmpty());

        // Test deserializing a collection with one String element
        JsonNode jsonOneElement = mapper.readTree("[\"hello\"]");
        Collection<String> oneElementCollection = mapper.readerForUpdating(new ArrayList<>())
                .readValue(jsonOneElement);
        assertEquals(oneElementCollection.size(), 1);
        assertEquals(oneElementCollection.iterator().next(), "hello");

        // Test deserializing a collection with multiple String elements
        JsonNode jsonMultipleElements = mapper.readTree("[\"hello\", \"world\"]");
        Collection<String> multipleElementsCollection = mapper.readerForUpdating(new ArrayList<>())
                .readValue(jsonMultipleElements);
        assertEquals(multipleElementsCollection.size(), 2);
        assertTrue(multipleElementsCollection.containsAll(List.of("hello", "world")));
    }

    @Test
    public void testStringCollectionDeserializerWithCustomValueDeserializer() throws IOException, JsonMappingException {
        // Test deserializing a collection with custom String deserializer
        JsonNode jsonCustomString = mapper.readTree("[{\"value\": \"hello\"}]");
        Collection<String> customStringCollection = mapper.readerForUpdating(new ArrayList<>())
                .readValue(jsonCustomString);
        assertEquals(customStringCollection.size(), 1);
        assertEquals(customStringCollection.iterator().next(), "hello");
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializerWithInvalidJson() throws IOException {
        // Test deserializing an invalid JSON string
        JsonNode jsonInvalid = mapper.readTree("[]hello");
        Collection<String> invalidCollection = mapper.readerForUpdating(new ArrayList<>())
                .readValue(jsonInvalid);
        fail("Expected JsonMappingException but received a Collection instead.");
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCollectionDeserializerWithNullValue() throws IOException {
        // Test deserializing a collection with a null value
        JsonNode jsonNullValue = mapper.readTree("[null]");
        Collection<String> nullValueCollection = mapper.readerForUpdating(new ArrayList<>())
                .readValue(jsonNullValue);
        fail("Expected JsonMappingException but received a Collection instead.");
    }
}