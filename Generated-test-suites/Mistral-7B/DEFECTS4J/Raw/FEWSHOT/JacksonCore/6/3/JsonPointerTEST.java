package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
    }

    @Test
    public void testConstructorWithEmptyStringAndNullNext() {
        JsonPointer pointer = new JsonPointer("", "", null);
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testConstructorWithFullStringAndNullNext() {
        String fullString = "/foo/bar/baz";
        JsonPointer pointer = new JsonPointer(fullString, "", null);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(fullString, pointer.toString());
    }

    @Test
    public void testConstructorWithFullStringAndNotNullNext() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer next = new JsonPointer("/baz", "", null);
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(next, pointer.tail());
        assertEquals(fullString, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithNoSlash() {
        JsonPointer.compile("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithQuotedElement() {
        JsonPointer.compile("\"foo\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithQuotedSegment() {
        JsonPointer.compile("/\"foo\"");
    }

    @Test
    public void testMatches() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer pointer1 = new JsonPointer(fullString, segment, null);
        String fullString2 = "/foo/bar/baz/quux";
        JsonPointer pointer2 = new JsonPointer(fullString2, "", null);
        JsonPointer pointer3 = new JsonPointer("/foo/quux", "", null);
        JsonPointer pointer4 = new JsonPointer("/foo/bar/quux", "", null);
        assertTrue(pointer1.matches());
        assertFalse(pointer2.matches());
        assertFalse(pointer3.matches());
        assertTrue(pointer4.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer pointer = new JsonPointer(fullString, segment, null);
        assertEquals("bar", pointer.getMatchingProperty());
        String fullString2 = "/foo/quux/quux";
        JsonPointer pointer2 = new JsonPointer(fullString2, "", null);
        assertNull(pointer2.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        String fullString = "/foo[0]/bar/baz[1]";
        JsonPointer pointer = new JsonPointer(fullString, "", null);
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer pointer2 = new JsonPointer("/foo[1]/bar/baz[0]", "", null);
        assertEquals(1, pointer2.getMatchingIndex());
        JsonPointer pointer3 = new JsonPointer("/foo[0]/quux", "", null);
        assertNull(pointer3.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        String fullString = "/foo/bar/baz";
        JsonPointer pointer = new JsonPointer(fullString, "bar", null);
        assertTrue(pointer.mayMatchProperty());
        JsonPointer pointer2 = new JsonPointer("/foo/quux", "", null);
        assertFalse(pointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        String fullString = "/foo[0]/bar/baz[1]";
        JsonPointer pointer = new JsonPointer(fullString, "", null);
        assertTrue(pointer.mayMatchElement());
        JsonPointer pointer2 = new JsonPointer("/foo/bar/quux", "", null);
        assertFalse(pointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        String fullString = "/foo/bar/baz";
        String matchingProperty = "bar";
        JsonPointer pointer = new JsonPointer(fullString, matchingProperty, null);
        JsonPointer matcher = new JsonPointer("bar", "", null);
        assertEquals(pointer, matcher.matchProperty(matchingProperty));
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchProperty("quux"));
    }

    @Test
    public void testMatchElement() {
        String fullString = "/foo[0]/bar/baz[1]";
        int matchingIndex = 1;
        JsonPointer pointer = new JsonPointer(fullString, "", null);
        JsonPointer matcher = new JsonPointer("", "", null);
        assertEquals(pointer, matcher.matchElement(matchingIndex));
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(2));
    }

    @Test
    public void testTail() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer pointer = new JsonPointer(fullString, segment, null);
        assertEquals(new JsonPointer("/baz", "", null), pointer.tail());
        JsonPointer pointer2 = new JsonPointer("/foo/quux", "", null);
        assertNull(pointer2.tail());
    }

    @Test
    public void testToString() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer pointer = new JsonPointer(fullString, segment, null);
        assertEquals(fullString, pointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String fullString1 = "/foo/bar/baz";
        String segment1 = "bar";
        JsonPointer pointer1 = new JsonPointer(fullString1, segment1, null);
        String fullString2 = "/foo/quux/quux";
        JsonPointer pointer2 = new JsonPointer(fullString2, "", null);
        String fullString3 = "/foo/bar/baz";
        JsonPointer pointer3 = new JsonPointer(fullString3, segment1, null);
        assertEquals(pointer1.hashCode(), pointer3.hashCode());
        assertTrue(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(pointer2));
    }
}