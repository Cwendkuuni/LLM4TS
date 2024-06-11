package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyJsonPointer() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", JsonPointer.EMPTY);

        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/segment");

        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("segment");
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", JsonPointer.EMPTY);
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", JsonPointer.EMPTY);

        assertTrue(pointer1.equals(pointer2));
    }

    // Add more test cases for edge cases and exception handling scenarios here

}