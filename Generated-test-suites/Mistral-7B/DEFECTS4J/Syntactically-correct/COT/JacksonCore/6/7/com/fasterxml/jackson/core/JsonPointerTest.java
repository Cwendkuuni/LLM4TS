package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty instance
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    /**
     * Test constructor for non-empty instance
     */
    @Test
    public void testNonEmptyConstructor() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);

        assertFalse(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertSame(next, jsonPointer.tail());
        assertEquals(fullString, jsonPointer.toString());
    }

    /**
     * Test compile method with valid input
     */
    @Test
    public void testCompileValidInput() {
        String input = "/test/property1/index1";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        assertFalse(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    /**
     * Test compile method with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        String input = "test";
        JsonPointer.compile(input);
    }

    /**
     * Test matches method
     */
    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer tail = new JsonPointer("/test/property1/index1");
        assertTrue(jsonPointer.matches());
        assertFalse(tail.matches());
    }

    /**
     * Test getMatchingProperty method
     */
    @Test
    public void testGetMatchingProperty() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertEquals("property1", jsonPointer.getMatchingProperty());
    }

    /**
     * Test getMatchingIndex method
     */
    @Test
    public void testGetMatchingIndex() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertEquals(1, jsonPointer.getMatchingIndex());
    }

    /**
     * Test mayMatchProperty method
     */
    @Test
    public void testMayMatchProperty() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertTrue(jsonPointer.mayMatchProperty());
    }

    /**
     * Test mayMatchElement method
     */
    @Test
    public void testMayMatchElement() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertFalse(jsonPointer.mayMatchElement());
    }

    /**
     * Test matchProperty method
     */
    @Test
    public void testMatchProperty() {
        String fullString1 = "/test/property1/index1";
        String matchingProperty = "property1";
        String fullString2 = "/test/property1/index2";
        JsonPointer jsonPointer1 = new JsonPointer(fullString1, matchingProperty, EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, matchingProperty, EMPTY);
        JsonPointer expected = new JsonPointer();
        assertTrue(jsonPointer1.matches());
        assertTrue(jsonPointer1.mayMatchProperty());
        assertEquals(expected, jsonPointer1.matchProperty(matchingProperty));
        assertNull(jsonPointer2.matchProperty(matchingProperty));
    }

    /**
     * Test matchElement method
     */
    @Test
    public void testMatchElement() {
        String fullString = "/test/array/index1/array/index2";
        int matchingIndex1 = 1;
        int matchingIndex2 = 2;
        JsonPointer jsonPointer = new JsonPointer(fullString, "", EMPTY);
        JsonPointer expected1 = new JsonPointer("/test/array/index1");
        JsonPointer expected2 = new JsonPointer("/test/array/index2");
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(expected1, jsonPointer.matchElement(matchingIndex1));
        assertEquals(expected2, jsonPointer.matchElement(matchingIndex2));
        assertNull(jsonPointer.matchElement(-1));
        assertNull(jsonPointer.matchElement(3));
    }

    /**
     * Test tail method
     */
    @Test
    public void testTail() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertEquals(next, jsonPointer.tail());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        String fullString = "/test/property1/index1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertEquals(fullString, jsonPointer.toString());
    }

    /**
     * Test hashCode method
     */
    @Test
    public void testHashCode() {
        String fullString1 = "/test/property1/index1";
        String fullString2 = "/test/property1/index2";
        JsonPointer jsonPointer1 = new JsonPointer(fullString1, "", EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "", EMPTY);
        assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    /**
     * Test equals method
     */
    @Test
    public void testEquals() {
        String fullString1 = "/test/property1/index1";
        String fullString2 = "/test/property1/index2";
        JsonPointer jsonPointer1 = new JsonPointer(fullString1, "", EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "", EMPTY);
        JsonPointer jsonPointer3 = new JsonPointer(fullString1, "", jsonPointer2);
        assertTrue(jsonPointer1.equals(jsonPointer1));
        assertFalse(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
        JsonPointer jsonPointer4 = new JsonPointer(fullString1, "", jsonPointer1);
        assertTrue(jsonPointer1.equals(jsonPointer4));
    }
}