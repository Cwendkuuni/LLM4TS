package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty JsonPointer instance.
     */
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertEquals("", emptyJsonPointer.toString());
        assertEquals(emptyJsonPointer, emptyJsonPointer.tail());
        assertTrue(emptyJsonPointer.matches());
        assertNull(emptyJsonPointer.getMatchingProperty());
        assertNull(emptyJsonPointer.getMatchingIndex());
    }

    /**
     * Test constructor for non-empty JsonPointer instance.
     */
    @Test
    public void testNonEmptyJsonPointer() {
        String input = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
        assertEquals("/bar", jsonPointer.tail().toString());
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());

        assertEquals(jsonPointer, jsonPointer.tail());
        assertNull(jsonPointer.matchProperty("baz"));
        assertNull(jsonPointer.matchElement(-1));
    }

    /**
     * Test IllegalArgumentException when input is not a valid JSON pointer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer.compile("invalid");
    }

    /**
     * Test null pointer input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullJsonPointerInput() {
        JsonPointer.compile(null);
    }

    /**
     * Test empty input.
     */
    @Test
    public void testEmptyJsonPointerInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertEquals("", jsonPointer.toString());
        assertEquals(jsonPointer, jsonPointer.tail());
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
    }
}