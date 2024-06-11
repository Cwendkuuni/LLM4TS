package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for {@link JsonPointer} class.
 */
public class JsonPointerTest {

    /**
     * Test for constructor with empty string input.
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    /**
     * Test for constructor with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructorInput() {
        new JsonPointer(null, "", null);
    }

    /**
     * Test for constructor with non-empty input.
     */
    @Test
    public void testNonEmptyConstructor() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/b/c", tail.toString());
    }

    /**
     * Test for equals method.
     */
    @Test
    public void testEquals() {
        String input1 = "/a/0/b/c";
        String input2 = "/a/0/b/c";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertTrue(jsonPointer1.equals(jsonPointer2));
        JsonPointer jsonPointer3 = new JsonPointer();
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    /**
     * Test for hashCode method.
     */
    @Test
    public void testHashCode() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        int hashCode = jsonPointer.hashCode();
        String input2 = "/d/1/e/f";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        JsonPointer jsonPointer3 = new JsonPointer();
        int hashCode2 = jsonPointer3.hashCode();
        assertEquals(hashCode, jsonPointer.hashCode());
        assertNotSame(hashCode, hashCode2);
    }

    /**
     * Test for toString method.
     */
    @Test
    public void testToString() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
    }

    /**
     * Test for matches method.
     */
    @Test
    public void testMatches() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());

        String input2 = "/a/0/b";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertTrue(jsonPointer2.matches());
    }

    /**
     * Test for getMatchingProperty method.
     */
    @Test
    public void testGetMatchingProperty() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("a", jsonPointer.getMatchingProperty());

        String input2 = "/a/1/b";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertEquals("a", jsonPointer2.getMatchingProperty());

        String input3 = "/a/0";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);
        assertEquals("a", jsonPointer3.getMatchingProperty());
    }

    /**
     * Test for getMatchingIndex method.
     */
    @Test
    public void testGetMatchingIndex() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(0, jsonPointer.getMatchingIndex());

        String input2 = "/a/1/b";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertEquals(1, jsonPointer2.getMatchingIndex());

        String input3 = "/a/0";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);
        assertEquals(-1, jsonPointer3.getMatchingIndex());
    }

    /**
     * Test for mayMatchProperty method.
     */
    @Test
    public void testMayMatchProperty() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.mayMatchProperty());

        String input2 = "/a/1/b";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertTrue(jsonPointer2.mayMatchProperty());

        String input3 = "/a/0";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);
        assertTrue(jsonPointer3.mayMatchProperty());

        JsonPointer jsonPointer4 = new JsonPointer();
        assertFalse(jsonPointer4.mayMatchProperty());
    }

    /**
     * Test for mayMatchElement method.
     */
    @Test
    public void testMayMatchElement() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.mayMatchElement());

        String input2 = "/a/1/b";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertFalse(jsonPointer2.mayMatchElement());

        String input3 = "/a/0";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);
        assertTrue(jsonPointer3.mayMatchElement());

        JsonPointer jsonPointer4 = new JsonPointer();
        assertFalse(jsonPointer4.mayMatchElement());
    }

    /**
     * Test for matchProperty method.
     */
    @Test
    public void testMatchProperty() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer matchProperty = JsonPointer.compile("/a/0");
        JsonPointer expectedJsonPointer = JsonPointer.compile("/b/c");
        assertEquals(expectedJsonPointer, jsonPointer.matchProperty("b"));

        JsonPointer jsonPointer2 = JsonPointer.compile("/a/1/b");
        assertNull(jsonPointer2.matchProperty("b"));

        JsonPointer jsonPointer3 = JsonPointer.compile("/a/0");
        assertEquals(jsonPointer3, jsonPointer3.matchProperty("a"));
    }

    /**
     * Test for matchElement method.
     */
    @Test
    public void testMatchElement() {
        String input = "/a/0/b/c/1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer matchElement = JsonPointer.compile("/a/0/b/c");
        JsonPointer expectedJsonPointer = JsonPointer.compile("/c/1");
        assertEquals(expectedJsonPointer, jsonPointer.matchElement(1));

        JsonPointer jsonPointer2 = JsonPointer.compile("/a/1/b/c");
        assertNull(jsonPointer2.matchElement(1));

        JsonPointer jsonPointer3 = JsonPointer.compile("/a/0/b/c");
        assertEquals(jsonPointer3, jsonPointer3.matchElement(0));
    }

    /**
     * Test for tail method.
     */
    @Test
    public void testTail() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer expectedJsonPointer = JsonPointer.compile("/b/c");
        assertEquals(expectedJsonPointer, jsonPointer.tail());

        String input2 = "/a";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertNull(jsonPointer2.tail());
    }

    /**
     * Test for compile factory method.
     */
    @Test
    public void testCompileFactoryMethod() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/a/0/b/c", jsonPointer.toString());

        String input2 = "/~1/a/b/~2/c";
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertEquals("/a/b/c", jsonPointer2.toString());

        String input3 = "";
        JsonPointer jsonPointer3 = JsonPointer.compile(input3);
        assertEquals("", jsonPointer3.toString());

        String input4 = null;
        try {
            JsonPointer jsonPointer4 = JsonPointer.compile(input4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for valueOf factory method.
     */
    @Test
    public void testValueOfFactoryMethod() {
        String input = "/a/0/b/c";
        JsonPointer jsonPointer = JsonPointer.valueOf(input);
        assertEquals("/a/0/b/c", jsonPointer.toString());
    }

    /**
     * Test for fromSegment factory method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFromSegmentFactoryMethod() {
        String[] segments = {"/a", "/b", "/c"};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        fail("Expected UnsupportedOperationException");
    }
}