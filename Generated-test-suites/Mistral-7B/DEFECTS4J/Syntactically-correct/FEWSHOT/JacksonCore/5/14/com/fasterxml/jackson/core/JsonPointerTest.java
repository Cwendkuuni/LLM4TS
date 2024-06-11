package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("/", empty.toString());
        assertNull(empty.tail());
        assertTrue(empty.matches());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertNull(pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchProperty(""));
        assertNull(pointer.matchElement(-1));
        assertEquals(pointer, pointer.matchProperty("foo"));
    }

    @Test
    public void testPointerWithTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        assertEquals(JsonPointer.compile("/bar"), pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(-1));
        assertEquals(JsonPointer.compile("/bar"), pointer.matchProperty("bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~foo");
    }

    @Test
    public void testPointerWithEscapedCharacter() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar~1");
        assertEquals("/foo/bar/", pointer.toString());
        assertEquals(JsonPointer.compile("/bar"), pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(-1));
        assertEquals(JsonPointer.compile("/bar"), pointer.matchProperty("bar"));
    }

    @Test
    public void testPointerWithMultipleEscapedCharacters() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar~1/baz~0");
        assertEquals("/foo/bar/baz", pointer.toString());
        assertEquals(JsonPointer.compile("/baz"), pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchElement(-1));
        assertEquals(JsonPointer.compile("/baz"), pointer.matchProperty("baz"));
    }
}