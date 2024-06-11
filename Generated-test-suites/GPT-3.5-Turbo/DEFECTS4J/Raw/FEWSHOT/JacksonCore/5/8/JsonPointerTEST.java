package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());

        pointer = JsonPointer.compile("/");
        assertEquals("", pointer.toString());

        pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.toString());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("foo/bar", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("foo");
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        assertEquals("", pointer.toString());

        pointer = JsonPointer.valueOf("/");
        assertEquals("", pointer.toString());

        pointer = JsonPointer.valueOf("/foo");
        assertEquals("foo", pointer.toString());

        pointer = JsonPointer.valueOf("/foo/bar");
        assertEquals("foo/bar", pointer.toString());
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());

        pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());

        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNull(pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/foo/bar");
        assertNull(pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/foo");
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("");
        assertFalse(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("");
        assertFalse(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNull(pointer.matchProperty("foo"));

        pointer = JsonPointer.compile("/foo");
        assertNull(pointer.matchProperty("bar"));
        assertEquals(pointer, pointer.matchProperty("foo"));

        pointer = JsonPointer.compile("/foo/bar");
        assertNull(pointer.matchProperty("foo"));
        assertNull(pointer.matchProperty("bar"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNull(pointer.matchElement(0));

        pointer = JsonPointer.compile("/0");
        assertNull(pointer.matchElement(1));
        assertEquals(pointer, pointer.matchElement(0));

        pointer = JsonPointer.compile("/0/1");
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.compile(""), pointer.tail());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals(JsonPointer.compile("bar"), pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());

        pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.toString());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("foo/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("");
        JsonPointer pointer2 = JsonPointer.compile("");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("");
        JsonPointer pointer2 = JsonPointer.compile("");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo/bar");
        pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));

        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(""));
    }

}