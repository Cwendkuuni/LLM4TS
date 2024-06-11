package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer.
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty instance.
     */
    @Test
    public void testConstructorEmpty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
        assertEquals(0, jsonPointer.hashCode());
        assertTrue(jsonPointer.equals(JsonPointer.EMPTY));
    }

    /**
     * Test constructor for non-empty instance.
     */
    @Test
    public void testConstructorNonEmpty() {
        String fullString = "/a/b/c";
        String segment = "b";
        JsonPointer next = new JsonPointer("/d");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertEquals(jsonPointer.tail(), next);
        assertEquals(fullString, jsonPointer.toString());
        int hashCode = jsonPointer.hashCode();
        JsonPointer jsonPointer2 = new JsonPointer(fullString, segment, next);
        assertTrue(jsonPointer.equals(jsonPointer2));
        assertEquals(hashCode, jsonPointer2.hashCode());
    }

    /**
     * Test compile method.
     */
    @Test
    public void testCompile() {
        String input = "/a/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
        assertEquals(input.hashCode(), jsonPointer.hashCode());

        String inputEmpty = "";
        JsonPointer jsonPointerEmpty = JsonPointer.compile(inputEmpty);
        assertTrue(jsonPointerEmpty.matches());
        assertNull(jsonPointerEmpty.getMatchingProperty());
        assertEquals(-1, jsonPointerEmpty.getMatchingIndex());
        assertNull(jsonPointerEmpty.tail());
        assertEquals("", jsonPointerEmpty.toString());
        assertEquals(0, jsonPointerEmpty.hashCode());
        assertTrue(jsonPointerEmpty.equals(JsonPointer.EMPTY));

        String inputInvalid = "invalid";
        try {
            JsonPointer jsonPointerInvalid = JsonPointer.compile(inputInvalid);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test mayMatchProperty method.
     */
    @Test
    public void testMayMatchProperty() {
        String fullString = "/a/b/c";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        assertTrue(jsonPointer.mayMatchProperty());
        assertEquals("b", jsonPointer.getMatchingProperty());

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertFalse(jsonPointerEmpty.mayMatchProperty());
        assertEquals(null, jsonPointerEmpty.getMatchingProperty());
    }

    /**
     * Test mayMatchElement method.
     */
    @Test
    public void testMayMatchElement() {
        String fullString = "/a/b/0";
        JsonPointer jsonPointer = new JsonPointer(fullString, "", null);
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());

        String fullString2 = "/a/b";
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "", null);
        assertFalse(jsonPointer2.mayMatchElement());
        assertEquals(-1, jsonPointer2.getMatchingIndex());

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertFalse(jsonPointerEmpty.mayMatchElement());
        assertEquals(-1, jsonPointerEmpty.getMatchingIndex());
    }

    /**
     * Test matchProperty method.
     */
    @Test
    public void testMatchProperty() {
        String fullString = "/a/b/c";
        String property = "b";
        JsonPointer jsonPointer = new JsonPointer(fullString, property, null);
        JsonPointer expected = new JsonPointer(fullString.substring(1), "", null);
        JsonPointer actual = jsonPointer.matchProperty(property);
        assertEquals(expected, actual);

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        JsonPointer actualEmpty = jsonPointerEmpty.matchProperty(property);
        assertNull(actualEmpty);
    }

    /**
     * Test matchElement method.
     */
    @Test
    public void testMatchElement() {
        String fullString = "/a/0";
        int index = 0;
        JsonPointer jsonPointer = new JsonPointer(fullString, "", null);
        JsonPointer expected = new JsonPointer("", "", null);
        JsonPointer actual = jsonPointer.matchElement(index);
        assertEquals(expected, actual);

        String fullString2 = "/a/1";
        int index2 = 1;
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "", null);
        assertNull(jsonPointer2.matchElement(index2));

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertNull(jsonPointerEmpty.matchElement(index));
    }

    /**
     * Test tail method.
     */
    @Test
    public void testTail() {
        String fullString = "/a/b/c";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        JsonPointer expected = new JsonPointer("/c", "", null);
        JsonPointer actual = jsonPointer.tail();
        assertEquals(expected, actual);

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertNull(jsonPointerEmpty.tail());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String fullString = "/a/b/c";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        assertEquals(fullString, jsonPointer.toString());

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertEquals("", jsonPointerEmpty.toString());
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        String fullString = "/a/b/c";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        int expectedHashCode = fullString.hashCode();
        int actualHashCode = jsonPointer.hashCode();
        assertEquals(expectedHashCode, actualHashCode);

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertEquals(0, jsonPointerEmpty.hashCode());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        String fullString = "/a/b/c";
        String fullString2 = "/a/b/c";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "b", null);
        assertTrue(jsonPointer.equals(jsonPointer2));
        assertTrue(jsonPointer.equals(JsonPointer.valueOf(fullString)));
        assertTrue(jsonPointer2.equals(JsonPointer.valueOf(fullString2)));

        JsonPointer jsonPointerEmpty = JsonPointer.EMPTY;
        assertTrue(jsonPointerEmpty.equals(JsonPointer.EMPTY));
        assertTrue(jsonPointerEmpty.equals(JsonPointer.valueOf("")));
    }
}