package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.TestUtils;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() throws JsonProcessingException {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer);
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty(""));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(-1));
        assertTrue(pointer.matches());
    }

    @Test
    public void testSimplePointer() throws JsonProcessingException {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertEquals("/foo", pointer.toString());
        assertNull(pointer.tail());
        assertNull(pointer.matchProperty(""));
        assertNull(pointer.matchElement(-1));
        assertFalse(pointer.matches());
        assertEquals("/foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(pointer, pointer.tail());
        assertSame(JsonPointer.EMPTY, pointer.matchProperty(""));
        assertSame(JsonPointer.EMPTY, pointer.matchElement(-1));
    }

    @Test
    public void testSimplePointerWithMatchingElement() throws JsonProcessingException {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertNull(pointer.tail());
        assertNull(pointer.matchProperty(""));
        assertEquals(0, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.matches());
        assertEquals("/foo", pointer.getMatchingProperty());
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty(""));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(-1));
        assertEquals(pointer, pointer.tail());
    }

    @Test
    public void testComplexPointer() throws JsonProcessingException, IOException {
        String input = "/foo/bar/baz[1]/quux";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertNotNull(pointer.tail());
        assertEquals("bar/baz[1]/quux", pointer.tail().toString());
        assertNull(pointer.matchProperty(""));
        assertNull(pointer.matchElement(-1));
        assertFalse(pointer.matches());
        assertEquals("/foo/bar/baz[1]/quux", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertEquals("/foo/bar/baz", pointer.matchProperty("bar"));
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty(""));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(-1));
        assertEquals(pointer, pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() throws JsonProcessingException {
        String input = "~foo";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithTrailingSlash() throws JsonProcessingException {
        String input = "/foo/";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithEmptySegment() throws JsonProcessingException {
        String input = "/foo/";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithTwoSlashes() throws JsonProcessingException {
        String input = "/foo//bar";
        JsonPointer.compile(input);
    }
}