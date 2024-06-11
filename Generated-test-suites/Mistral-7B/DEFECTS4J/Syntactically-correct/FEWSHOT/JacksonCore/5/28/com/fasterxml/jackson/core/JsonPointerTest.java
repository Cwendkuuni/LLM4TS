package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer actual = JsonPointer.EMPTY;
        JsonPointer expected = new JsonPointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructFromSegments() {
        String[] segments = {"/a/0", "/b/1"};
        JsonPointer actual = JsonPointer.fromSegment(segments);
        String expectedString = "/a/0/b/1";
        JsonPointer expected = new JsonPointer(expectedString, segments[0], segments[1]);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatches() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
    }

    @Test
    public void testMismatchedProperties() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matchesProperty("/b/0"));
    }

    @Test
    public void testMatchedProperty() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/a", pointer.getMatchingProperty());
    }

    @Test
    public void testMatchedIndex() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        String input = "/a/0";
        String property = "a";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer expected = JsonPointer.compile("/a");
        assertEquals(expected, pointer.matchProperty(property));
    }

    @Test
    public void testMatchElement() {
        String input = "/a/0";
        int index = 0;
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer expected = JsonPointer.compile("/a");
        assertEquals(expected, pointer.matchElement(index));
    }

    @Test
    public void testTail() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testHashCodeAndEquals() {
        String input1 = "/a/0";
        String input2 = "/a/0";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer1, pointer2);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        String input3 = "/a/1";
        JsonPointer pointer3 = JsonPointer.compile(input3);
        assertNotEquals(pointer1, pointer3);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testParseIndex() {
        int index0 = JsonPointer._parseIndex("0");
        int index1 = JsonPointer._parseIndex("123456789");
        int index2 = JsonPointer._parseIndex("abcde");
        int index3 = JsonPointer._parseIndex("12345678901234567890");
        int index4 = JsonPointer._parseIndex("123456789012345678901234567890");
        assertEquals(0, index0);
        assertEquals(123456789, index1);
        assertEquals(-1, index2);
        assertEquals(-1, index3);
        assertEquals(-1, index4);
    }

    @Test
    public void testParseTail() {
        String input1 = "/a/0";
        String input2 = "/a/~0/b";
        JsonPointer expected1 = new JsonPointer("/a/0", "", JsonPointer.EMPTY);
        JsonPointer expected2 = new JsonPointer("/a/0", "~0", new JsonPointer("/b", "", JsonPointer.EMPTY));
        JsonPointer actual1 = JsonPointer._parseTail(input1);
        JsonPointer actual2 = JsonPointer._parseTail(input2);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testParseQuotedTail() {
        String input = "/a/~0~/b";
        JsonPointer expected = new JsonPointer("/a/0/b", "", JsonPointer.EMPTY);
        JsonPointer actual = JsonPointer._parseQuotedTail(input, 1);
        assertEquals(expected, actual);
    }
}