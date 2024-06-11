package com.fasterxml.jackson.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
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
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testPropertyPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testElementPointer() {
        JsonPointer pointer = JsonPointer.compile("/list/1");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testTailPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/");
        JsonPointer tail = pointer.tail();
        assertTrue(tail.matches());
        assertNull(tail.getMatchingProperty());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertEquals("/bar", tail._asString);
    }

    @Test
    public void testQuotedPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar~1/baz~0");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/bar/baz", pointer._asString);
    }

    @Test
    public void testCompoundPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/list[0]/bar");
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/foo/list[0]/bar", pointer._asString);
    }
}