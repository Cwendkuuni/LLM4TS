package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setup() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        // Testing the deserialize method for a valid array of strings
        String input = "[\"foo\", \"bar\", \"baz\"]";
        String[] expected = {"foo", "bar", "baz"};
        assertEquals(expected, stringArrayDeserializer.deserialize(input));
    }

    @Test
    public void testDeserializeWithNulls() {
        // Edge case
        // Testing the deserialize method for an array with null values
        String input = "[\"foo\", null, \"bar\"]";
        String[] expected = {"foo", null, "bar"};
        assertEquals(expected, stringArrayDeserializer.deserialize(input));
    }

    @Test
    public void testDeserializeEmptyArray() {
        // Edge case
        // Testing the deserialize method for an empty array
        String input = "[]";
        String[] expected = {};
        assertEquals(expected, stringArrayDeserializer.deserialize(input));
    }

    @Test
    public void testDeserializeWithEmptyStringAsNull() {
        // Error scenario
        // Testing the deserialize method for an array with empty string as null
        String input = "[\"\"]";
        String[] expected = {null};
        assertEquals(expected, stringArrayDeserializer.deserialize(input));
    }

    @Test
    public void testDeserializeWithType() {
        // Typical use case
        // Testing the deserializeWithType method for a valid type
        String input = "{\"type\":\"string_array\",\"values\":[\"foo\", \"bar\"]}";
        String[] expected = {"foo", "bar"};
        assertEquals(expected, stringArrayDeserializer.deserializeWithType(input));
    }

}