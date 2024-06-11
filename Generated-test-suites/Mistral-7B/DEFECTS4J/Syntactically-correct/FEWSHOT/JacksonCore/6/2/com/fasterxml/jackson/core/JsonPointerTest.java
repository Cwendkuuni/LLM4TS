package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithSegment() {
        JsonPointer pointer = new JsonPointer("/foo", "foo", JsonPointer.EMPTY);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithPropertyMatch() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithElementMatch() {
        JsonPointer pointer = new JsonPointer("/foo/0", "", new JsonPointer("/foo", "foo", JsonPointer.EMPTY));
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalJsonPointerInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(pointer.tail(), new JsonPointer("/foo/bar/baz", "baz", JsonPointer.EMPTY));
    }
}