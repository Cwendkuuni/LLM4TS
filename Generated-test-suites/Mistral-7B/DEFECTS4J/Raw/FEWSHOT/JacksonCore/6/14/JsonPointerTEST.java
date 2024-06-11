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

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputForCompile() {
        String input = "bar";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchingProperty() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test
    public void testMatchingElement() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo/0", jsonPointer.toString());
    }

    @Test
    public void testMatchingPropertyAndElement() {
        String input = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo/0/bar", jsonPointer.toString());
    }

    @Test
    public void testTail() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertNull(jsonPointer.tail());
        input = "/foo/0/bar";
        jsonPointer = JsonPointer.compile(input);
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
    }

    @Test
    public void testToString() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test
    public void testMatches() {
        String input = "/foo/bar";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        JsonPointer jsonPointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer jsonPointer3 = JsonPointer.compile("/foo");
        JsonPointer jsonPointer4 = JsonPointer.compile("/baz");
        JsonPointer jsonPointer5 = JsonPointer.compile("/foo/quux");
        assertTrue(jsonPointer1.matches());
        assertTrue(jsonPointer2.matches());
        assertFalse(jsonPointer3.matches());
        assertFalse(jsonPointer4.matches());
        assertFalse(jsonPointer5.matches());
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertNull(jsonPointer.matchProperty(null));
        assertNull(jsonPointer.matchProperty("quux"));
        assertEquals(jsonPointer, jsonPointer.matchProperty("bar"));
        assertEquals(jsonPointer, jsonPointer.matchProperty("foo/bar"));
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertNull(jsonPointer.matchElement(-1));
        assertNull(jsonPointer.matchElement(1));
        assertEquals(jsonPointer, jsonPointer.matchElement(0));
    }

    @Test
    public void testParseIndex() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(-1, JsonPointer._parseIndex(""));
        assertEquals(-1, JsonPointer._parseIndex("10"));
    }

    @Test
    public void testParseTail() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer._parseTail(input);
        assertEquals(JsonPointer.EMPTY, jsonPointer);

        input = "/foo/bar/baz";
        jsonPointer = JsonPointer._parseTail(input);
        assertEquals(new JsonPointer("/foo/bar", "baz", JsonPointer.EMPTY), jsonPointer);

        input = "/foo/bar/~1";
        jsonPointer = JsonPointer._parseTail(input);
        assertEquals(new JsonPointer("/foo/bar", "", new JsonPointer("/", "", JsonPointer.EMPTY)), jsonPointer);

        input = "/foo/bar/~0/baz";
        jsonPointer = JsonPointer._parseTail(input);
        assertEquals(new JsonPointer("/foo/bar", "", new JsonPointer("/foo/0/baz", "", JsonPointer.EMPTY)), jsonPointer);
    }
}