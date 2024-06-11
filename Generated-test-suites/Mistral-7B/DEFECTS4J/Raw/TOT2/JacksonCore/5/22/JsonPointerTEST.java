package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
    }

    // ... (other tests)

    @Test
    public void testInputStringContainsMultipleSlashes() {
        String input = "/a/b//c/d";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/a/b/c/d", pointer.toString());
        // ... (other assertions)
    }

    
}