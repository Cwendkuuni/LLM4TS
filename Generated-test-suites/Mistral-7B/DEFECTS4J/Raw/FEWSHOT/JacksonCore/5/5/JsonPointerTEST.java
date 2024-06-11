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
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPointerWithPropertyAndElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertNull(tail.getMatchingProperty());
        assertTrue(tail.mayMatchProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals("/", tail.getMatchingProperty());
        assertEquals("0", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "~foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        String expected = "/bar/baz";
        assertEquals(expected, pointer.toString());
    }

    @Test
    public void testPointerWithQuotedSegment() {
        String input = "/foo/~1o/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo/1o", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(expected, pointer.toString());
    }

    @Test
    public void testPointerWithMultipleQuotedSegments() {
        String input = "/foo/~1o/bar/~2ba/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo/1o/bar/2ba", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(expected, pointer.toString());
    }

    @Test
    public void testPointerWithTrailingSlash() {
        String input = "/foo/";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPointerWithLeadingSlash() {
        String input = "/";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/", pointer.toString());
    }

    @Test
    public void testMatchesProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        String property = "foo";
        JsonPointer matchingPointer = new JsonPointer(input, property, null);
        assertTrue(pointer.matches());
        assertTrue(pointer.matchesProperty(property));
        assertSame(matchingPointer, pointer.matchProperty(property));
        assertNull(pointer.matchElement(1));
    }

    @Test
    public void testMatchesElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertTrue(pointer.matchesElement(0));
        assertNull(pointer.matchProperty("bar"));
    }

    @Test
    public void testTailPointer() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/baz", tail.toString());
    }

    @Test
    public void testTailOfEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.tail());
    }
}