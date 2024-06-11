package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/segment1");
        assertEquals("/segment1", pointer.toString());
    }

    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("segment1");
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/segment1");
        JsonPointer pointer2 = JsonPointer.compile("/segment1");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/segment1");
        JsonPointer pointer2 = JsonPointer.compile("/segment1");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/segment1");
        assertNull(pointer.matchProperty("segment2"));
        assertNotNull(pointer.matchProperty("segment1"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/1");
        assertNull(pointer.matchElement(2));
        assertNotNull(pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/segment1/segment2");
        assertEquals("/segment2", pointer.tail().toString());
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/segment1");
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/segment1");
        assertEquals("segment1", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/1");
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/segment1");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/1");
        assertTrue(pointer.mayMatchElement());
    }
}