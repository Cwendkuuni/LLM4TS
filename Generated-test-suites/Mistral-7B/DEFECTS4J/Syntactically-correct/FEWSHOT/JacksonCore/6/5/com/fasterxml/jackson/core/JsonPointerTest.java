package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointerConstruction() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar/baz", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals("/bar/baz", pointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerInput() {
        String input = "foo";
        JsonPointer.compile(input); // this will throw
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        String matchingProperty = "bar";
        JsonPointer matchedPointer = pointer.matchProperty(matchingProperty);

        assertTrue(matchedPointer.matches());
        assertEquals("baz", matchedPointer.tail().getMatchingProperty());
        assertNull(matchedPointer.getMatchingIndex());
        assertEquals("/bar/baz", matchedPointer.toString());
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);

        int matchingIndex = 0;
        JsonPointer matchedPointer = pointer.matchElement(matchingIndex);

        assertTrue(matchedPointer.matches());
        assertEquals("bar", matchedPointer.getMatchingProperty());
        assertEquals(0, matchedPointer.getMatchingIndex());
        assertNull(matchedPointer.tail());
        assertEquals("/0/bar", matchedPointer.toString());
    }

    @Test
    public void testMatchElementNotFound() {
        String input = "/foo/1/bar";
        JsonPointer pointer = JsonPointer.compile(input);

        int matchingIndex = 0;
        JsonPointer matchedPointer = pointer.matchElement(matchingIndex);

        assertNull(matchedPointer);
    }

    @Test
    public void testMatchPropertyNotFound() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        String matchingProperty = "quux";
        JsonPointer matchedPointer = pointer.matchProperty(matchingProperty);

        assertNull(matchedPointer);
    }

    @Test
    public void testTail() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        JsonPointer tailPointer = pointer.tail();

        assertFalse(tailPointer.matches());
        assertEquals("baz", tailPointer.getMatchingProperty());
        assertNull(tailPointer.getMatchingIndex());
        assertEquals("/bar/baz", tailPointer.toString());
    }

    @Test
    public void testParseQuotedTail() {
        String input = "/foo/bar/~1~2~3/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar/baz", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals("/baz", pointer.tail().toString());
    }

    @Test
    public void testParseMultipleQuotedTail() {
        String input = "/foo/bar/~1~2~3/qux/~4~5~6/quux";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar/quux", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals("/quux", pointer.tail().toString());
    }

    @Test
    public void testParseMultipleSegments() {
        String input = "/foo/bar/1/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar/baz", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseArrayAsFirstSegment() {
        String input = "/1/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseEmptySegment() {
        String input = "/foo//bar";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseTrailingSlash() {
        String input = "/foo/bar/";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseMultipleSlashes() {
        String input = "/foo/bar//baz/";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals("/foo/bar/baz", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testMatchesEmptyPointer() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.compile("/");

        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertTrue(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesNonEmptyPointer() {
        String input = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = new JsonPointer("/foo/bar/baz");

        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertTrue(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesDifferentProperties() {
        String input1 = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/quux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesDifferentIndices() {
        String input1 = "/foo/0/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/1/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesDifferentTails() {
        String input1 = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/qux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSameTailDifferentProperties() {
        String input1 = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/baz";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSameTailSamePropertyDifferentIndices() {
        String input1 = "/foo/0/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/1/bar/baz";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSameTailSamePropertySameIndex() {
        String input1 = "/foo/0/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/0/bar/baz";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertTrue(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSameTailDifferentPropertiesDifferentIndices() {
        String input1 = "/foo/0/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/1/qux/quux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSamePropertyDifferentIndices() {
        String input1 = "/foo/bar/0";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/1";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSamePropertySameIndexDifferentTails() {
        String input1 = "/foo/bar/0/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/0/quuz";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSamePropertySameIndexSameTails() {
        String input1 = "/foo/bar/0/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/0/qux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertTrue(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSameTailDifferentPropertiesSameIndices() {
        String input1 = "/foo/bar/0/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/0/qux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesSameTailDifferentPropertiesDifferentIndices() {
        String input1 = "/foo/bar/0/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/1/quux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesDifferentTailsDifferentProperties() {
        String input1 = "/foo/bar/1/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/0/quux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesDifferentTailsSameProperties() {
        String input1 = "/foo/bar/1/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar/2/qux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesEmptyPointerDifferent() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        String input2 = "/foo/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesEmptyPointerSame() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;

        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertTrue(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesEmptyPointerSameNull() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = null;

        assertNull(pointer2);
        assertFalse(pointer1.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testMatchesEmptyPointerNull() {
        JsonPointer pointer1 = null;
        JsonPointer pointer2 = JsonPointer.EMPTY;

        assertNull(pointer1);
        assertFalse(pointer2.matches());
        assertFalse(pointer1.matches(pointer2));
    }

    @Test
    public void testToString() {
        String input = "/foo/bar/0/qux";
        JsonPointer pointer = JsonPointer.compile(input);

        assertEquals(input, pointer.toString());
    }

    @Test
    public void testHashCodeEquals() {
        String input = "/foo/bar/0/qux";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = JsonPointer.compile(input);

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testHashCodeNotEquals() {
        String input1 = "/foo/bar/0/qux";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/qux/0/quux";
        JsonPointer pointer2 = JsonPointer.compile(input2);

        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testHashCodeEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;

        assertEquals(0, pointer.hashCode());
        assertTrue(pointer.equals(JsonPointer.EMPTY));
    }

    @Test
    public void testHashCodeNullPointer() {
        JsonPointer pointer = null;

        assertEquals(0, pointer.hashCode());
        assertFalse(pointer.equals(null));
    }
}