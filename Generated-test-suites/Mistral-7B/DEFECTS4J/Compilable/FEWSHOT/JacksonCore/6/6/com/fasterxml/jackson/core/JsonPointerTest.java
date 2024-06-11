package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertTrue(emptyJsonPointer.matches());
        assertEquals("", emptyJsonPointer.getMatchingProperty());
        assertEquals(-1, emptyJsonPointer.getMatchingIndex());
        assertNull(emptyJsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithMultipleSegmentsAndProperties() {
        String input = "/foo/bar/baz/qux";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/baz/qux", tail.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegmentsAndElements() {
        String input = "/foo/0/bar/1/baz/2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/baz/2", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer.compile("invalidJsonPointer");
    }

    @Test
    public void testJsonPointerWithTildeEscapes() {
        String input = "/foo/~1/bar/~0/baz";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/~1/baz", tail.toString());
    }
}