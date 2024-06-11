package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());

        pointer = new JsonPointer("test", null, null);
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());

        pointer = new JsonPointer("test", "property", null);
        assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("test", null, 3);
        assertEquals(3, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());

        pointer = new JsonPointer("test", "property", null);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("test", null, 3);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", null);
        JsonPointer next = pointer.matchProperty("property");
        assertNull(next);

        next = pointer.matchProperty("wrong");
        assertNull(next);

        pointer = new JsonPointer("test", null, null);
        next = pointer.matchProperty("property");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", null, 2);
        JsonPointer next = pointer.matchElement(2);
        assertNull(next);

        next = pointer.matchElement(3);
        assertNull(next);

        pointer = new JsonPointer("test", null, null);
        next = pointer.matchElement(2);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", null, null);
        JsonPointer next = pointer.tail();
        assertNull(next);

        pointer = new JsonPointer("test", "property", null);
        next = pointer.tail();
        assertNull(next);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/");
        assertTrue(pointer.matches());

        pointer = JsonPointer.compile("/test");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/3");
        assertFalse(pointer.matches());
        assertEquals(3, pointer.getMatchingIndex());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());

        pointer = new JsonPointer("test", "property", null);
        assertEquals("/property", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.hashCode() == pointer2.hashCode());

        pointer1 = new JsonPointer("test", "property", null);
        pointer2 = new JsonPointer("test", "property", null);
        assertTrue(pointer1.hashCode() == pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("test", "property", null);
        pointer2 = new JsonPointer("test", "property", null);
        assertTrue(pointer1.equals(pointer2));

        pointer1 = new JsonPointer("test", "property", null);
        assertFalse(pointer1.equals("test"));

        assertFalse(pointer1.equals(null));
    }
}