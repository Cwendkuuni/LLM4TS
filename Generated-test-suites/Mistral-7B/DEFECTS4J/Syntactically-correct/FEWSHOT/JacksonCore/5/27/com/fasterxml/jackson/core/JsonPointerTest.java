package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("bar/baz", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String input = "~foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testJsonPointerWithIndex() {
        String input = "/array/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/array/0", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidIndex() {
        String input = "/array/invalid";
        JsonPointer.compile(input);
    }

    @Test
    public void testJsonPointerWithQuotes() {
        String input = "/array/\"0\"";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/array/\"0\"", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleQuotes() {
        String input = "/array/\"0\"/\"1\"";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        JsonPointer tail1 = jsonPointer.tail();
        assertEquals("/\"1\"", tail1.toString());
        JsonPointer tail2 = tail1.tail();
        assertNull(tail2);
        assertEquals("/array/\"0\"/\"1\"", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTilde() {
        String input = "/foo/~1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("~1", tail.toString());
    }

    @Test
    public void testJsonPointerWithTildeAndSegment() {
        String input = "/foo/~1/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail1 = jsonPointer.tail();
        assertEquals("~1", tail1.toString());
        JsonPointer tail2 = tail1.tail();
        assertEquals("bar", tail2.getMatchingProperty());
        assertFalse(tail2.mayMatchElement());
        assertNull(tail2.tail());
        assertEquals("/foo/~1/bar", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTildeAndIndex() {
        String input = "/foo/~1/array/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail1 = jsonPointer.tail();
        assertEquals("~1", tail1.toString());
        JsonPointer tail2 = tail1.tail();
        assertEquals("array/0", tail2.toString());
        assertFalse(tail2.mayMatchElement());
        assertNull(tail2.tail());
        assertEquals("/foo/~1/array/0", jsonPointer.toString());
    }
}