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
        assertEquals(empty, empty.tail());
        assertTrue(empty.matches());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
    }

    @Test
    public void testBasicPointerConstruction() {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertEquals("/foo", pointer.tail().toString());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/bar", pointer.matchProperty("bar").toString());
        assertEquals(null, pointer.matchProperty("baz"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertEquals("/foo", pointer.matchElement(0).toString());
        assertEquals(null, pointer.matchElement(-1));
    }

    @Test
    public void testEquals() {
        String input1 = "/foo/bar/1";
        String input2 = "/foo/bar/1";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testNotEquals() {
        String input1 = "/foo/bar/1";
        String input2 = "/foo/bar/2";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertNotEquals(pointer1, pointer2);
    }

    // Add more test cases as needed, for example testing other factory methods, methods for handling special characters in JSON pointers, etc.

    @Test
    public void testFactoryFromSegment() {
        String[] segments = {"/foo", "/bar", "/1"};
        JsonPointer pointer = JsonPointer.fromSegment(segments);
        assertEquals("/foo/bar/1", pointer.toString());
        assertEquals("/foo/bar", pointer.tail().toString());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
    }
}