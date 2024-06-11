package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/baz", tail.toString());
        assertTrue(tail.matches());
        assertEquals("baz", tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
    }

    @Test
    public void testJsonPointerWithIndex() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidInput() {
        String input = "~foo";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithEmptyInput() {
        String input = "";
        JsonPointer.compile(input);
    }
}