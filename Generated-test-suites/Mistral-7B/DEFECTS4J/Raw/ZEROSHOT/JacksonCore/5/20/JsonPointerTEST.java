package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testBasicPointerConstruction() {
        String jsonPointerString = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertFalse(jsonPointer.matches());
        assertEquals("/foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar/baz", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerString() {
        String jsonPointerString = "~/invalid";
        JsonPointer.compile(jsonPointerString);
    }

    @Test
    public void testMismatchedProperty() {
        String jsonPointerString = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertFalse(jsonPointer.matches());
        assertEquals("/foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar", tail.toString());
        assertNull(jsonPointer.matchProperty("qux"));
    }

    @Test
    public void testMismatchedIndex() {
        String jsonPointerString = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertFalse(jsonPointer.matches());
        assertEquals("/foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("", tail.toString());
        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testMatchedProperty() {
        String jsonPointerString = "/foo/0/property";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertFalse(jsonPointer.matches());
        assertEquals("/foo/0", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("property", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertEquals("/property", tail.toString());
        JsonPointer matchedProperty = jsonPointer.matchProperty("property");
        assertEquals("/foo/0/property", matchedProperty.toString());
    }

    @Test
    public void testMatchedIndex() {
        String jsonPointerString = "/foo/1";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertFalse(jsonPointer.matches());
        assertEquals("/foo", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("", tail.toString());
        assertNull(jsonPointer.matchElement(0));
        JsonPointer matchedIndex = jsonPointer.matchElement(1);
        assertEquals("/foo/1", matchedIndex.toString());
    }

    @Test
    public void testEmptyStringPointer() {
        String jsonPointerString = "";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testTailOfEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testToString() {
        String jsonPointerString = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertEquals(jsonPointerString, jsonPointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String jsonPointerString1 = "/foo/bar/baz";
        String jsonPointerString2 = "/foo/bar/baz";
        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
        assertEquals(jsonPointer1, jsonPointer2);
        JsonPointer jsonPointer3 = new JsonPointer();
        assertNotEquals(jsonPointer1, jsonPointer3);
    }
}