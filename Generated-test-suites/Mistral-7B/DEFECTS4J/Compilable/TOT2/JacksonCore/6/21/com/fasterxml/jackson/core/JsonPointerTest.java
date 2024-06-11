package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.tail());
    }

    @Test
    public void testBasicPointer() {
        JsonPointer pointer = JsonPointer.compile("/testProperty");
        assertTrue(pointer.matches());
        assertEquals("testProperty", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("testProperty");
    }

    @Test
    public void testPointerWithTail() {
        JsonPointer pointer = JsonPointer.compile("/testProperty/0");
        assertTrue(pointer.matches());
        assertEquals("testProperty", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test
    public void testPointerWithQuotedTail() {
        JsonPointer pointer = JsonPointer.compile("/testProperty/~1");
        assertTrue(pointer.matches());
        assertEquals("testProperty", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }
}