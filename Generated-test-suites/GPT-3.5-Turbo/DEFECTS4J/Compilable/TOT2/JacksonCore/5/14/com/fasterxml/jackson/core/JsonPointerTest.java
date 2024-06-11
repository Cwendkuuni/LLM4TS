package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.tail());
    }

    @Test
    public void testValidJsonPointerCompilation() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testInvalidJsonPointerCompilation() {
        try {
            JsonPointer.compile("foo/bar");
            fail("Compilation should fail for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for other methods and edge cases

}