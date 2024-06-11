
package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.matches());

        pointer = JsonPointer.compile("/");
        assertTrue(pointer.matches());

        pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("foo", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/");
        assertNull(pointer.getMatchingProperty());

        pointer = JsonPointer.compile("");
        assertNull(pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(0, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/");
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("");
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertTrue(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/");
        assertTrue(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("");
        assertFalse(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertFalse(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/0");
        assertTrue(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/");
        assertFalse(pointer.mayMatchElement());

        pointer = JsonPointer.compile("");
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer next = pointer.matchProperty("foo");
        assertEquals("bar", next.getMatchingProperty());

        next = pointer.matchProperty("baz");
        assertNull(next);

        pointer = JsonPointer.compile("/");
        next = pointer.matchProperty("");
        assertNull(next);

        next = pointer.matchProperty("foo");
        assertNull(next);

        pointer = JsonPointer.compile("");
        next = pointer.matchProperty("");
        assertNull(next);

        next = pointer.matchProperty("foo");
        assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer next = pointer.matchElement(0);
        assertEquals(-1, next.getMatchingIndex());

        next = pointer.matchElement(1);
        assertNull(next);

        pointer = JsonPointer.compile("/");
        next = pointer.matchElement(0);
        assertNull(next);

        pointer = JsonPointer.compile("");
        next = pointer.matchElement(0);
        assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/baz", tail.toString());

        pointer = JsonPointer.compile("/");
        tail = pointer.tail();
        assertNull(tail);

        pointer = JsonPointer.compile("");
        tail = pointer.tail();
        assertNull(tail);
    }

    @Test
    public void testCompile() {
        assertCompile("/foo/bar", "foo", "bar");
        assertCompile("/foo", "foo");
        assertCompile("/", "");
        assertCompile("", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_InvalidInput() {
        JsonPointer.compile("foo/bar");
    }

    @Test
    public void testValueOf() {
        assertCompile("/foo/bar", "foo", "bar");
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());

        pointer = JsonPointer.compile("/");
        assertEquals("/", pointer.toString());

        pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals(pointer.toString().hashCode(), pointer.hashCode());

        pointer = JsonPointer.compile("/");
        assertEquals(pointer.toString().hashCode(), pointer.hashCode());

        pointer = JsonPointer.compile("");
        assertEquals(pointer.toString().hashCode(), pointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/");
        pointer2 = JsonPointer.compile("/");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("");
        pointer2 = JsonPointer.compile("");
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo/bar");
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(new Object()));
    }

    private void assertCompile(String input, String... segments) {
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer current = pointer;
        for (String segment : segments) {
            assertEquals(segment, current.getMatchingProperty());
            current = current.tail();
        }
        assertFalse(current.matches());
    }
}
