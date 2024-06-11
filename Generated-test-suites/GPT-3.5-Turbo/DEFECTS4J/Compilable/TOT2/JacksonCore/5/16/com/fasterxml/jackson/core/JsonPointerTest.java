package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertNotNull(pointer);
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNotNull(pointer);
        assertEquals("", pointer.toString());
    }

    @Test
    public void testCompileInvalidInput() {
        try {
            JsonPointer.compile("foo/bar");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/3");
        assertTrue(pointer.mayMatchElement());
        assertEquals(3, pointer.getMatchingIndex());
        JsonPointer result = pointer.matchElement(3);
        assertNull(result);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/name");
        assertTrue(pointer.mayMatchProperty());
        assertEquals("name", pointer.getMatchingProperty());
        JsonPointer result = pointer.matchProperty("name");
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("bar", tail.toString());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
}