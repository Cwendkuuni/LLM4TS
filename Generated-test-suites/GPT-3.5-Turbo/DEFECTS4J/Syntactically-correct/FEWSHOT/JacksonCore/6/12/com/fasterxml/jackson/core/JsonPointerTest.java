package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/");
        assertEquals(true, pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals(true, pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(true, pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = pointer1.matchProperty("foo");
        assertEquals(true, pointer1 != pointer2);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = JsonPointer.compile("/0");
        JsonPointer pointer2 = pointer1.matchElement(0);
        assertEquals(true, pointer1 != pointer2);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/bar", pointer.tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(true, pointer1.equals(pointer2));
    }
}