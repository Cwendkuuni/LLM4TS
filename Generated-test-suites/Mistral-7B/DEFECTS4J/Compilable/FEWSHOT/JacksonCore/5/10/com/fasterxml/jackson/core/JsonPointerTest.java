package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertNull(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("/", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        JsonPointer jsonPointer = new JsonPointer("/foo", "foo", JsonPointer.EMPTY);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/foo", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "bar", new JsonPointer("/foo", "foo", JsonPointer.EMPTY));
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(new JsonPointer("/foo", "foo", JsonPointer.EMPTY), jsonPointer.tail());
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(new JsonPointer("/foo", "foo", JsonPointer.EMPTY), jsonPointer.tail());
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerCompileInvalidInput() {
        JsonPointer.compile("invalidInput");
    }

    @Test
    public void testJsonPointerMatches() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
        JsonPointer matchingJsonPointer = new JsonPointer("/foo/bar/baz", "baz", JsonPointer.EMPTY);
        JsonPointer nonMatchingJsonPointer = new JsonPointer("/foo/qux", "qux", JsonPointer.EMPTY);

        assertTrue(jsonPointer.matches());
        assertFalse(matchingJsonPointer.matches());
        assertFalse(nonMatchingJsonPointer.matches());
    }

    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
        JsonPointer matchingJsonPointer = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
        JsonPointer nonMatchingJsonPointer = new JsonPointer("/foo/qux", "bar", JsonPointer.EMPTY);

        assertEquals(jsonPointer, jsonPointer.matchProperty("bar"));
        assertEquals(jsonPointer, matchingJsonPointer.matchProperty("bar"));
        assertNull(nonMatchingJsonPointer.matchProperty("bar"));
    }

    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("/foo/0", "0", JsonPointer.EMPTY);
        JsonPointer matchingJsonPointer = new JsonPointer("/foo/0", "0", JsonPointer.EMPTY);
        JsonPointer nonMatchingJsonPointer = new JsonPointer("/foo/1", "0", JsonPointer.EMPTY);

        assertEquals(jsonPointer, jsonPointer.matchElement(0));
        assertEquals(jsonPointer, matchingJsonPointer.matchElement(0));
        assertNull(nonMatchingJsonPointer.matchElement(0));
    }
}