package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testBasicParsing() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar/baz", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());

        pointer = pointer.matchProperty("bar");
        assertEquals("/foo/baz", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("baz", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());

        pointer = pointer.matchProperty("baz");
        assertEquals("/foo", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());

        pointer = pointer.matchProperty("foo");
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testIndexParsing() {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/0/bar", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = pointer.matchElement(0);
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testDeepParsing() {
        String input = "/foo/0/bar/1/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/0/bar/1/baz", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("baz", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = pointer.matchElement(1);
        assertEquals("/foo/0/bar", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());

        pointer = pointer.matchProperty("bar");
        assertEquals("/foo/0", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());

        pointer = pointer.matchProperty("foo");
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }
}