package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("/", pointer.toString());
        assertEquals(pointer, pointer.tail());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertFalse(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.matches("/"));
        assertFalse(pointer.matches("/foo"));
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertEquals(null, pointer.tail());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.matches("/"));
        assertTrue(pointer.matches("/foo"));
        assertFalse(pointer.matches("/bar"));
        assertFalse(pointer.matches("/foo/bar"));
    }

    @Test
    public void testMultiSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        JsonPointer tail = pointer.tail();
        assertEquals("/bar", tail.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.matches("/"));
        assertTrue(pointer.matches("/foo"));
        assertTrue(pointer.matches("/foo/bar"));
        assertFalse(pointer.matches("/foo/qux"));
        assertFalse(pointer.matches("/quux"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer2() {
        JsonPointer.compile("foo");
    }
}