package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/foo", tail.toString());
    }

    @Test
    public void testPointerWithElement() {
        JsonPointer pointer = JsonPointer.compile("/list/0");
        assertEquals("/list/0", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/list", tail.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo/bar/baz", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("baz", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/foo/bar", tail.toString());
    }

    @Test
    public void testPointerWithLongElementIndex() {
        JsonPointer pointer = JsonPointer.compile("/list/999999999");
        assertEquals("/list/999999999", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(999999999, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/list", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~/invalid");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer match = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
        JsonPointer tail = pointer.tail();
        assertTrue(pointer.matchProperty("bar").equals(match));
        assertTrue(pointer.matchProperty("qux").isNull());
        assertTrue(tail.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/list/0");
        JsonPointer match = new JsonPointer("/list", null, JsonPointer.EMPTY);
        JsonPointer tail = pointer.tail();
        assertTrue(pointer.matchElement(0).equals(match));
        assertTrue(pointer.matchElement(-1).isNull());
        assertTrue(tail.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo/bar", pointer.tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/list/0");
        JsonPointer pointer3 = JsonPointer.compile("/");
        assertEquals("/foo/bar", pointer1.toString());
        assertEquals("/list/0", pointer2.toString());
        assertEquals("/", pointer3.toString());
    }

    @Test
    public void testEqualsHashcode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/list/0");
        JsonPointer pointer4 = new JsonPointer("/foo/baz", null, JsonPointer.EMPTY);
        assertEquals(pointer1, pointer2);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1, pointer3);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
        assertNotEquals(pointer1, pointer4);
        assertNotEquals(pointer1.hashCode(), pointer4.hashCode());
    }

    @Test
    public void testParseFromSegments() {
        // not implemented
    }
}