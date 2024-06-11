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
    }

    @Test
    public void testBasicPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testPropertyMatching() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer property = new JsonPointer("/foo", null);
        JsonPointer expected = new JsonPointer("/bar", null);
        assertEquals(expected, pointer.matchProperty("bar"));
        assertEquals(property, pointer.matchProperty("foo"));
        assertNull(pointer.matchProperty("baz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testElementMatching() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testMultiSegmentPointer() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer expectedBar = new JsonPointer("/bar", null);
        JsonPointer expectedBaz = new JsonPointer("/baz", null);
        assertEquals(expectedBar, pointer.tail());
        assertEquals(expectedBaz, expectedBar.tail());
        assertEquals("bar", expectedBar.getMatchingProperty());
        assertEquals("baz", expectedBaz.getMatchingProperty());
        assertNull(pointer.matchProperty("qux"));
    }

    @Test
    public void testIndexMatchingWithLongStrings() {
        String input = "/foo/10";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(10, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testEscapedSegments() {
        String input = "/foo/~0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer expectedBar = new JsonPointer("/foo/0/bar", null);
        assertEquals(expectedBar, pointer);
        assertEquals("bar", expectedBar.getMatchingProperty());
        assertNull(pointer.tail());
    }

    @Test
    public void testEmptyArray() {
        String input = "/foo/[]";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testSingleElementArray() {
        String input = "/foo/[0]";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testMultipleElementArray() {
        String input = "/foo/[0]/[1]";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer expectedOne = new JsonPointer("/foo/[0]", null);
        JsonPointer expectedTwo = new JsonPointer("/foo/[1]", null);
        assertEquals(expectedTwo, pointer.tail());
        assertEquals("1", expectedTwo.getMatchingProperty());
        assertEquals("1", expectedTwo.toString().substring(3));
        assertEquals(expectedOne, expectedTwo.tail());
        assertEquals("0", expectedOne.getMatchingProperty());
        assertEquals("0", expectedOne.toString().substring(3));
        assertNull(pointer.matchProperty("qux"));
    }
}