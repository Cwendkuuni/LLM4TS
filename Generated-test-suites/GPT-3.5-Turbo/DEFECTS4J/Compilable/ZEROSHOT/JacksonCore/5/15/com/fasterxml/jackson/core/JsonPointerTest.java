package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;

        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyJsonPointer() {
        JsonPointer pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);

        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("other"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());

        JsonPointer tail = pointer.tail();
        assertTrue(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(0));
        assertNull(tail.tail());
    }

    @Test
    public void testJsonPointerWithIndex() {
        JsonPointer pointer = new JsonPointer("segment", "10", JsonPointer.EMPTY);

        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(10, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(9));
        assertNull(pointer.matchElement(11));
        assertNull(pointer.tail());

        JsonPointer tail = pointer.tail();
        assertTrue(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(0));
        assertNull(tail.tail());
    }

    @Test
    public void testJsonPointerWithEscapeCharacter() {
        JsonPointer pointer = new JsonPointer("segment", "~1", JsonPointer.EMPTY);

        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());

        JsonPointer tail = pointer.tail();
        assertTrue(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(0));
        assertNull(tail.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer.compile("test");
    }

    @Test
    public void testValidJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/test");

        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("other"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());

        JsonPointer tail = pointer.tail();
        assertTrue(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(0));
        assertNull(tail.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/test");

        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/test");
        JsonPointer pointer2 = JsonPointer.compile("/test");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/test");
        JsonPointer pointer2 = JsonPointer.compile("/test");
        JsonPointer pointer3 = JsonPointer.compile("/other");

        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }
}