package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/");
        assertTrue(pointer.matches());

        pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());

        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("foo", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/");
        assertEquals("", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(0, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/foo/2");
        assertEquals(2, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/foo/bar");
        assertTrue(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/");
        assertFalse(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertTrue(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/foo/2");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer next = pointer.matchProperty("foo");
        assertNotNull(next);
        assertEquals("", next.toString());

        next = pointer.matchProperty("bar");
        assertNull(next);

        pointer = JsonPointer.compile("/");
        next = pointer.matchProperty("");
        assertNotNull(next);
        assertEquals("", next.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer next = pointer.matchElement(0);
        assertNotNull(next);
        assertEquals("", next.toString());

        next = pointer.matchElement(1);
        assertNull(next);

        pointer = JsonPointer.compile("/");
        next = pointer.matchElement(0);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("/bar", tail.toString());

        pointer = JsonPointer.compile("/");
        tail = pointer.tail();
        assertNull(tail);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());

        pointer = JsonPointer.compile("/");
        assertEquals("/", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = JsonPointer.compile("/");
        pointer2 = JsonPointer.compile("/");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/");
        pointer2 = JsonPointer.compile("/");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/");
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(""));

        pointer1 = JsonPointer.compile("/");
        assertFalse(pointer1.equals(JsonPointer.compile("/foo")));
        assertFalse(pointer1.equals(JsonPointer.compile("/0")));
    }
}