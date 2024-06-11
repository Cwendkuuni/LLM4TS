package com.fasterxml.jackson.core;

import org.junit.Test;
import org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertNotNull(pointer);
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test");
        JsonPointer result = pointer.matchProperty("test");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        JsonPointer result = pointer.matchElement(0);
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/test");
        JsonPointer result = pointer.tail();
        assertNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/test");
        String result = pointer.toString();
        assertEquals("/test", result);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = JsonPointer.compile("/test");
        int hashCode = pointer.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer = JsonPointer.compile("/test");
        boolean result = pointer.equals("/test");
        assertFalse(result);
    }
}