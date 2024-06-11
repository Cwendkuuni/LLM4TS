
package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
    }

    @Test
    public void testCompileWithValidInput() {
        String input = "/abc";
        JsonPointer pointer = JsonPointer.compile(input);
        assertNotNull(pointer);
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        String input = "abc";
        JsonPointer.compile(input);
    }

    @Test
    public void testValueOf() {
        String input = "/abc";
        JsonPointer pointer = JsonPointer.valueOf(input);
        assertNotNull(pointer);
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        assertEquals("abc", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/123");
        assertEquals(123, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/123");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchPropertyWithMatchingName() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        JsonPointer next = pointer.matchProperty("abc");
        assertNotNull(next);
        assertEquals("", next.toString());
    }

    @Test
    public void testMatchPropertyWithNonMatchingName() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        JsonPointer next = pointer.matchProperty("def");
        assertNull(next);
    }

    @Test
    public void testMatchElementWithMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer next = pointer.matchElement(0);
        assertNotNull(next);
        assertEquals("", next.toString());
    }

    @Test
    public void testMatchElementWithNonMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer next = pointer.matchElement(1);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        assertEquals("def", pointer.tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        assertEquals("/abc/def", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/abc");
        JsonPointer pointer2 = JsonPointer.compile("/abc");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEqualsWithSameObject() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        assertTrue(pointer.equals(pointer));
    }

    @Test
    public void testEqualsWithNull() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        assertFalse(pointer.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        JsonPointer pointer = JsonPointer.compile("/abc");
        assertFalse(pointer.equals("abc"));
    }

    @Test
    public void testEqualsWithEqualPointers() {
        JsonPointer pointer1 = JsonPointer.compile("/abc");
        JsonPointer pointer2 = JsonPointer.compile("/abc");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testEqualsWithDifferentPointers() {
        JsonPointer pointer1 = JsonPointer.compile("/abc");
        JsonPointer pointer2 = JsonPointer.compile("/def");
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testPrivateMethods() {
        JsonPointer pointer = new JsonPointer();
        int index = pointer._parseIndex("123");
        assertEquals(123, index);
    }
}

