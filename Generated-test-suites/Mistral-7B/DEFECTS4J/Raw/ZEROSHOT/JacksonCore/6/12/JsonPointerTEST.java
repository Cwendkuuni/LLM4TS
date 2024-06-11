package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class
 */
public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/", jsonPointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/b/c");
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/b/c", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~/a/b/c");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/b/c");
        JsonPointer matchingProperty = jsonPointer.matchProperty("b");
        assertEquals("/c", matchingProperty.toString());
        assertNull(jsonPointer.matchProperty("a"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/0/c");
        JsonPointer matchingElement = jsonPointer.matchElement(0);
        assertEquals("/c", matchingElement.toString());
        assertNull(jsonPointer.matchElement(-1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/b/c");
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/b/c", tail.toString());
        JsonPointer tailOfTail = tail.tail();
        assertEquals("/c", tailOfTail.toString());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/a/b/c");
        assertEquals("/a/b/c", jsonPointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/a/b/c");
        JsonPointer jsonPointer2 = JsonPointer.compile("/a/b/c");
        JsonPointer jsonPointer3 = JsonPointer.compile("/a/d/e");
        assertEquals(jsonPointer1, jsonPointer1);
        assertEquals(jsonPointer1, jsonPointer2);
        assertNotEquals(jsonPointer1, jsonPointer3);
        int hashCode1 = jsonPointer1.hashCode();
        int hashCode2 = jsonPointer2.hashCode();
        int hashCode3 = jsonPointer3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}