package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.NumberInput;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonPointerTest {

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/123");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(123, pointer.getMatchingIndex());
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals("bar", pointer.tail().getMatchingProperty());
        assertNull(pointer.tail().tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_invalidInput() {
        JsonPointer pointer = JsonPointer.compile("invalid");
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/");
        JsonPointer pointer2 = JsonPointer.compile("/");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testNotEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/bar");
        assertFalse(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertFalse(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo");
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/");
        assertEquals("/", pointer.toString());

        pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/");
        JsonPointer pointer2 = JsonPointer.compile("/");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer matchedPointer = pointer.matchProperty("foo");
        assertTrue(matchedPointer.matches());
        assertNull(matchedPointer.tail());

        matchedPointer = pointer.matchProperty("bar");
        assertNull(matchedPointer);

        matchedPointer = pointer.matchProperty("foo/bar");
        assertNull(matchedPointer);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/123");
        JsonPointer matchedPointer = pointer.matchElement(123);
        assertTrue(matchedPointer.matches());
        assertNull(matchedPointer.tail());

        matchedPointer = pointer.matchElement(456);
        assertNull(matchedPointer);

        matchedPointer = pointer.matchElement(-123);
        assertNull(matchedPointer);

        matchedPointer = pointer.matchElement(12.3);
        assertNull(matchedPointer);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/");
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.tail().getMatchingProperty());
        assertNull(pointer.tail().tail());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("bar", pointer.tail().getMatchingProperty());
        assertNull(pointer.tail().tail());
    }
}