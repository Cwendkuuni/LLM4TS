package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer.
 */
public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testConstructorWithFullStringAndSegmentWithNext() throws IllegalArgumentException {
        String fullString = "/a/b/c";
        String segment = "b";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, new JsonPointer("/d/e"));
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidInput() throws IllegalArgumentException {
        String input = "123";
        JsonPointer.compile(input);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCompileMethod() throws IllegalArgumentException {
        String input = "/a/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testValueOfMethod() throws IllegalArgumentException {
        String input = "/a/b/c";
        JsonPointer jsonPointer = JsonPointer.valueOf(input);
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(input, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromSegmentWithNullOrEmptySegmentsArray() {
        String[] segments = {};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromSegmentWithInvalidSegment() {
        String[] segments = {"/a", "~"};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFromSegmentWithValidSegments() {
        String[] segments = {"/a", "b"};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/a/b", jsonPointer.toString());
    }

    @Test
    public void testMatchesMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/b");
        assertTrue(jsonPointer.matches());
        jsonPointer = new JsonPointer("/a/b/c");
        assertFalse(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingPropertyMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/b");
        assertEquals("b", jsonPointer.getMatchingProperty());
        jsonPointer = new JsonPointer("/a/c");
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndexMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/0");
        assertEquals(0, jsonPointer.getMatchingIndex());
        jsonPointer = new JsonPointer("/a/1");
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchPropertyMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/b");
        assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = new JsonPointer("/a");
        assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = new JsonPointer("");
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElementMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/0");
        assertTrue(jsonPointer.mayMatchElement());
        jsonPointer = new JsonPointer("/a");
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchPropertyMethod() {
        JsonPointer base = new JsonPointer("/a/b");
        JsonPointer jsonPointer = base.matchProperty("b");
        assertEquals(base, jsonPointer);
        jsonPointer = base.matchProperty("c");
        assertNull(jsonPointer);
    }

    @Test
    public void testMatchElementMethod() {
        JsonPointer base = new JsonPointer("/a/0");
        JsonPointer jsonPointer = base.matchElement(0);
        assertEquals(base, jsonPointer);
        jsonPointer = base.matchElement(1);
        assertNull(jsonPointer);
    }

    @Test
    public void testTailMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/b");
        assertEquals(new JsonPointer("/b"), jsonPointer.tail());
        jsonPointer = new JsonPointer("/a");
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testToStringMethod() {
        JsonPointer jsonPointer = new JsonPointer("/a/b");
        assertEquals("/a/b", jsonPointer.toString());
        jsonPointer = new JsonPointer("/a");
        assertEquals("/a", jsonPointer.toString());
        jsonPointer = new JsonPointer("");
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testEqualsMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/a/b");
        JsonPointer jsonPointer2 = new JsonPointer("/a/b");
        JsonPointer jsonPointer3 = new JsonPointer("/a/c");
        JsonPointer jsonPointer4 = new JsonPointer("/a");
        JsonPointer jsonPointer5 = new JsonPointer("");
        JsonPointer jsonPointer6 = jsonPointer1;

        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertTrue(jsonPointer1.equals(jsonPointer6));
        assertFalse(jsonPointer1.equals(jsonPointer3));
        assertFalse(jsonPointer1.equals(jsonPointer4));
        assertFalse(jsonPointer1.equals(jsonPointer5));
    }

    @Test
    public void testHashCodeMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/a/b");
        JsonPointer jsonPointer2 = new JsonPointer("/a/b");
        JsonPointer jsonPointer3 = new JsonPointer("/a/c");
        JsonPointer jsonPointer4 = new JsonPointer("/a");
        JsonPointer jsonPointer5 = new JsonPointer("");

        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        assertEquals(jsonPointer1.hashCode(), jsonPointer6.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer4.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer5.hashCode());
    }
}