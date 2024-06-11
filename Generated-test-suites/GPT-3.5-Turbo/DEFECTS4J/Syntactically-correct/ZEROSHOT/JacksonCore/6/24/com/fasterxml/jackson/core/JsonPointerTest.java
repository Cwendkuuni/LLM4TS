
package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    // Test for empty pointer
    @Test
    public void testEmpty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
    }

    // Test for non-empty pointer
    @Test
    public void testNonEmpty() {
        // Construct a non-empty pointer
        JsonPointer pointer = new JsonPointer("/a/b/c");
        assertFalse(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/b/c", pointer.tail().toString());
        // Test matching property
        JsonPointer matchProperty = pointer.matchProperty("a");
        assertNull(matchProperty);
        matchProperty = pointer.matchProperty("b");
        assertNotNull(matchProperty);
        assertTrue(matchProperty.matches());
        // Test matching index
        JsonPointer matchElement = pointer.matchElement(0);
        assertNull(matchElement);
        matchElement = pointer.matchElement(1);
        assertNotNull(matchElement);
        assertTrue(matchElement.matches());
    }

    // Test for escaped characters
    @Test
    public void testEscapedCharacters() {
        // Construct a pointer with escaped characters
        JsonPointer pointer = new JsonPointer("/~0~1~2/d/e");
        assertFalse(pointer.matches());
        assertEquals("~", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/d/e", pointer.tail().toString());
        // Test matching property
        JsonPointer matchProperty = pointer.matchProperty("~");
        assertNotNull(matchProperty);
        assertFalse(matchProperty.matches());
        // Test matching index
        JsonPointer matchElement = pointer.matchElement(0);
        assertNull(matchElement);
    }

    // Test for invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }
}
