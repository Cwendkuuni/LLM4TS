package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/2");
        assertEquals(2, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/2");
        assertTrue(pointer.mayMatchElement());
    }

    // Add more test cases for other methods as needed

}