package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSegment() {
        String input = "/property/element";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("property", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/element", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String input = "property";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        fail("Expected IllegalArgumentException but got: " + jsonPointer);
    }

    @Test
    public void testJsonPointerWithQuotedSegments() {
        String input = "/property/~1~2/element";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("property", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/element", tail.toString());
    }
}