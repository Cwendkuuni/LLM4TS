package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty instance.
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertEquals("", empty.getMatchingProperty());
        assertEquals(-1, empty.getMatchingIndex());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
    }

    /**
     * Test constructor for non-empty instance.
     */
    @Test
    public void testNonEmptyConstructor() {
        String input = "/a/0/b/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    /**
     * Test compile method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        String input = "abcd";
        JsonPointer.compile(input);
    }

    /**
     * Test mayMatchProperty method.
     */
    @Test
    public void testMayMatchProperty() {
        String input = "/a/0/b/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.mayMatchProperty());
        assertEquals("/a", pointer.toString().substring(0, pointer.toString().lastIndexOf('/')));
    }

    /**
     * Test mayMatchElement method.
     */
    @Test
    public void testMayMatchElement() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
    }

    /**
     * Test matchProperty method.
     */
    @Test
    public void testMatchProperty() {
        String input = "/a/0/b/1";
        JsonPointer pointer = JsonPointer.compile(input);
        String property = "b";
        JsonPointer result = pointer.matchProperty(property);
        assertTrue(result != null);
        assertEquals(property, result.getMatchingProperty());
    }

    /**
     * Test matchProperty method with non-matching property.
     */
    @Test
    public void testMatchPropertyNonMatching() {
        String input = "/a/0/b/1";
        JsonPointer pointer = JsonPointer.compile(input);
        String property = "c";
        JsonPointer result = pointer.matchProperty(property);
        assertNull(result);
    }

    /**
     * Test matchElement method.
     */
    @Test
    public void testMatchElement() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        int index = 0;
        JsonPointer result = pointer.matchElement(index);
        assertTrue(result != null);
        assertEquals(index, result.getMatchingIndex());
    }

    /**
     * Test matchElement method with non-matching index.
     */
    @Test
    public void testMatchElementNonMatching() {
        String input = "/a/0";
        JsonPointer pointer = JsonPointer.compile(input);
        int index = 1;
        JsonPointer result = pointer.matchElement(index);
        assertNull(result);
    }

    /**
     * Test tail method.
     */
    @Test
    public void testTail() {
        String input = "/a/0/b/1";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer result = pointer.tail();
        assertFalse(result.matches());
        assertEquals("/b/1", result.toString());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String input = "/a/0/b/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
    }

    /**
     * Test hashCode and equals methods.
     */
    @Test
    public void testHashCodeAndEquals() {
        String input1 = "/a/0/b/1";
        String input2 = "/a/0/b/1";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(input1, pointer1.toString());
        assertEquals(input2, pointer2.toString());
        assertEquals(pointer1, pointer2);
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(empty.equals(pointer1));
    }
}