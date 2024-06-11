package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test", "test", null);
        JsonPointer pointer2 = new JsonPointer("/test", "test", null);
        assertTrue(pointer1.equals(pointer2));
    }
}