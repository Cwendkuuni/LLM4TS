package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testSimplePointerWithTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertFalse(tail.matches());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testPointerWithIndex() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo/0", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~/foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithSlash() {
        JsonPointer.compile("/~/foo");
    }

    @Test
    public void testPointerWithTailContainingEmptySegment() {
        JsonPointer pointer = JsonPointer.compile("/foo/");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertTrue(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
        assertEquals("/foo/", pointer.toString());
    }
}