package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    // Test fixture setup
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    // Basic test case for deserialize method with default configuration
    @Test
    public void testDeserializeDefault() throws IOException {
        String json = "[\"alpha\", \"beta\", \"gamma\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("alpha");
        expected.add("beta");
        expected.add("gamma");

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, result);
    }

    // Basic test case for deserialize method with custom value deserializer
    @Test
    public void testDeserializeCustomValueDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, null, null);
        String json = "[\"alpha\", \"beta\", \"gamma\"]";
        Collection<String> expected = new ArrayList<>();
        expected.add("alpha");
        expected.add("chi"); // Change the second value to test custom deserializer
        expected.add("gamma");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                customDeserializer, null, null, null, null);

        Collection<String> result = mapper.readerForListOf(String.class)
                .readValue(json, deserializer);
        assertEquals(expected, result);
    }

    // Test case for deserialize method with null values
    @Test
    public void testDeserializeNullValues() throws IOException {
        String json = "[null, \"alpha\", null]";
        Collection<String> expected = new ArrayList<>();
        expected.add(null);
        expected.add("alpha");
        expected.add(null);

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, result);
    }

    // Test case for deserialize method with empty array
    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        Collection<String> expected = new ArrayList<>();

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
        assertEquals(expected, result);
    }

    // Test case for deserialize method with non-array JSON input
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws IOException {
        String json = "{\"alpha\": \"value\"}";
        mapper.readValue(json, StringCollectionDeserializer.class); // expected exception
    }
}