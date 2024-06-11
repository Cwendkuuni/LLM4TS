package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize_WithNullValueDeserializer_ShouldReturnEmptyCollection() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_WithCustomValueDeserializer_ShouldReturnCollectionWithCustomValues() throws IOException {
        JsonDeserializer<String> valueDeserializer = new CustomStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, null);
        Collection<String> result = deserializer.deserialize(null, null);
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Custom1");
        expected.add("Custom2");
        assertEquals(expected, result);
    }

    // Add more test cases for other methods if needed

}

class CustomStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return "Custom1"; // Return a custom value
    }

}