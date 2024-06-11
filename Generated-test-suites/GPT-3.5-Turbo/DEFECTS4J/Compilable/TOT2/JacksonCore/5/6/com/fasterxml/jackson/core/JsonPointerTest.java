package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompile_emptyInput_shouldReturnEmptyPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testCompile_validInput_shouldReturnJsonPointerInstance() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testCompile_invalidInput_shouldThrowIllegalArgumentException() {
        try {
            JsonPointer.compile("test");
            fail("Should throw IllegalArgumentException for input without leading slash");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMatchProperty_matchingName_shouldReturnNextSegmentPointer() {
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer result = pointer.matchProperty("test");
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test
    public void testMatchProperty_nonMatchingName_shouldReturnNull() {
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer result = pointer.matchProperty("other");
        assertNull(result);
    }

    @Test
    public void testMatchElement_matchingIndex_shouldReturnNextSegmentPointer() {
        JsonPointer pointer = new JsonPointer("/1", "1", new JsonPointer());
        JsonPointer result = pointer.matchElement(1);
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test
    public void testMatchElement_nonMatchingIndex_shouldReturnNull() {
        JsonPointer pointer = new JsonPointer("/1", "1", new JsonPointer());
        JsonPointer result = pointer.matchElement(2);
        assertNull(result);
    }

    @Test
    public void testTail_shouldReturnNextSegmentPointer() {
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer result = pointer.tail();
        assertNotNull(result);
        assertEquals("", result.toString());
    }
}