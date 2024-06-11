package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("/", empty.toString());
        assertEquals(empty, empty.tail());
        assertTrue(empty.matches());
    }

    @Test
    public void testSimplePointer() {
        String input = "/a/b";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals("a", pointer.matchProperty("/a").toString());
        assertEquals("b", pointer.matchProperty("/a/b").toString());
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchElement(1));
        assertEquals("b", pointer.matchElement(2).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "~a/b";
        JsonPointer.compile(input);
    }

    @Test
    public void testComplexPointer() {
        String input = "/a/0/c/d/e/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertEquals("c", pointer.matchProperty("/a/c").toString());
        assertEquals("d", pointer.matchProperty("/a/c/d").toString());
        assertEquals("e", pointer.matchProperty("/a/c/d/e").toString());
        assertEquals(1, pointer.matchElement(3).getMatchingIndex());
        assertEquals(null, pointer.matchElement(-1));
        assertEquals(null, pointer.matchElement(0));
        assertEquals(null, pointer.matchElement(1));
        assertEquals(null, pointer.matchElement(2));
    }

    @Test
    public void testComplexPointerTail() {
        String input = "/a/0/c/d/e/1";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/c/d/e", tail.toString());
        assertNotNull(tail.tail());
        assertFalse(tail.matches());
        assertEquals("d", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertEquals("e", tail.matchProperty("/c/d/e").toString());
        assertNull(tail.matchElement(-1));
        assertNull(tail.matchElement(0));
        assertEquals(1, tail.matchElement(1).getMatchingIndex());
    }
}