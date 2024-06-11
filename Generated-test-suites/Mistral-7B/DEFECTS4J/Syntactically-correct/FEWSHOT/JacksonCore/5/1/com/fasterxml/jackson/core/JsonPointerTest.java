package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
        assertNull(empty.tail());
        assertEquals("/", empty.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testPropertyPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertTrue(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testElementPointer() {
        JsonPointer pointer = JsonPointer.compile("/list[0]");
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertNull(pointer.tail());
        assertEquals("/list[0]", pointer.toString());
    }

    @Test
    public void testComplexPointer() {
        JsonPointer pointer = JsonPointer.compile("/user/name[0]/friends[0]/posts[1]/comments[2]");
        assertTrue(pointer.matches());
        assertEquals("comments[2]", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/user/name[0]/friends[0]/posts[1]", tail.toString());
        assertNull(tail.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/baz");
        JsonPointer pointer3 = JsonPointer.compile("/foo");
        JsonPointer pointer4 = JsonPointer.compile("/foo/bar");

        assertNull(pointer1.matchProperty("baz"));
        assertNull(pointer1.matchProperty("bar"));
        assertSame(pointer3, pointer1.matchProperty("foo"));
        assertSame(pointer4, pointer1.matchProperty("/foo/bar"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = JsonPointer.compile("/list[0]");
        JsonPointer pointer2 = JsonPointer.compile("/list[1]");
        JsonPointer pointer3 = JsonPointer.compile("/list");

        assertNull(pointer1.matchElement(-1));
        assertNull(pointer1.matchElement(0));
        assertSame(pointer1, pointer1.matchElement(0));
        assertNull(pointer2.matchElement(0));
        assertNull(pointer3.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer pointer1 = JsonPointer.compile("/user/name[0]/friends[0]/posts[1]/comments[2]");
        JsonPointer pointer2 = JsonPointer.compile("/user/name[0]");

        JsonPointer tail1 = pointer1.tail();
        JsonPointer tail2 = pointer2.tail();

        assertEquals("/user/name[0]/friends[0]/posts[1]", tail1.toString());
        assertEquals("/user/name[0]", tail2.toString());
        assertNull(tail1.tail());
        assertNull(tail2.tail());
    }

    @Test
    public void testHashCodeEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/foo/baz");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }
}