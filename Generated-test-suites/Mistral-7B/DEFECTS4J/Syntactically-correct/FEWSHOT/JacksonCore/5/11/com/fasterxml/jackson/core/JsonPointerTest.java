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
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testPropertyPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals("bar", pointer.tail().getMatchingProperty());
    }

    @Test
    public void testElementPointer() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testMultipleElementPointer() {
        String input = "/foo/1/2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("2", tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("./");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithTrailingSlash() {
        JsonPointer.compile("/.");
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchProperty = new JsonPointer("/foo", "bar", null);
        JsonPointer tail = pointer.tail();
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertEquals("bar", pointer.matchProperty("bar").toString());
        assertEquals(matchProperty, pointer.matchProperty("bar"));
        assertNull(pointer.matchProperty("baz"));
        assertEquals(tail, pointer.matchProperty("foo").tail());
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchElement = new JsonPointer("/foo", null, null);
        JsonPointer tail = pointer.tail();
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.matchElement(0).getMatchingIndex());
        assertEquals(matchElement, pointer.matchElement(0));
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(1));
        assertEquals(tail, pointer.matchElement("foo").tail());
    }

    @Test
    public void testDeepPointer() {
        String input = "/foo/0/bar/1/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(1, tail.getMatchingIndex());
        tail = tail.tail();
        assertEquals("baz", tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
    }

    @Test
    public void testTail() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/bar", tail.toString());
        assertEquals(null, tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertNull(tail.tail());
    }
}