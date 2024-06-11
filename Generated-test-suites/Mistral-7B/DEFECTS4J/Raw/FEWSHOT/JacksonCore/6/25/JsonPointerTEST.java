package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    /**
     * Test for constructor used for creating "empty" instance, used to represent
     * state that matches current node.
     */
    @Test
    public void testJsonPointerEmpty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("", jsonPointer.toString());
    }

    /**
     * Test for constructor used for creating non-empty Segments.
     */
    @Test
    public void testJsonPointerNonEmpty() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer("/baz");

        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);

        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(fullString, jsonPointer.toString());

        assertEquals("/bar", jsonPointer.tail().toString());
    }

    /**
     * Test for factory method that parses given input and constructs matching pointer
     * instance, if it represents a valid JSON Pointer: if not, a
     * {@link IllegalArgumentException} is thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerInvalid() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    /**
     * Test for factory method that compose a pointer instance, given a set
     * of 'raw' segments: raw meaning that no processing will be done,
     * no escaping may is present.
     */
    @Test
    public void testJsonPointerFromSegment() {
        String[] segments = {"foo", "bar"};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    /**
     * Test for public API methods
     */
    @Test
    public void testJsonPointerPublicApi() {
        String fullString = "/foo/bar/1";
        String segment = "foo";
        JsonPointer next = new JsonPointer("/baz");

        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);

        assertEquals(true, jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertEquals(true, jsonPointer.mayMatchProperty());
        assertEquals(true, jsonPointer.mayMatchElement());
        assertEquals("/bar/1", jsonPointer.tail().toString());
    }
}