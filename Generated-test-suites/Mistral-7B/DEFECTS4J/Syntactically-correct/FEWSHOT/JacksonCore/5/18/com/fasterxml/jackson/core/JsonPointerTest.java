package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("/", pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertNull(pointer.tail());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testPointerWithPropertyAndElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "~bar";
        JsonPointer.compile(input);
    }

    @Test
    public void testPointerWithTail() {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertNotNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchingProperty() {
        String input = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
        JsonPointer matchingProperty = pointer1.matchProperty("bar");
        assertEquals(pointer2, matchingProperty);
    }

    @Test
    public void testMatchingElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer matchingElement = pointer.matchElement(0);
        assertEquals(JsonPointer.EMPTY, matchingElement);
    }

    @Test
    public void testMatchingElementNegativeIndex() {
        String input = "/foo/-1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(-1, pointer.getMatchingIndex());
        JsonPointer matchingElement = pointer.matchElement(-1);
        assertEquals(JsonPointer.EMPTY, matchingElement);
    }

    @Test
    public void testMatchingElementNotExisting() {
        String input = "/foo/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.mayMatchElement());
        JsonPointer matchingElement = pointer.matchElement(1);
        assertEquals(JsonPointer.EMPTY, matchingElement);
    }

    @Test
    public void testMatches() {
        String input1 = "/foo/0";
        JsonPointer pointer1 = JsonPointer.compile(input1);

        String input2 = "/foo/1";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        String input3 = "/foo";
        JsonPointer pointer3 = JsonPointer.compile(input3);

        assertTrue(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer3.matches());
    }

    @Test
    public void testToString() {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String input1 = "/foo/0";
        JsonPointer pointer1 = JsonPointer.compile(input1);

        String input2 = "/foo/0";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        String input3 = "/foo/1";
        JsonPointer pointer3 = JsonPointer.compile(input3);

        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertEquals(pointer1, pointer1);

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertEquals(pointer1, pointer2);

        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
        assertNotEquals(pointer1, pointer3);
    }
}