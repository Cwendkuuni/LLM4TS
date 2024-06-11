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
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo");
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultiSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("/bar", jsonPointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("~foo");
    }

    @Test
    public void testJsonPointerWithIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo/0", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleIndices() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0/1");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo/1", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer matchingPropertyPointer = JsonPointer.compile("/foo/bar");
        assertEquals(jsonPointer, matchingPropertyPointer);
        JsonPointer notMatchingPropertyPointer = JsonPointer.compile("/foo/baz");
        assertNotEquals(jsonPointer, notMatchingPropertyPointer);
    }

    @Test
    public void testJsonPointerWithMatchingElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        JsonPointer matchingElementPointer = JsonPointer.compile("/foo/0");
        assertEquals(jsonPointer, matchingElementPointer);
        JsonPointer notMatchingElementPointer = JsonPointer.compile("/foo/1");
        assertNotEquals(jsonPointer, notMatchingElementPointer);
    }

    @Test
    public void testJsonPointerWithTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/bar/baz", jsonPointer.tail().toString());
        JsonPointer noTailPointer = JsonPointer.EMPTY;
        assertNull(noTailPointer.tail());
    }
}