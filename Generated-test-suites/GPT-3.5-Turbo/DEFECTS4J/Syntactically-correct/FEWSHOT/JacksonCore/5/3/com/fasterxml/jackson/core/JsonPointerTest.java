package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer1 = new JsonPointer();
        assertTrue(pointer1.matches());

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        assertFalse(pointer2.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer1 = new JsonPointer();
        assertEquals("", pointer1.getMatchingProperty());

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        assertEquals("foo", pointer2.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer1 = new JsonPointer();
        assertEquals(-1, pointer1.getMatchingIndex());

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        assertEquals(-1, pointer2.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer1 = new JsonPointer();
        assertTrue(pointer1.mayMatchProperty());

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        assertTrue(pointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer1 = new JsonPointer();
        assertFalse(pointer1.mayMatchElement());

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        assertFalse(pointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = new JsonPointer();
        assertNull(pointer1.matchProperty("foo"));

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        JsonPointer next1 = pointer2.matchProperty("foo");
        assertEquals(pointer1, next1);

        JsonPointer next2 = pointer2.matchProperty("baz");
        assertNull(next2);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = new JsonPointer();
        assertNull(pointer1.matchElement(1));

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        JsonPointer next1 = pointer2.matchElement(1);
        assertNull(next1);

        JsonPointer next2 = pointer2.matchElement(0);
        assertNull(next2);
    }

    @Test
    public void testTail() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer tail1 = pointer1.tail();
        assertNull(tail1);

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        JsonPointer tail2 = pointer2.tail();
        assertEquals(pointer1, tail2);
    }

    @Test
    public void testToString() {
        JsonPointer pointer1 = new JsonPointer();
        assertEquals("", pointer1.toString());

        JsonPointer pointer2 = new JsonPointer("foo", "bar", pointer1);
        assertEquals("/foo/bar", pointer2.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
    }
}