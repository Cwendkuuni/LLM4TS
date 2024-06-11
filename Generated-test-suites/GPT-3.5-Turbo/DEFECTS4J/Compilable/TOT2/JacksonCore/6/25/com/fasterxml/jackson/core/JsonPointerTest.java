package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("segment");
    }
    
    // Additional test cases for other methods in JsonPointer class can be added here
    
}