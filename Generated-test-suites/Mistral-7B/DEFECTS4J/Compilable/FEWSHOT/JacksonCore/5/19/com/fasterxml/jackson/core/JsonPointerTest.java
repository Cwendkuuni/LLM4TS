package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyPointer() {
        JsonPointer ptr = JsonPointer.EMPTY;
        assertTrue(ptr.matches());
        assertNull(ptr.getMatchingProperty());
        assertFalse(ptr.mayMatchProperty());
        assertFalse(ptr.mayMatchElement());
        assertNull(ptr.tail());
        assertEquals("", ptr.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer ptr = JsonPointer.compile("/foo");
        assertTrue(ptr.matches());
        assertEquals("foo", ptr.getMatchingProperty());
        assertTrue(ptr.mayMatchProperty());
        assertFalse(ptr.mayMatchElement());
        assertNull(ptr.tail());
        assertEquals("/foo", ptr.toString());
    }

    @Test
    public void testPropertyPointer() {
        JsonPointer ptr = JsonPointer.compile("/foo/bar");
        assertFalse(ptr.matches());
        assertEquals("bar", ptr.getMatchingProperty());
        assertTrue(ptr.mayMatchProperty());
        assertFalse(ptr.mayMatchElement());
        assertEquals("/foo", ptr.tail().getMatchingProperty());
        assertEquals("/foo/bar", ptr.toString());
    }

    @Test
    public void testElementPointer() {
        JsonPointer ptr = JsonPointer.compile("/foo/0");
        assertFalse(ptr.matches());
        assertNull(ptr.getMatchingProperty());
        assertFalse(ptr.mayMatchProperty());
        assertTrue(ptr.mayMatchElement());
        assertEquals("/foo", ptr.tail().getMatchingProperty());
        assertEquals("/foo/0", ptr.toString());
    }

    @Test
    public void testComplexPointer() {
        JsonPointer ptr = JsonPointer.compile("/foo/bar/1/baz/2");
        assertFalse(ptr.matches());
        assertEquals("baz", ptr.getMatchingProperty());
        assertFalse(ptr.mayMatchProperty());
        assertTrue(ptr.mayMatchElement());
        JsonPointer tail = ptr.tail();
        assertEquals("bar/1", tail.getMatchingProperty());
        assertEquals("/foo/bar/1/baz", ptr.tail().toString());
        assertEquals("/foo/bar/1/baz/2", ptr.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("invalid");
    }
}