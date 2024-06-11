package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
    }

    @Test
    public void testSimplePointer() {
        String pointerInput = "/test/property";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithIndex() {
        String pointerInput = "/test/0";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerInput() {
        String pointerInput = "testproperty";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerStartWithoutSlash() {
        String pointerInput = "test";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
    }

    @Test
    public void testPointerTail() {
        String pointerInput = "/test/property/child";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        JsonPointer tail = pointer.tail();
        assertEquals("/test/property", tail.toString());
    }

    @Test
    public void testPointerMatchProperty() {
        String pointerInput = "/test/property";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        String matchingProperty = "property";
        JsonPointer matchedPointer = pointer.matchProperty(matchingProperty);
        assertNotNull(matchedPointer);
        assertEquals(pointerInput, matchedPointer.toString());
    }

    @Test
    public void testPointerMatchWithoutProperty() {
        String pointerInput = "/test/property";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        String nonMatchingProperty = "nonmatching";
        JsonPointer matchedPointer = pointer.matchProperty(nonMatchingProperty);
        assertNull(matchedPointer);
    }

    @Test
    public void testPointerMatchElement() {
        String pointerInput = "/test/0";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        JsonPointer matchedPointer = pointer.matchElement(0);
        assertNotNull(matchedPointer);
        assertEquals(pointerInput, matchedPointer.toString());
    }

    @Test
    public void testPointerMatchInvalidElementIndex() {
        String pointerInput = "/test/0";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        JsonPointer matchedPointer = pointer.matchElement(-1);
        assertNull(matchedPointer);
    }
}