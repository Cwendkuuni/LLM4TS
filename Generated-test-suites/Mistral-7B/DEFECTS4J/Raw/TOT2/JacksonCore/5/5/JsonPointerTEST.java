package com.fasterxml.jackson.core; // imported classes are assumed

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.toString());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
    }

    @Test
    public void testValidJsonPointer() {
        String input = "/test/property";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/test/property", jsonPointer.toString());
        assertEquals(null, jsonPointer.tail());
        assertFalse(jsonPointer.matches());
        assertEquals("property", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/test", tail.toString());
        assertEquals(JsonPointer.EMPTY, tail.tail());
        assertFalse(tail.matches());
        assertEquals("property", tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String input = "test";
        JsonPointer.compile(input); // should throw an IllegalArgumentException
    }

    @Test
    public void testValidJsonPointerWithElement() {
        String input = "/test/property/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/test/property/0", jsonPointer.toString());
        assertEquals(null, jsonPointer.tail());
        assertFalse(jsonPointer.matches());
        assertEquals("property", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/test/property", tail.toString());
        assertEquals(JsonPointer.EMPTY, tail.tail());
        assertFalse(tail.matches());
        assertEquals("property", tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.getMatchingIndex());
    }

    // Add more test cases for other methods and edge cases
}