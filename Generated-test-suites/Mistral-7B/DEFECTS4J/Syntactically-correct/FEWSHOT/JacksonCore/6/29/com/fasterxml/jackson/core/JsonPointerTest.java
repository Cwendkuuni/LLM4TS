package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
        assertEquals(0, empty.hashCode());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
        assertEquals(1, pointer.hashCode());
    }

    @Test
    public void testNestedPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals("/foo/bar", pointer.toString());
        assertEquals(2, pointer.hashCode());
    }

    @Test
    public void testPointerWithIndex() {
        JsonPointer pointer = JsonPointer.compile("/foo/1");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals("/foo/1", pointer.toString());
        assertEquals(3, pointer.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~invalid");
    }

    @Test
    public void testPointerWithQuotes() {
        JsonPointer pointer = JsonPointer.compile("/foo/\"bar\"");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals("/foo/\"bar\"", pointer.toString());
        assertEquals(5, pointer.hashCode());
    }

    @Test
    public void testPointerWithMultipleQuotes() {
        JsonPointer pointer = JsonPointer.compile("/foo/\"bar\"/\"quux\"");
        assertFalse(pointer.matches());
        assertEquals("quux", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals("/foo/\"bar\"/\"quux\"", pointer.toString());
        assertEquals(7, pointer.hashCode());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer match = new JsonPointer("/foo/bar");
        assertEquals(match, pointer.matchProperty("bar"));
        assertNull(pointer.matchProperty("baz"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/1");
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(0));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(-1));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(2));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/1/bar");
        assertEquals(JsonPointer.compile("/foo/1/bar"), pointer.tail());
        assertEquals(JsonPointer.compile("/foo/1"), pointer.tail().tail());
        assertEquals(JsonPointer.compile("/foo"), pointer.tail().tail().tail());
        assertEquals(JsonPointer.EMPTY, pointer.tail().tail().tail().tail());
    }
}