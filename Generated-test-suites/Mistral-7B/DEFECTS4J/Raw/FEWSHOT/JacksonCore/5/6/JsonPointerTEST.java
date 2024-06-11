package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertTrue(emptyJsonPointer.matches());
        assertNull(emptyJsonPointer.getMatchingProperty());
        assertNegative(emptyJsonPointer.getMatchingIndex());
        assertFalse(emptyJsonPointer.mayMatchProperty());
        assertFalse(emptyJsonPointer.mayMatchElement());
        assertNull(emptyJsonPointer.tail());
        assertEquals("", emptyJsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertNegative(jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertNegative(jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("baz", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertEquals("baz", tail.getMatchingProperty());
        assertNegative(tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
        assertEquals("/foo/bar/baz", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithIndex() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals(null, jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo/0", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String input = "foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
    }

    @Test
    public void testJsonPointerWithEscapes() {
        String input = "/foo/bar/baz~1/quux~0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("baz", jsonPointer.getMatchingProperty());
        assertNegative(jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("quux", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertEquals("quux", tail.getMatchingProperty());
        assertNegative(tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
        assertEquals("/foo/bar/baz~1/quux", jsonPointer.toString());
    }
}