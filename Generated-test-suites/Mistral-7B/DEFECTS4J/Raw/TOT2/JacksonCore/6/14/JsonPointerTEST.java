
package com.example.junit4; // Replace with your test package name

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
        assertEquals(empty, empty.tail());
        assertTrue(empty.matches());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
    }

    @Test
    public void testCompileNullInput() {
        try {
            JsonPointer pointer = JsonPointer.compile(null);
            fail("Expected IllegalArgumentException but got: " + pointer);
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        assertEquals(pointer, pointer.tail());
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
    }

    @Test
    public void testCompileInvalidInput() {
        try {
            JsonPointer pointer = JsonPointer.compile("abc");
            fail("Expected IllegalArgumentException but got: " + pointer);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"abc\"", e.getMessage());
        }
    }

    @Test
    public void testCompileSingleProperty() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertEquals("/property", pointer.toString());
        assertEquals(pointer, pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("property", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
    }

    @Test
    public void testCompileSingleIndex() {
        JsonPointer pointer = JsonPointer.compile("/array/0");
        assertEquals("/array/0", pointer.toString());
        assertEquals(pointer, pointer.tail());
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.getMatchingProperty());
    }

    @Test
    public void testCompileMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/array/0/object/property");
        assertEquals("/array/0/object/property", pointer.toString());
        assertEquals(pointer, pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("property", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
    }

    @Test
    public void testCompileEscapedSegments() {
        JsonPointer pointer = JsonPointer.compile("/array/0/object/~1property");
        assertEquals("/array/0/object/property", pointer.toString());
        assertEquals(pointer, pointer.tail());
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("property", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
    }

    @Test
    public void testMatches() {
        JsonPointer pointer1 = JsonPointer.compile("/array/0");
        JsonPointer pointer2 = JsonPointer.compile("/array/0");
        JsonPointer pointer3 = JsonPointer.compile("/array/1");

        assertTrue(pointer1.matches());
        assertTrue(pointer1.matches());
        assertFalse(pointer1.matches());
        assertFalse(pointer1.matches());

        assertEquals(pointer1, pointer1.tail());
        assertSame(pointer1, pointer1.tail());
        assertNull(pointer1.tail());

        assertFalse(pointer1.mayMatchProperty());
        assertFalse(pointer1.mayMatchElement());
        assertNull(pointer1.getMatchingProperty());
        assertEquals(0, pointer1.getMatchingIndex());

        assertTrue(pointer2.matches());
        assertTrue(pointer2.equals(pointer1));

        assertFalse(pointer1.equals(pointer3));

        assertEquals(pointer1.toString(), "/array/0");
        assertEquals(pointer2.toString(), "/array/0");
        assertEquals(pointer3.toString(), "/array/1");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = JsonPointer.compile("/array/0/object/property");
        JsonPointer pointer2 = JsonPointer.compile("/array/0");
        JsonPointer pointer3 = JsonPointer.compile("/array/0/object/property");

        assertNull(pointer1.matchProperty(null));
        assertNull(pointer1.matchProperty(""));
        assertNull(pointer1.matchProperty("property1"));

        assertEquals(pointer3, pointer1.matchProperty("property"));
        assertNull(pointer1.matchProperty("property1"));
        assertNull(pointer1.matchProperty("property2"));

        assertSame(pointer1, pointer1.tail());
        assertSame(pointer1, pointer1.tail());
        assertNull(pointer1.tail());

        assertEquals(3, pointer1.toString().split("/").length);
        assertEquals(3, pointer3.toString().split("/").length);
        assertEquals(1, pointer2.toString().split("/").length);

        assertFalse(pointer1.mayMatchProperty());
        assertFalse(pointer1.mayMatchElement());
        assertFalse(pointer2.mayMatchProperty());
        assertFalse(pointer2.mayMatchElement());
        assertTrue(pointer3.mayMatchProperty());
        assertTrue(pointer3.mayMatchElement());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = JsonPointer.compile("/array/0/0");
        JsonPointer pointer2 = JsonPointer.compile("/array/0/1");
        JsonPointer pointer3 = JsonPointer.compile("/array/1/0");

        assertNull(pointer1.matchElement(null));
        assertNull(pointer1.matchElement(-1));

        assertEquals(JsonPointer.EMPTY, pointer1.matchElement(0));
        assertEquals(JsonPointer.EMPTY, pointer2.matchElement(0));
        assertEquals(pointer3, pointer3.matchElement(0));

        assertSame(pointer1, pointer1.tail());
        assertSame(pointer1, pointer1.tail());
        assertNull(pointer1.tail());

        assertEquals(3, pointer1.toString().split("/").length);
        assertEquals(3, pointer3.toString().split("/").length);
        assertEquals(1, pointer2.toString().split("/").length);

        assertFalse(pointer1.mayMatchProperty());
        assertFalse(pointer1.mayMatchElement());
        assertFalse(pointer2.mayMatchProperty());
        assertFalse(pointer2.mayMatchElement());
        assertFalse(pointer3.mayMatchProperty());
        assertTrue(pointer3.mayMatchElement());
    }

    @Test
    public void testHashCodeEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/array/0");
        JsonPointer pointer2 = JsonPointer.compile("/array/0");
        JsonPointer pointer3 = JsonPointer.compile("/array/1");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertEquals(pointer1, pointer2);
        assertNotEquals(pointer1, pointer3);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testToString() {
        JsonPointer pointer1 = JsonPointer.compile("/array/0");
        JsonPointer pointer2 = JsonPointer.compile("/array/0");
        JsonPointer pointer3 = JsonPointer.compile("/array/1");

        assertEquals(pointer1.toString(), "/array/0");
        assertEquals(pointer2.toString(), "/array/0");
        assertEquals(pointer3.toString(), "/array/1");
    }

}