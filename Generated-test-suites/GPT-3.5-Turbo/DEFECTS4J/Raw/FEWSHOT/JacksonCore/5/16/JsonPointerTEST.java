package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testCompileWithNullInput() {
        JsonPointer pointer = JsonPointer.compile(null);
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("test"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("property");
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertFalse(pointer.matches());
        assertEquals("/property", pointer.toString());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer newPointer = pointer.matchProperty("property");
        assertEquals("", newPointer.toString());
        assertTrue(newPointer.matches());
        assertNull(newPointer.getMatchingProperty());
        assertEquals(-1, newPointer.getMatchingIndex());
        assertFalse(newPointer.mayMatchProperty());
        assertFalse(newPointer.mayMatchElement());
        assertNull(newPointer.matchProperty("property"));
        assertNull(newPointer.matchElement(0));
        assertNull(newPointer.tail());
    }

    @Test
    public void testCompileWithArrayElement() {
        JsonPointer pointer = JsonPointer.compile("/1");
        assertFalse(pointer.matches());
        assertEquals("/1", pointer.toString());
        assertNull(pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        JsonPointer newPointer = pointer.matchElement(1);
        assertEquals("", newPointer.toString());
        assertTrue(newPointer.matches());
        assertNull(newPointer.getMatchingProperty());
        assertEquals(-1, newPointer.getMatchingIndex());
        assertFalse(newPointer.mayMatchProperty());
        assertFalse(newPointer.mayMatchElement());
        assertNull(newPointer.matchProperty("property"));
        assertNull(newPointer.matchElement(0));
        assertNull(newPointer.tail());
    }

    @Test
    public void testCompileWithQuotedInput() {
        JsonPointer pointer = JsonPointer.compile("/properties~0~1$index");
        assertFalse(pointer.matches());
        assertEquals("/properties~0~1$index", pointer.toString());
        assertEquals("properties~/1$index", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer newPointer = pointer.matchProperty("properties~/1$index");
        assertEquals("", newPointer.toString());
        assertTrue(newPointer.matches());
        assertNull(newPointer.getMatchingProperty());
        assertEquals(-1, newPointer.getMatchingIndex());
        assertFalse(newPointer.mayMatchProperty());
        assertFalse(newPointer.mayMatchElement());
        assertNull(newPointer.matchProperty("property"));
        assertNull(newPointer.matchElement(0));
        assertNull(newPointer.tail());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/property1/property2");
        assertFalse(pointer.matches());
        assertEquals("/property1/property2", pointer.toString());
        assertEquals("property1", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer newPointer = pointer.tail();
        assertEquals("/property2", newPointer.toString());
        assertFalse(newPointer.matches());
        assertEquals("property2", newPointer.getMatchingProperty());
        assertEquals(-1, newPointer.getMatchingIndex());
        assertTrue(newPointer.mayMatchProperty());
        assertFalse(newPointer.mayMatchElement());
        assertNull(newPointer.matchProperty("property2"));
        assertNull(newPointer.matchElement(0));
        assertNull(newPointer.tail());
    }

}