package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertFalse(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testBasicPointer() throws IOException {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar/1", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());

        assertEquals(pointer, pointer.tail());
        assertEquals(JsonPointer.EMPTY, pointer.tail().tail());

        assertNull(pointer.matchProperty("/qux"));
        assertNull(pointer.matchElement(-1));

        assertEquals(pointer, pointer.matchProperty("bar"));
        assertEquals(pointer, pointer.matchElement(1));
    }

    @Test
    public void testEmptyInput() {
        try {
            JsonPointer.compile("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"\"", e.getMessage());
        }
    }

    @Test
    public void testInvalidInput() {
        try {
            JsonPointer.compile("/qux");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': /qux", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput2() {
        JsonPointer.compile("/qux/");
    }

    @Test
    public void testIndexOutOfBounds() {
        String input = "/foo/bar/10";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar", pointer.tail().toString());
        assertNull(pointer.matchElement(10));
    }

    @Test
    public void testTailOfEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testTailOfNonemptyPointer() {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/bar", pointer.tail().toString());
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("/baz").tail());
        assertNull(pointer.matchProperty("/baz"));
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("bar").tail());
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("bar"));
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(0));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(1).tail());
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(1));
    }
}