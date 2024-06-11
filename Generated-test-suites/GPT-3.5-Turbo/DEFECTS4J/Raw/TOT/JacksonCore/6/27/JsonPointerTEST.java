package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testCompileValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchElement());
        assertTrue(pointer.mayMatchProperty());
        assertNull(pointer.matchProperty("baz"));
        assertNull(pointer.matchElement(1));

        JsonPointer next = pointer.tail();
        assertFalse(next.matches());
        assertEquals("bar", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertFalse(next.mayMatchElement());
        assertTrue(next.mayMatchProperty());
        assertNull(next.matchProperty("baz"));
        assertNull(next.matchElement(1));

        JsonPointer last = next.tail();
        assertTrue(last.matches());
        assertEquals("", last.getMatchingProperty());
        assertEquals(-1, last.getMatchingIndex());
        assertFalse(last.mayMatchElement());
        assertFalse(last.mayMatchProperty());
        assertNull(last.matchProperty("baz"));
        assertNull(last.matchElement(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("foo/bar");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer match = pointer.matchProperty("foo");
        assertNotNull(match);
        assertEquals(pointer.tail(), match);
        assertNull(match.matchProperty("baz"));
        assertNull(match.matchElement(1));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer match = pointer.matchElement(1);
        assertNull(match);
        match = pointer.matchElement(-1);
        assertNull(match);

    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        JsonPointer empty1 = JsonPointer.EMPTY;
        JsonPointer empty2 = JsonPointer.EMPTY;
        assertEquals(empty1.hashCode(), empty2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1, pointer2);
        assertNotEquals(pointer1, null);
        assertNotEquals(pointer1, new Object());
        JsonPointer empty1 = JsonPointer.EMPTY;
        JsonPointer empty2 = JsonPointer.EMPTY;
        assertEquals(empty1, empty2);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("/bar", tail.toString());
        JsonPointer emptyTail = tail.tail();
        assertNotNull(emptyTail);
        assertTrue(emptyTail.matches());
    }

}