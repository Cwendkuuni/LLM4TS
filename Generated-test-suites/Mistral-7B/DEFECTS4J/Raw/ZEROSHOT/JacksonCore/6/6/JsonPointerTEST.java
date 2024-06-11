package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
        assertEquals(empty, empty.tail());
        assertNull(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test
    public void testCompileWithEmptyString() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("not/valid");
    }

    @Test
    public void testCompileWithSingleSegment() {
        JsonPointer pointer = JsonPointer.compile("/a/b");
        assertEquals("/a/b", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertTrue(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testCompileWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/a/b/c/d");
        assertEquals("/a/b/c/d", pointer.toString());
        JsonPointer tail = pointer.tail();
        assertEquals("/b/c/d", tail.toString());
        assertTrue(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertSame(pointer, tail.tail());
    }

    @Test
    public void testMatchesProperty() {
        JsonPointer pointer = JsonPointer.compile("/a/b");
        JsonPointer matchingPointer = new JsonPointer("/a/b", "b", JsonPointer.EMPTY);
        assertTrue(pointer.matchesProperty("b"));
        assertSame(matchingPointer, pointer.tail());
    }

    @Test
    public void testMatchesElement() {
        JsonPointer pointer = JsonPointer.compile("/a/0");
        JsonPointer matchingPointer = new JsonPointer("/a", "", JsonPointer.compile("/0"));
        assertTrue(pointer.matchesElement(0));
        assertSame(matchingPointer, pointer.tail());
    }

    @Test
    public void testEquals() throws UnsupportedEncodingException {
        String json = "{\"a\": [1, 2, 3], \"b\": 42}";
        JsonPointer pointer1 = JsonPointer.compile("/a/1");
        JsonPointer pointer2 = new JsonPointer("/a/1", "", JsonPointer.compile("/1"));
        assertEquals(pointer1, pointer2);
        JsonPointer pointer3 = new JsonPointer("/a/2");
        assertNotEquals(pointer1, pointer3);
        JsonPointer pointer4 = new JsonPointer("/a/b");
        assertNotEquals(pointer1, pointer4);
    }
}