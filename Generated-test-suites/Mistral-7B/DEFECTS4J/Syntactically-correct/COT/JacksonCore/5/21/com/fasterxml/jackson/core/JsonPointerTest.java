package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer with JUnit 4.
 */
public class JsonPointerTest {

    // Testing constructor and factory methods

    @Test
    public void testEmptyConstructor() {
        JsonPointer empty = new JsonPointer();
        assertTrue(empty.matches());
        assertEquals("", empty.toString());
        assertNull(empty._nextSegment);
    }

    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer empty = JsonPointer.compile("");
        assertTrue(empty.matches());
        assertEquals("", empty.toString());
        assertNull(empty._nextSegment);
    }

    @Test
    public void testCompileWithValidJsonPointer() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertFalse(jsonPointer.matches());
        assertEquals(jsonPointerInput, jsonPointer.toString());
        JsonPointer nextSegment = jsonPointer._nextSegment;
        assertEquals("/foo", nextSegment.toString());
        assertEquals("foo", nextSegment.getMatchingProperty());
        assertEquals(0, nextSegment.getMatchingIndex());
    }

    // Testing methods

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer.compile("baz"); // should throw IllegalArgumentException
    }

    @Test
    public void testMatches() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertFalse(jsonPointer.matches());

        JsonPointer matchingJsonPointer = new JsonPointer(jsonPointerInput, jsonPointerInput, null);
        assertTrue(matchingJsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertEquals("foo", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertEquals(0, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        JsonPointer matchingJsonPointer = new JsonPointer("/foo/0", jsonPointerInput, null);
        JsonPointer matchedJsonPointer = jsonPointer.matchProperty("foo");
        assertEquals(matchingJsonPointer, matchedJsonPointer);
    }

    @Test
    public void testMatchElement() {
        String jsonPointerInput = "/foo/1/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        JsonPointer matchingJsonPointer = new JsonPointer("/foo/1", jsonPointerInput, null);
        JsonPointer matchedJsonPointer = jsonPointer.matchElement(1);
        assertEquals(matchingJsonPointer, matchedJsonPointer);
    }

    @Test
    public void testTail() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        JsonPointer tail = jsonPointer.tail();
        assertFalse(tail.matches());
        assertEquals("/foo/0", tail.toString());
    }

    // Testing equals, hashCode, toString methods

    @Test
    public void testEquals() {
        String jsonPointerInput1 = "/foo/0/bar";
        String jsonPointerInput2 = "/foo/0/bar";
        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerInput1);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerInput2);
        assertTrue(jsonPointer1.equals(jsonPointer2));
        JsonPointer jsonPointer3 = new JsonPointer("/foo/1/bar", jsonPointerInput1, null);
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    @Test
    public void testHashCode() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerInput);
        JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerInput);
        int hashCode1 = jsonPointer1.hashCode();
        int hashCode2 = jsonPointer2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        String jsonPointerInput = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerInput);
        assertEquals(jsonPointerInput, jsonPointer.toString());
    }

    // Testing exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidJsonPointer1() {
        JsonPointer.compile("baz/"); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidJsonPointer2() {
        JsonPointer.compile("foo/bar"); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidJsonPointer3() {
        JsonPointer.compile("foo/0bar"); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidJsonPointer4() {
        JsonPointer.compile("/foo/-1/bar"); // should throw IllegalArgumentException
    }
}