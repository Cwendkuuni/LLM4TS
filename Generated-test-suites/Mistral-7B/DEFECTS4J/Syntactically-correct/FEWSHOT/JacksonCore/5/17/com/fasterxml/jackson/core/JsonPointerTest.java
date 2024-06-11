package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertEquals("/", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithOneSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo");
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertEquals("/foo", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithOneElementIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchElement());
        assertFalse(jsonPointer.mayMatchProperty());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertEquals("/foo/0", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0/bar/1");
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertEquals("/foo/0/bar/1", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testJsonPointerWithNullInput() {
        try {
            JsonPointer.compile(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"null\"", e.getMessage());
        }
    }

    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0/bar/1");
        JsonPointer matchingPropertyJsonPointer = JsonPointer.compile("/foo/0/bar");
        JsonPointer notMatchingPropertyJsonPointer = JsonPointer.compile("/foo/0/baz");

        JsonPointer matchedJsonPointer = jsonPointer.matchProperty("bar");
        assertTrue(matchedJsonPointer != null);
        assertEquals("/foo/0/bar", matchedJsonPointer.toString());

        JsonPointer unmatchedJsonPointer = jsonPointer.matchProperty("baz");
        assertNull(unmatchedJsonPointer);

        JsonPointer matchingPropertyJsonPointerMatched = matchingPropertyJsonPointer.matchProperty("bar");
        assertTrue(matchingPropertyJsonPointerMatched != null);
        assertEquals("/foo/0/bar", matchingPropertyJsonPointerMatched.toString());

        JsonPointer unmatchedPropertyJsonPointer = matchingPropertyJsonPointer.matchProperty("baz");
        assertNull(unmatchedPropertyJsonPointer);

        JsonPointer notMatchingPropertyJsonPointerMatched = notMatchingPropertyJsonPointer.matchProperty("bar");
        assertNull(notMatchingPropertyJsonPointerMatched);
    }

    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0/bar/1");
        JsonPointer matchingElementJsonPointer = JsonPointer.compile("/foo/0/bar/1");
        JsonPointer notMatchingElementJsonPointer = JsonPointer.compile("/foo/0/bar/2");

        JsonPointer matchedJsonPointer = jsonPointer.matchElement(1);
        assertTrue(matchedJsonPointer != null);
        assertEquals("/foo/0/bar", matchedJsonPointer.toString());

        JsonPointer unmatchedJsonPointer = jsonPointer.matchElement(2);
        assertNull(unmatchedJsonPointer);

        JsonPointer matchingElementJsonPointerMatched = matchingElementJsonPointer.matchElement(1);
        assertTrue(matchingElementJsonPointerMatched != null);
        assertEquals("/foo/0/bar", matchingElementJsonPointerMatched.toString());

        JsonPointer unmatchedElementJsonPointer = matchingElementJsonPointer.matchElement(2);
        assertNull(unmatchedElementJsonPointer);

        JsonPointer notMatchingElementJsonPointerMatched = notMatchingElementJsonPointer.matchElement(1);
        assertNull(notMatchingElementJsonPointerMatched);
    }

    @Test
    public void testJsonPointerTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0/bar/1");
        JsonPointer tailJsonPointer = jsonPointer.tail();

        assertTrue(tailJsonPointer.matches());
        assertTrue(tailJsonPointer.mayMatchProperty());
        assertTrue(tailJsonPointer.mayMatchElement());
        assertNull(tailJsonPointer.getMatchingProperty());
        assertNull(tailJsonPointer.getMatchingIndex());
        assertEquals("/foo/0/bar", tailJsonPointer.toString());
    }
}