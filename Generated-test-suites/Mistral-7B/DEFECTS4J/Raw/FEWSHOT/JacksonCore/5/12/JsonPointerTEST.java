package com.fasterxml.jackson.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithOneSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
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
        assertNull(jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer next = jsonPointer.tail();
        assertNotNull(next);
        assertEquals("bar", next.getMatchingProperty());
        assertNull(next.getMatchingIndex());
        assertFalse(next.mayMatchProperty());
        assertFalse(next.mayMatchElement());
        assertNull(next.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTwoElementSegments() {
        String input = "/foo/0/bar";
        JsonPointer jsonPointer = null;
        try {
            jsonPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(jsonPointer);
        input = "/foo/1/bar";
        jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        JsonPointer next = jsonPointer.tail();
        assertNotNull(next);
        assertEquals("bar", next.getMatchingProperty());
        assertNull(next.getMatchingIndex());
        assertFalse(next.mayMatchProperty());
        assertFalse(next.mayMatchElement());
        assertNull(next.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithNegativeIndexSegment() {
        String input = "/foo/-1/bar";
        JsonPointer jsonPointer = null;
        try {
            jsonPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(jsonPointer);
    }

    @Test
    public void testJsonPointerWithLongSegment() {
        String input = "/foo/0/bar/baz/qux/quux/corge/gaarbularblargh/12345678901234567890";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        JsonPointer next = jsonPointer.tail();
        assertNotNull(next);
        assertEquals("bar", next.getMatchingProperty());
        assertNull(next.getMatchingIndex());
        assertFalse(next.mayMatchProperty());
        assertFalse(next.mayMatchElement());
        JsonPointer nextNext = next.tail();
        assertNotNull(nextNext);
        assertEquals("baz", nextNext.getMatchingProperty());
        assertNull(nextNext.getMatchingIndex());
        assertFalse(nextNext.mayMatchProperty());
        assertFalse(nextNext.mayMatchElement());
        assertNull(nextNext.tail());
        assertEquals(input, jsonPointer.toString());
    }

}