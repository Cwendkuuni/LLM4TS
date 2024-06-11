package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("invalid_pointer");
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testPointerWithIndex() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    @Test
    public void testPointerWithPropertyAndIndex() {
        JsonPointer pointer = JsonPointer.compile("/test/property/0");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/property/0", pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/test/property1/property2/property3");
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals( JsonPointer.compile("/test/property1"), pointer.tail());
        assertEquals("/test/property1/property2/property3", pointer.toString());
    }

    @Test
    public void testPointerWithEscapedCharacter() {
        JsonPointer pointer = JsonPointer.compile("/test/property~1/property2");
        assertFalse(pointer.matches());
        assertEquals("property~1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(JsonPointer.compile("/test/property~1"), pointer.tail());
        assertEquals("/test/property~1/property2", pointer.toString());
    }

    @Test
    public void testPointerWithEmptySegment() {
        JsonPointer pointer = JsonPointer.compile("/test//property");
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test//property", pointer.toString());
    }
}