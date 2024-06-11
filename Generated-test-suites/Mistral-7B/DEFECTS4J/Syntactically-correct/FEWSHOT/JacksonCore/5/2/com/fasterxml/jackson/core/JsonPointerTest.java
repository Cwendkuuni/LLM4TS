package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("bar", pointer.getMatchingProperty());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertEquals("/foo/0", pointer.toString());
        assertFalse(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithMultipleElements() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/1");
        assertEquals("/foo/0/1", pointer.toString());
        assertFalse(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/foo/0", tail.toString());
        assertFalse(tail.matches());
        assertFalse(tail.mayMatchProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals(0, tail.getMatchingIndex());
        assertNull(tail.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~foo");
    }
}