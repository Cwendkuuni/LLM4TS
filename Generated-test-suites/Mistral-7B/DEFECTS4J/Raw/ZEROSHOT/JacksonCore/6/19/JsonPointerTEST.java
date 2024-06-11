package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testBasicPointer() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/property", tail.toString());
        assertEquals("/test/property", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "test";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchProperty() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchProperty = pointer.matchProperty("property");
        assertEquals("/test", matchProperty.toString());
    }

    @Test
    public void testMatchElement() {
        String input = "/test/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchElement = pointer.matchElement(0);
        assertEquals("/test", matchElement.toString());
    }

    @Test
    public void testTail() {
        String input = "/test/property/child";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/property/child", tail.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String input1 = "/test/property";
        String input2 = "/test/property";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testDifferentObjects() {
        String input1 = "/test/property";
        String input2 = "/test/child";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testFactoryMethod() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        JsonPointer pointerFromSegments = JsonPointer.fromSegment("test", "property");
        assertEquals(pointerFromSegments, tail);
    }
}