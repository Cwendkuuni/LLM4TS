package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

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
        assertEquals("/", empty.toString());
    }

    @Test
    public void testSingleSegmentPointer() {
        JsonPointer pointer = new JsonPointer("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertSame(JsonPointer.EMPTY, pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testMultipleSegmentPointer() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertSame(new JsonPointer("/baz"), pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~foo");
    }

    @Test
    public void testMatches() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/bar/baz");
        JsonPointer pointer3 = new JsonPointer("/foo");

        assertTrue(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer3.matches());

        assertSame(pointer2, pointer1.matchProperty("bar"));
        assertSame(pointer3, pointer2.matchProperty("baz"));
        assertNull(pointer1.matchProperty("qux"));

        assertSame(pointer2, pointer1.matchElement(1));
        assertNull(pointer1.matchElement(2));
    }
}