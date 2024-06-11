package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());

        pointer = new JsonPointer("test");
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());

        pointer = new JsonPointer("test");
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());

        pointer = new JsonPointer("test");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("0");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchProperty("test");
        assertNull(result);

        pointer = new JsonPointer("test");
        result = pointer.matchProperty("test");
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchElement(0);
        assertNull(result);

        pointer = new JsonPointer("0");
        result = pointer.matchElement(0);
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.tail();
        assertNotNull(result);
        assertEquals("", result.toString());

        pointer = new JsonPointer("test/0");
        result = pointer.tail();
        assertNotNull(result);
        assertEquals("0", result.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());

        pointer = new JsonPointer("test/0");
        assertEquals("test/0", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = new JsonPointer("test/0");
        pointer2 = new JsonPointer("test/0");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("test/0");
        pointer2 = new JsonPointer("test/0");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("test/0");
        pointer2 = new JsonPointer("test/1");
        assertFalse(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("test/0");
        pointer2 = new JsonPointer();
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNotNull(pointer);
        assertEquals("", pointer.toString());

        pointer = JsonPointer.compile("test");
        assertNotNull(pointer);
        assertEquals("test", pointer.toString());
    }
}