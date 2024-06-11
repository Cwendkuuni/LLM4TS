package com.fasterxml.jackson.core.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;

        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testValidJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());

        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertFalse(tail.matches());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer.compile("foo/bar");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        JsonPointer match = pointer.matchProperty("foo");
        assertNotNull(match);
        assertFalse(match.matches());
        assertEquals("bar", match.getMatchingProperty());

        JsonPointer noMatch = pointer.matchProperty("baz");
        assertNull(noMatch);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");

        JsonPointer match = pointer.matchElement(0);
        assertNotNull(match);
        assertFalse(match.matches());
        assertEquals(0, match.getMatchingIndex());

        JsonPointer noMatch = pointer.matchElement(1);
        assertNull(noMatch);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");

        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");

        assertTrue(pointer1.equals(pointer2));
    }
}