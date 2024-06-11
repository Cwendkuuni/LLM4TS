package com.fasterxml.jackson.core;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setup() {
        jsonPointer = JsonPointer.EMPTY;
    }

    @Test
    public void testMatches() {
        assertTrue(jsonPointer.matches());
        jsonPointer = JsonPointer.compile("/");
        assertTrue(jsonPointer.matches());
        jsonPointer = JsonPointer.compile("/foo");
        assertFalse(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        assertEquals("", jsonPointer.getMatchingProperty());
        jsonPointer = JsonPointer.compile("/foo");
        assertEquals("foo", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        assertEquals(-1, jsonPointer.getMatchingIndex());
        jsonPointer = JsonPointer.compile("/[0]");
        assertEquals(0, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = JsonPointer.compile("/foo");
        assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = JsonPointer.compile("/[0]");
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        assertFalse(jsonPointer.mayMatchElement());
        jsonPointer = JsonPointer.compile("/foo");
        assertFalse(jsonPointer.mayMatchElement());
        jsonPointer = JsonPointer.compile("/[0]");
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        jsonPointer = JsonPointer.compile("/foo");
        assertNull(jsonPointer.matchProperty("bar"));
        assertEquals(jsonPointer, jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchProperty("Foo"));
    }

    @Test
    public void testMatchElement() {
        jsonPointer = JsonPointer.compile("/[0]");
        assertNull(jsonPointer.matchElement(1));
        assertEquals(jsonPointer, jsonPointer.matchElement(0));
        assertNull(jsonPointer.matchElement(-1));
    }

    @Test
    public void testTail() {
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        jsonPointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
    }

    @Test
    public void testToString() {
        assertEquals("", jsonPointer.toString());
        jsonPointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, jsonPointer.hashCode());
        jsonPointer = JsonPointer.compile("/foo");
        assertEquals(111324069, jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals("foo"));
        jsonPointer = JsonPointer.compile("/foo");
        assertTrue(jsonPointer.equals(JsonPointer.compile("/foo")));
        assertFalse(jsonPointer.equals(JsonPointer.compile("/bar")));
    }

}