package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "bar";
        JsonPointer.compile(input);
    }

    @Test
    public void testMultiSegmentPointer() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/baz", tail.toString());
        assertEquals(2, tail.getMatchingIndex());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testIndexedPointer() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/0", pointer.toString());
    }

    @Test
    public void testNegativeIndexedPointer() {
        String input = "/foo/-1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/-1", pointer.toString());
    }

    @Test
    public void testMultiLevelIndexedPointer() {
        String input = "/foo/1/bar/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertTrue(pointer.mayMatchProperty());
        assertEquals("1", pointer.getMatchingProperty());
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/0", tail.toString());
        assertEquals(0, tail.getMatchingIndex());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals("/foo/1/bar", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithQuotes() {
        String input = "/\"foo\"/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertTrue(pointer.mayMatchProperty());
        assertNull(pointer.tail());
        assertEquals("/\"foo\"", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithQuotesAndIndex() {
        String input = "/\"foo\"/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/\"foo\"/1", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithDoubleQuotes() {
        String input = "/\"foo\"/bar/\"baz\"";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertNull(pointer.tail());
        assertEquals("/\"foo\"/bar", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithTilde() {
        String input = "/foo/~0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/~0", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithTildeAndQuotes() {
        String input = "/foo/\"~0\"";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/\"~0\"", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithTildeAndIndex() {
        String input = "/foo/~1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/~1", pointer.toString());
    }

    @Test
    public void testMultiSegmentPointerWithTildeAndQuotesAndIndex() {
        String input = "/foo/\"~1\"";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/\"~1\"", pointer.toString());
    }
}