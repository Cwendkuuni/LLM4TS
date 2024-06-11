package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String fullString = "/foo";
        String segment = "foo";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String fullString = "/foo/bar/baz";
        String segment1 = "foo";
        String segment2 = "bar";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment1, new JsonPointer(fullString, segment2, JsonPointer.EMPTY));
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/bar/baz", jsonPointer.toString());
        assertEquals("/bar", jsonPointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String input = "invalid";
        JsonPointer.compile(input);
    }

    @Test
    public void testJsonPointerWithIndex() {
        String fullString = "/foo/0";
        String segment = "foo";
        int index = 0;
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithPropertyNameAndIndex() {
        String fullString = "/foo/0/bar/1";
        String segment1 = "foo";
        String segment2 = "bar";
        int index1 = 0;
        int index2 = 1;
        JsonPointer jsonPointer = new JsonPointer(fullString, segment1, new JsonPointer(fullString, segment2, new JsonPointer(fullString, "", JsonPointer.EMPTY)));
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertEquals("/bar/1", jsonPointer.tail().toString());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithQuotedStringSegment() {
        String fullString = "/foo/\"bar\"";
        String segment = "\"bar\"";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithEscapedSegments() {
        String fullString = "/foo/~1~bar/~0~baz";
        String segment1 = "foo";
        String segment2 = "bar";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment1, new JsonPointer(fullString, segment2, JsonPointer.EMPTY));
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/bar/baz", jsonPointer.toString());
    }
}