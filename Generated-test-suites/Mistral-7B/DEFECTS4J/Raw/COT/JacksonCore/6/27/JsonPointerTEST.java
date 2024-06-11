package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * JUnit test file for JsonPointer class.
 */
public class JsonPointerTest {

    // Test constructor for empty instance
    @Test
    public void testEmptyConstructor() {
        JsonPointer empty = new JsonPointer();
        assertEquals("", empty.toString());
        assertNull(empty.tail());
        assertSame(empty, empty.matches());
    }

    // Test constructor for non-empty instance
    @Test
    public void testNonEmptyConstructor() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer next = new JsonPointer("/foo/quux");
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        assertEquals(fullString, pointer.toString());
        assertSame(next, pointer.tail());
        assertSame(segment, pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
    }

    // Test compile method with valid input
    @Test
    public void testCompileValidInput() throws Exception {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertSame(EMPTY, pointer.tail());
        assertSame("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    // Test compile method with invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() throws Exception {
        String input = "quux";
        JsonPointer.compile(input);
    }

    // Test factory method fromSegment
    // TODO: Implement this method and write test case

    // Test matches method
    @Test
    public void testMatches() {
        JsonPointer pointer1 = new JsonPointer("/foo");
        JsonPointer pointer2 = new JsonPointer("/foo");
        JsonPointer pointer3 = new JsonPointer("/bar");

        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertFalse(pointer3.matches());
    }

    // Test getMatchingProperty method
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        assertEquals("bar", pointer.getMatchingProperty());
    }

    // Test getMatchingIndex method
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/foo/0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    // Test matchProperty method with matching property
    @Test
    public void testMatchPropertyWithMatchingProperty() {
        String propertyName = "bar";
        JsonPointer pointer1 = new JsonPointer("/foo/bar");
        JsonPointer pointer2 = new JsonPointer("/foo/" + propertyName);
        JsonPointer match = pointer1.matchProperty(propertyName);
        assertSame(pointer2, match);
    }

    // Test matchProperty method with non-matching property
    @Test
    public void testMatchPropertyWithNonMatchingProperty() {
        String propertyName = "quux";
        JsonPointer pointer = new JsonPointer("/foo/bar");
        assertNull(pointer.matchProperty(propertyName));
    }

    // Test matchElement method with matching index
    @Test
    public void testMatchElementWithMatchingIndex() {
        int index = 0;
        JsonPointer pointer = new JsonPointer("/foo/0");
        JsonPointer match = pointer.matchElement(index);
        assertSame(pointer, match);
    }

    // Test matchElement method with non-matching index
    @Test
    public void testMatchElementWithNonMatchingIndex() {
        int index = 1;
        JsonPointer pointer = new JsonPointer("/foo/0");
        assertNull(pointer.matchElement(index));
    }

    // Test toString method
    @Test
    public void testToString() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = new JsonPointer(input);
        assertEquals(input, pointer.toString());
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        String input1 = "/foo/bar/baz";
        String input2 = "/foo/quux/quux";
        JsonPointer pointer1 = new JsonPointer(input1);
        JsonPointer pointer2 = new JsonPointer(input2);
        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    // Test equals method
    @Test
    public void testEquals() {
        String input1 = "/foo/bar/baz";
        String input2 = "/foo/bar/baz";
        String input3 = "/foo/quux/quux";
        JsonPointer pointer1 = new JsonPointer(input1);
        JsonPointer pointer2 = new JsonPointer(input1);
        JsonPointer pointer3 = new JsonPointer(input2);
        JsonPointer pointer4 = new JsonPointer(input3);
        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(pointer4));
    }
}