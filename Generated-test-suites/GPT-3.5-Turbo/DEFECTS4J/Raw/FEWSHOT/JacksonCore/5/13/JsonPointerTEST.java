
package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/");
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithProperty() {
        JsonPointer pointer = JsonPointer.compile("/name");
        assertFalse(pointer.matches());
        assertEquals("name", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        JsonPointer next = pointer.tail();
        assertTrue(next.matches());
        assertNull(next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithNestedProperty() {
        JsonPointer pointer = JsonPointer.compile("/parent/child");
        assertFalse(pointer.matches());
        assertEquals("parent", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        JsonPointer next = pointer.tail();
        assertFalse(next.matches());
        assertEquals("child", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());

        JsonPointer nextNext = next.tail();
        assertTrue(nextNext.matches());
        assertNull(nextNext.getMatchingProperty());
        assertEquals(-1, nextNext.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithArrayIndex() {
        JsonPointer pointer = JsonPointer.compile("/array/0");
        assertFalse(pointer.matches());
        assertEquals("array", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        JsonPointer next = pointer.tail();
        assertFalse(next.matches());
        assertEquals("0", next.getMatchingProperty());
        assertEquals(0, next.getMatchingIndex());

        JsonPointer nextNext = next.tail();
        assertTrue(nextNext.matches());
        assertNull(nextNext.getMatchingProperty());
        assertEquals(-1, nextNext.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithNestedArrayIndex() {
        JsonPointer pointer = JsonPointer.compile("/parent/children/4");
        assertFalse(pointer.matches());
        assertEquals("parent", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        JsonPointer next = pointer.tail();
        assertFalse(next.matches());
        assertEquals("children", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());

        JsonPointer nextNext = next.tail();
        assertFalse(nextNext.matches());
        assertEquals("4", nextNext.getMatchingProperty());
        assertEquals(4, nextNext.getMatchingIndex());

        JsonPointer nextNextNext = nextNext.tail();
        assertTrue(nextNextNext.matches());
        assertNull(nextNextNext.getMatchingProperty());
        assertEquals(-1, nextNextNext.getMatchingIndex());
    }
}
