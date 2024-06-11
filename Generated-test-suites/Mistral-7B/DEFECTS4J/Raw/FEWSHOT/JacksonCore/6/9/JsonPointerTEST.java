package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmpty() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testBasic() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("/qux"));
        assertNull(pointer.matchElement(0));
        assertEquals(pointer.tail(), new JsonPointer("/bar/baz", "", null));
    }

    @Test
    public void testEmptyTail() {
        String input = "/foo/bar/";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("/qux"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testIndex() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("/qux"));
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(0));
        assertNull(pointer.tail());
    }

    @Test
    public void testIndexOutOfRange() {
        String input = "/foo/-1";
        try {
            JsonPointer.compile(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultipleSegments() {
        String input = "/foo/bar/baz/qux";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("/quu"));
        assertNull(pointer.matchElement(0));
        assertEquals(new JsonPointer("/baz/qux", "", null), pointer.tail());
    }

    @Test
    public void testQuotedTail() {
        String input = "/foo/~1~2/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("/quu"));
        assertNull(pointer.matchElement(0));
        assertEquals(new JsonPointer("/2/bar", "", null), pointer.tail());
    }

    @Test
    public void testQuotedTailOutOfRange() {
        String input = "/foo/~1~3/bar";
        try {
            JsonPointer.compile(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}