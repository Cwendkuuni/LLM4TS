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
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPropertyPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testElementPointer() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPropertyAndElementPointer() {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testMatchesProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer otherPointer = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
        assertTrue(pointer.matches());
        assertTrue(pointer.matches(otherPointer));
    }

    @Test
    public void testMatchesElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer otherPointer = new JsonPointer("/foo", null, JsonPointer.EMPTY);
        JsonPointer otherElementPointer = new JsonPointer("/foo", null, new JsonPointer(input.substring(input.length() - 1), "", JsonPointer.EMPTY));
        assertTrue(pointer.matchesElement(0));
        assertTrue(pointer.matchesElement(otherPointer));
        assertTrue(pointer.matchesElement(otherElementPointer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("~");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithSlash() {
        JsonPointer.compile("/~");
    }

    @Test
    public void testTail() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tailPointer = pointer.tail();
        assertEquals("/bar/baz", tailPointer.toString());
        assertFalse(tailPointer.matches());
    }

    @Test
    public void testToString() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
    }
}