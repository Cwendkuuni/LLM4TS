package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
    }

    @Test
    public void testJsonPointerConstruction() {
        JsonPointer pointer1 = JsonPointer.compile("/");
        assertTrue(pointer1.matches());

        JsonPointer pointer2 = JsonPointer.compile("/foo");
        assertFalse(pointer2.matches());
        assertTrue(pointer2.mayMatchProperty());
        assertEquals("foo", pointer2.getMatchingProperty());

        JsonPointer pointer3 = JsonPointer.compile("/3");
        assertFalse(pointer3.matches());
        assertTrue(pointer3.mayMatchElement());
        assertEquals(3, pointer3.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = pointer1.matchProperty("foo");
        assertTrue(pointer2.matches());

        JsonPointer pointer3 = pointer1.matchProperty("bar");
        assertNull(pointer3);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = JsonPointer.compile("/3");
        JsonPointer pointer2 = pointer1.matchElement(3);
        assertTrue(pointer2.matches());

        JsonPointer pointer3 = pointer1.matchElement(4);
        assertNull(pointer3);
    }

    @Test
    public void testTail() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer1.tail();
        assertEquals("/bar", tail.toString());

        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertNull(pointer2.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer1.toString());

        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertEquals("", pointer2.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        JsonPointer pointer3 = JsonPointer.compile("/foo");
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer2.equals(pointer3));
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testInvalidInput() {
        try {
            JsonPointer.compile("foo");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo\"", e.getMessage());
        }

        try {
            JsonPointer.compile("/foo/~invalid");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression contains invalid escape sequence", e.getMessage());
        }
    }
}