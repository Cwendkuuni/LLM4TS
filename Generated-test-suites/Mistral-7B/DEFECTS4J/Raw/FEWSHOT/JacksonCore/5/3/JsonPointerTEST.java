package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
        assertEquals(empty, empty.tail());
        assertTrue(empty.matches());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo", pointer.toString());
        assertEquals(null, pointer.tail());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
    }

    @Test
    public void testPointerWithTail() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar", pointer.toString());
        assertEquals(new JsonPointer("/foo"), pointer.tail());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testPointerWithIndex() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/0", pointer.toString());
        assertEquals(new JsonPointer("/foo"), pointer.tail());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/0/bar", pointer.toString());
        assertEquals(new JsonPointer("/foo/0"), pointer.tail());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "~foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatching() {
        String input = "/foo/0";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = new JsonPointer("/foo", "bar", null);
        JsonPointer pointer3 = new JsonPointer("/foo/0");
        JsonPointer pointer4 = new JsonPointer("/foo", null, pointer3);
        assertEquals(pointer3, pointer1.tail());
        assertEquals(pointer3, pointer2.tail());
        assertEquals(pointer3, pointer3.tail());
        assertEquals(pointer4, pointer1.matchProperty("/foo"));
        assertEquals(pointer3, pointer4.tail());
        assertEquals(pointer3, pointer1.matchElement(0));
        assertEquals(pointer3, pointer1.matchElement(-1));
        assertEquals(pointer3, pointer2.matchProperty("bar").tail());
        assertEquals(pointer3, pointer2.matchElement(0).tail());
    }

    @Test
    public void testMismatching() {
        String input = "/foo/0";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = new JsonPointer("/bar", "baz", null);
        assertNull(pointer1.matchProperty("/bar"));
        assertNull(pointer1.matchElement(-1));
        assertNull(pointer2.matchProperty("/foo"));
        assertNull(pointer2.matchElement(0));
    }
}