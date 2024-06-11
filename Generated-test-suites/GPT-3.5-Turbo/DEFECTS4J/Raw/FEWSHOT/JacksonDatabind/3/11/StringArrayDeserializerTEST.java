package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test when input json is an array of strings
        String[] expected = {"foo", "bar"};
        String[] actual = deserializer.deserialize("[\"foo\", \"bar\"]");
        assertArrayEquals(expected, actual);
        
        // Test when input json is an empty array
        expected = new String[0];
        actual = deserializer.deserialize("[]");
        assertArrayEquals(expected, actual);
        
        // Test when input json is an array with null values
        expected = new String[2];
        actual = deserializer.deserialize("[null, null]");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test when single value is provided and ACCEPT_SINGLE_VALUE_AS_ARRAY feature is disabled
        try {
            deserializer.handleNonArray("foo");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Exception expected
        }

        // Test when single value is an empty string and ACCEPT_EMPTY_STRING_AS_NULL_OBJECT feature is disabled
        try {
            deserializer.handleNonArray("");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Exception expected
        }

        // Test when single value is an empty string and ACCEPT_EMPTY_STRING_AS_NULL_OBJECT feature is enabled
        assertNull(deserializer.handleNonArray(""));

        // Test when single value is null
        String[] expected = {null};
        String[] actual = deserializer.handleNonArray("null");
        assertArrayEquals(expected, actual);

        // Test when single value is non-null
        expected = new String[]{ "foo" };
        actual = deserializer.handleNonArray("foo");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test when input json is an array of strings with type information
        String[] expected = {"foo", "bar"};
        String[] actual = deserializer.deserializeWithType("[\"foo\", \"bar\"]");
        assertArrayEquals(expected, actual);
        
        // Test when input json is an empty array with type information
        expected = new String[0];
        actual = deserializer.deserializeWithType("[]");
        assertArrayEquals(expected, actual);
        
        // Test when input json is an array with null values and type information
        expected = new String[2];
        actual = deserializer.deserializeWithType("[null, null]");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test when input json is an array of strings with custom deserializer
        String[] expected = {"foo", "bar"};
        String[] actual = deserializer._deserializeCustom("[\"foo\", \"bar\"]");
        assertArrayEquals(expected, actual);
        
        // Test when input json is an empty array with custom deserializer
        expected = new String[0];
        actual = deserializer._deserializeCustom("[]");
        assertArrayEquals(expected, actual);
        
        // Test when input json is an array with null values and custom deserializer
        expected = new String[2];
        actual = deserializer._deserializeCustom("[null, null]");
        assertArrayEquals(expected, actual);
    }
}