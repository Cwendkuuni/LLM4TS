package com.fasterxml.jackson.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());

        pointer = new JsonPointer("/foo");
        assertFalse(pointer.matches());

        pointer = new JsonPointer("/foo/bar");
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.getMatchingProperty());

        pointer = new JsonPointer("/foo");
        assertEquals("foo", pointer.getMatchingProperty());

        pointer = new JsonPointer("/foo/bar");
        assertEquals("foo", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("/foo");
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("/foo/bar");
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("/bar/0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchProperty());

        pointer = new JsonPointer("/foo");
        assertTrue(pointer.mayMatchProperty());

        pointer = new JsonPointer("/foo/bar");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("/foo");
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("/foo/bar");
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("/bar/0");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo");
        assertNull(pointer.matchProperty("bar"));

        pointer = new JsonPointer("/foo/bar");
        assertNull(pointer.matchProperty("foo"));

        pointer = new JsonPointer("/foo/bar");
        JsonPointer next = pointer.matchProperty("bar");
        assertNotNull(next);
        assertEquals("bar", next.getMatchingProperty());
        assertTrue(next.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/bar/0");
        assertNull(pointer.matchElement(1));

        pointer = new JsonPointer("/bar/0");
        JsonPointer next = pointer.matchElement(0);
        assertNotNull(next);
        assertEquals(0, next.getMatchingIndex());
        assertTrue(next.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.tail());

        pointer = new JsonPointer("/foo");
        assertNull(pointer.tail());

        pointer = new JsonPointer("/foo/bar");
        assertNotNull(pointer.tail());
        assertEquals("/bar", pointer.tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());

        pointer = new JsonPointer("/foo");
        assertEquals("/foo", pointer.toString());

        pointer = new JsonPointer("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = new JsonPointer("/foo");
        pointer2 = new JsonPointer("/foo");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = new JsonPointer("/foo/bar");
        pointer2 = new JsonPointer("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("/foo");
        pointer2 = new JsonPointer("/foo");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("/foo/bar");
        pointer2 = new JsonPointer("/foo/bar");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.EMPTY;
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(""));

        pointer1 = JsonPointer.EMPTY;
        assertFalse(pointer1.equals(new JsonPointer("/foo")));

        pointer1 = new JsonPointer("/foo");
        assertFalse(pointer1.equals(new JsonPointer("/bar")));

        pointer1 = new JsonPointer("/foo");
        assertFalse(pointer1.equals(new JsonPointer("/foo/bar")));
    }
}