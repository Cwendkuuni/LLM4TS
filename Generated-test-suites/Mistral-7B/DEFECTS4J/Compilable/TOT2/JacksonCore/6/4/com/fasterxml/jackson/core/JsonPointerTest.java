package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*; // for assertions
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSegment() {
        JsonPointer jsonPointer = new JsonPointer("/segment", null, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/segment", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithTwoSegments() {
        JsonPointer jsonPointer = new JsonPointer("/segment1/segment2", null, JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("segment2", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("/segment1/segment2", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput() {
        JsonPointer jsonPointer = JsonPointer.compile("nonValidJsonPointer");
    }
}