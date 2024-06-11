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
    }

    @Test
    public void testBasicPointer() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testIndexedPointer() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testNestedPointer() {
        String input = "/foo/0/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/bar/baz", pointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithSlashAtStart() {
        JsonPointer.compile("/invalid");
    }

    @Test
    public void testMatchesProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer propertyPointer = new JsonPointer("/foo/bar", "bar", null);
        assertTrue(pointer.matches());
        assertTrue(propertyPointer.matches());
        JsonPointer result = pointer.matchProperty("bar");
        assertEquals(propertyPointer, result);
    }

    @Test
    public void testMatchesElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer elementPointer = new JsonPointer("/foo/0", null, null);
        assertTrue(pointer.matches());
        assertTrue(elementPointer.matches());
        JsonPointer result = pointer.matchElement(0);
        assertEquals(elementPointer, result);
    }

    @Test
    public void testNullPointer() {
        String input = null;
        try {
            JsonPointer.compile(input);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}