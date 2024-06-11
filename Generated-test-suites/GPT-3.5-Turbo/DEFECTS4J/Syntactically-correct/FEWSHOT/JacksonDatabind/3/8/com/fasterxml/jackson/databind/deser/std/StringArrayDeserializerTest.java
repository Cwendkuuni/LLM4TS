package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        // Test case: empty array
        String[] result = deserializer.deserialize(null, null);
        assertNotNull(result);
        assertEquals(0, result.length);

        // Test case: array with one element
        result = deserializer.deserialize(null, null);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);

        // Test case: array with multiple elements
        result = deserializer.deserialize(null, null);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        // Test case: deserialize with type
        Object result = deserializer.deserializeWithType(null, null, null);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        // Test case: handle non-array when single value is not accepted
        try {
            deserializer.handleNonArray(null, null);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        // Test case: create contextual with null deserializer
        JsonDeserializer<?> result = deserializer.createContextual(null, null);
        assertSame(deserializer, result);

        // Test case: create contextual with different deserializer
        result = deserializer.createContextual(null, null);
        assertNotSame(deserializer, result);
    }
}