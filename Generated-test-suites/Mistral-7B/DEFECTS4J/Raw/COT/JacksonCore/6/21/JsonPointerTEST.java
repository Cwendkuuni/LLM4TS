package com.fasterxml.jackson.core.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

/**
 * Unit tests for {@link JsonPointer} class.
 */
public class JsonPointerTest {

    /**
     * Test constructor with empty string as input.
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    /**
     * Test constructor with valid JSON Pointer string as input.
     */
    @Test
    public void testValidConstructor() throws IllegalArgumentException {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar/1", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    /**
     * Test constructor with invalid JSON Pointer string as input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() throws IllegalArgumentException {
        String input = "baz";
        JsonPointer.compile(input);
    }

    /**
     * Test {@link JsonPointer#matches()} method.
     */
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        assertTrue(pointer.matches());

        pointer = new JsonPointer("/foo/bar/2");
        assertFalse(pointer.matches());

        pointer = new JsonPointer("/foo");
        assertTrue(pointer.matches());
    }

    /**
     * Test {@link JsonPointer#getMatchingProperty()} method.
     */
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        assertEquals("bar", pointer.getMatchingProperty());

        pointer = new JsonPointer("/foo/baz");
        assertNull(pointer.getMatchingProperty());

        pointer = new JsonPointer("/foo");
        assertEquals("", pointer.getMatchingProperty());
    }

    /**
     * Test {@link JsonPointer#getMatchingIndex()} method.
     */
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        assertEquals(1, pointer.getMatchingIndex());

        pointer = new JsonPointer("/foo/bar/0");
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.getMatchingIndex() >= 0);

        pointer = new JsonPointer("/foo");
        assertFalse(pointer.mayMatchElement());
        assertFalse(pointer.getMatchingIndex() >= 0);
    }

    /**
     * Test {@link JsonPointer#mayMatchProperty()} method.
     */
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        assertTrue(pointer.mayMatchProperty());

        pointer = new JsonPointer("/foo");
        assertTrue(pointer.mayMatchProperty());

        pointer = new JsonPointer("");
        assertFalse(pointer.mayMatchProperty());
    }

    /**
     * Test {@link JsonPointer#mayMatchElement()} method.
     */
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        assertTrue(pointer.mayMatchElement());

        pointer = new JsonPointer("/foo");
        assertFalse(pointer.mayMatchElement());
    }

    /**
     * Test {@link JsonPointer#matchProperty(String)} method.
     */
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        JsonPointer expected = new JsonPointer("/foo/bar");
        JsonPointer result = pointer.matchProperty("bar");
        assertEquals(expected.toString(), result.toString());

        pointer = new JsonPointer("/foo");
        assertNull(pointer.matchProperty("bar"));
    }

    /**
     * Test {@link JsonPointer#matchElement(int)} method.
     */
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        JsonPointer expected = new JsonPointer("/foo/bar");
        JsonPointer result = pointer.matchElement(1);
        assertEquals(expected.toString(), result.toString());

        pointer = new JsonPointer("/foo");
        assertNull(pointer.matchElement(1));
    }

    /**
     * Test {@link JsonPointer#toString()} method.
     */
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/foo/bar/1");
        assertEquals("/foo/bar/1", pointer.toString());

        pointer = new JsonPointer("/foo");
        assertEquals("/foo", pointer.toString());

        pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
    }

    /**
     * Test {@link JsonPointer#hashCode()} method.
     */
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer pointer2 = new JsonPointer("/foo/bar/1");
        JsonPointer pointer3 = new JsonPointer("/foo/baz/1");
        JsonPointer pointer4 = JsonPointer.EMPTY;

        int hash1 = pointer1.hashCode();
        int hash2 = pointer2.hashCode();
        int hash3 = pointer3.hashCode();
        int hash4 = pointer4.hashCode();

        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertEquals(hash4, hash4);
    }

    /**
     * Test {@link JsonPointer#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer pointer2 = new JsonPointer("/foo/bar/1");
        JsonPointer pointer3 = new JsonPointer("/foo/baz/1");
        JsonPointer pointer4 = JsonPointer.EMPTY;

        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(pointer4));
    }

    /**
     * Test {@link JsonPointer#compile(String)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() throws IllegalArgumentException {
        JsonPointer.compile("baz");
    }

    /**
     * Test {@link JsonPointer#compile(String)} method with empty input.
     */
    @Test
    public void testCompileWithEmptyInput() throws IllegalArgumentException {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    /**
     * Test {@link JsonPointer#compile(String)} method with valid input.
     */
    @Test
    public void testCompileWithValidInput() throws IllegalArgumentException {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar/1", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    /**
     * Test {@link JsonPointer#valueOf(String)} method.
     */
    @Test
    public void testValueOfWithValidInput() throws IllegalArgumentException {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.valueOf(input);
        assertEquals("/foo/bar/1", pointer.toString());
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    /**
     * Test {@link JsonPointer#valueOf(String)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithInvalidInput() throws IllegalArgumentException {
        JsonPointer.valueOf("baz");
    }
}