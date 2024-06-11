package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty JsonPointer instance
     */
    @Test
    public void testEmptyJsonPointerConstructor() {
        JsonPointer emptyJsonPointer = new JsonPointer();
        assertEquals("", emptyJsonPointer.toString());
        assertNull(emptyJsonPointer.tail());
        assertEquals(-1, emptyJsonPointer.getMatchingIndex());
        assertEquals("", emptyJsonPointer.getMatchingProperty());
        assertTrue(emptyJsonPointer.matches());
        assertFalse(emptyJsonPointer.mayMatchProperty());
        assertFalse(emptyJsonPointer.mayMatchElement());
    }

    /**
     * Test constructor for non-empty JsonPointer instance
     */
    @Test
    public void testNonEmptyJsonPointerConstructor() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/a/b/c/d/0", jsonPointer.toString());
        assertEquals("/c/d/0", jsonPointer.tail().toString());
        assertEquals("d", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
    }

    /**
     * Test compilation of invalid JSON Pointer input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput() {
        String input = "invalid";
        JsonPointer.compile(input);
    }

    /**
     * Test factory method for creating JsonPointer instance from array of raw segments
     */
    @Test
    public void testJsonPointerFromSegment() {
        String[] segments = {"/a", "/b", "/c"};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        assertEquals("/a/b/c", jsonPointer.toString());
        assertEquals("/c", jsonPointer.tail().toString());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
    }

    /**
     * Test matches method
     */
    @Test
    public void testMatches() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer emptyJsonPointer = new JsonPointer();
        JsonPointer nonEmptyJsonPointer = new JsonPointer("/a/b");
        JsonPointer matchingJsonPointer = new JsonPointer(input, "d", emptyJsonPointer);

        assertTrue(jsonPointer.matches());
        assertFalse(emptyJsonPointer.matches());
        assertFalse(nonEmptyJsonPointer.matches());
        assertTrue(matchingJsonPointer.matches());
    }

    /**
     * Test getMatchingProperty method
     */
    @Test
    public void testGetMatchingProperty() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        assertEquals("d", jsonPointer.getMatchingProperty());
        assertNull(new JsonPointer().getMatchingProperty());
    }

    /**
     * Test getMatchingIndex method
     */
    @Test
    public void testGetMatchingIndex() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNull(new JsonPointer().getMatchingIndex());
    }

    /**
     * Test matchProperty method
     */
    @Test
    public void testMatchProperty() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        JsonPointer propertyJsonPointer = new JsonPointer("/b");
        JsonPointer nullJsonPointer = null;

        assertNull(jsonPointer.matchProperty(null));
        assertNull(jsonPointer.matchProperty(""));
        assertSame(jsonPointer, jsonPointer.matchProperty(jsonPointer.toString()));
        assertNull(jsonPointer.matchProperty("e"));
        assertSame(propertyJsonPointer, jsonPointer.matchProperty("b"));
    }

    /**
     * Test matchElement method
     */
    @Test
    public void testMatchElement() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        JsonPointer elementJsonPointer = new JsonPointer("/c/d/0");
        JsonPointer nullJsonPointer = null;

        assertNull(jsonPointer.matchElement(null));
        assertNull(jsonPointer.matchElement(-1));
        assertSame(jsonPointer, jsonPointer.matchElement(jsonPointer.getMatchingIndex()));
        assertNull(jsonPointer.matchElement(1));
        assertSame(elementJsonPointer, jsonPointer.matchElement(jsonPointer.getMatchingIndex() + 1));
    }

    /**
     * Test tail method
     */
    @Test
    public void testTail() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals("/c/d/0", tailJsonPointer.toString());
        assertNull(new JsonPointer().tail());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        assertEquals("/a/b/c/d/0", jsonPointer.toString());
        assertEquals("", new JsonPointer().toString());
    }

    /**
     * Test hashCode method
     */
    @Test
    public void testHashCode() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer jsonPointer2 = new JsonPointer("/a/b/c/d/0");

        assertEquals(jsonPointer.hashCode(), jsonPointer2.hashCode());
        assertNotEquals(jsonPointer.hashCode(), new JsonPointer().hashCode());
    }

    /**
     * Test equals method
     */
    @Test
    public void testEquals() {
        String input = "/a/b/c/d/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer jsonPointer2 = new JsonPointer("/a/b/c/d/0");
        JsonPointer jsonPointer3 = new JsonPointer("/a/b/c");
        JsonPointer jsonPointer4 = new JsonPointer("/a/b/c");
        JsonPointer jsonPointerEmpty = new JsonPointer();

        assertTrue(jsonPointer.equals(jsonPointer));
        assertTrue(jsonPointer.equals(jsonPointer2));
        assertFalse(jsonPointer.equals(jsonPointer3));
        assertFalse(jsonPointer.equals(jsonPointer4));
        assertFalse(jsonPointer.equals(jsonPointerEmpty));
    }
}