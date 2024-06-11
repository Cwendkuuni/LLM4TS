package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithOneSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTwoSegments() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("bar", tail.getMatchingProperty());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithOneIndexedElement() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTwoIndexedElements() {
        String input = "/foo/0/1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("1", tail.getMatchingProperty());
        assertTrue(tail.mayMatchProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals(-1, tail.getMatchingIndex());
        assertNull(tail.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String input = "~foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
    }
}