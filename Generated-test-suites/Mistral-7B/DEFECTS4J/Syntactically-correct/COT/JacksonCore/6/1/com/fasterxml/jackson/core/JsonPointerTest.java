package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test cases for JsonPointer class.
 */
public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testStringConstructor() throws IllegalArgumentException {
        String input = "/foo/bar/baz";
        JsonPointer pointer = new JsonPointer(input, "foo", null);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());

        pointer = new JsonPointer(input, "bar", pointer);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
        assertEquals("/foo/bar", pointer.toString());

        pointer = new JsonPointer(input, "baz", pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("baz", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/bar/baz", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws IllegalArgumentException {
        String input = "invalid";
        JsonPointer.compile(input);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCompileMethod() throws IllegalArgumentException {
        String input1 = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        assertEquals(input1, pointer1.toString());

        String input2 = "/foo";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(input2, pointer2.toString());

        String input3 = "/";
        JsonPointer pointer3 = JsonPointer.compile(input3);
        assertEquals(input3, pointer3.toString());

        String input4 = "/~1";
        JsonPointer pointer4 = JsonPointer.compile(input4);
        assertEquals("/~1", pointer4.toString());

        String input5 = "/~1/~2";
        JsonPointer pointer5 = JsonPointer.compile(input5);
        assertEquals("/~1/~2", pointer5.toString());

        String input6 = "/~1/~2/~3";
        JsonPointer pointer6 = JsonPointer.compile(input6);
        assertEquals("/~1/~2/~3", pointer6.toString());
    }

    @Test
    public void testMatchMethods() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);

        JsonPointer propertyPointer = new JsonPointer("foo", pointer);
        assertFalse(propertyPointer.matches());
        assertEquals("foo", propertyPointer.getMatchingProperty());
        assertEquals(-1, propertyPointer.getMatchingIndex());
        assertNotNull(propertyPointer.tail());
        assertEquals("/foo", propertyPointer.toString());

        JsonPointer elementPointer = new JsonPointer("0", propertyPointer);
        assertFalse(elementPointer.matches());
        assertEquals("0", elementPointer.getMatchingProperty());
        assertEquals(0, elementPointer.getMatchingIndex());
        assertNull(elementPointer.tail());
        assertEquals("/foo/0", elementPointer.toString());

        JsonPointer emptyPointer = new JsonPointer(pointer.toString());
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testEqualsHashcodeToString() {
        String input = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = JsonPointer.compile(input);

        assertEquals(pointer1, pointer2);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertEquals(pointer1.toString(), pointer2.toString());

        input = "/foo/bar/quux";
        JsonPointer pointer3 = JsonPointer.compile(input);
        assertNotEquals(pointer1, pointer3);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
        assertNotEquals(pointer1.toString(), pointer3.toString());

        pointer3 = new JsonPointer("foo", pointer1);
        assertEquals(pointer1, pointer3);
        assertEquals(pointer1.hashCode(), pointer3.hashCode());
        assertEquals(pointer1.toString(), pointer3.toString());

        pointer3 = new JsonPointer("bar", pointer3);
        assertEquals(pointer1, pointer3);
        assertEquals(pointer1.hashCode(), pointer3.hashCode());
        assertEquals(pointer1.toString(), pointer3.toString());

        pointer3 = new JsonPointer("0", pointer3);
        assertEquals(pointer1, pointer3);
        assertEquals(pointer1.hashCode(), pointer3.hashCode());
        assertEquals(pointer1.toString(), pointer3.toString());
    }
}