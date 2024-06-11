package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.matches());
        assertEquals("/foo", pointer.toString());
        assertNull(pointer.tail());
    }

    @Test
    public void testMultiSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertTrue(pointer.matches());
        assertEquals("/foo/bar", pointer.toString());
        JsonPointer tail = pointer.tail();
        assertEquals("/bar", tail.toString());
        assertFalse(tail.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("invalid:/foo");
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer match = new JsonPointer("/foo/bar");
        JsonPointer tail = pointer.matchProperty("bar");
        assertEquals(match, tail);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer match = new JsonPointer("/foo/0");
        JsonPointer tail = pointer.matchElement(0);
        assertEquals(match, tail);
    }

    @Test
    public void testNoMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer.matchProperty("baz");
        assertNull(tail);
    }

    @Test
    public void testNoMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer tail = pointer.matchElement(1);
        assertNull(tail);
    }
}