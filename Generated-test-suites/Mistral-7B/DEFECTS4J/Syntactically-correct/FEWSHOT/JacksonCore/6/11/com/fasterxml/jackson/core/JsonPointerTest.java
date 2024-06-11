package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPropertyMatchingPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testElementMatchingPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertEquals("/foo/0", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testPropertyAndElementMatchingPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        assertEquals("/foo/0/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        JsonPointer match = pointer1.matchProperty("bar");
        assertNull(match);
        match = pointer2.matchProperty("bar");
        assertNull(match);
        match = pointer1.matchProperty("foo");
        assertEquals(pointer2, match);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/0");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        JsonPointer match = pointer1.matchElement(0);
        assertNull(match);
        match = pointer2.matchElement(0);
        assertNull(match);
        match = pointer1.matchElement(-1);
        assertNull(match);
        match = pointer2.matchElement(0);
        assertEquals(pointer1, match);
    }

    @Test
    public void testTail() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        assertEquals(pointer2, pointer1.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        JsonPointer pointer3 = JsonPointer.compile("/baz");
        assertEquals("/foo/bar", pointer1.toString());
        assertEquals("/foo", pointer2.toString());
        assertEquals("/baz", pointer3.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/baz");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/baz");
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }
}