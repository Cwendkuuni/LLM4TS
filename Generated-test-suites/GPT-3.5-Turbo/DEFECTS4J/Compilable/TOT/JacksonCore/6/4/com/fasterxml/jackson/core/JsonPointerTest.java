package com.fasterxml.jackson.core;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setUp() {
        jsonPointer = new JsonPointer();
    }

    @Test
    public void testConstructor_emptyInstance() {
        assertEquals("", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testCompile_validJsonPointer() {
        jsonPointer = JsonPointer.compile("/foo/bar");

        assertEquals("/foo/bar", jsonPointer.toString());
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNotNull(jsonPointer.tail());
        assertEquals("/bar", jsonPointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_invalidJsonPointer() {
        jsonPointer = JsonPointer.compile("foo");
    }

    @Test
    public void testMatchProperty_matchingProperty() {
        jsonPointer = JsonPointer.compile("/foo");
        JsonPointer next = jsonPointer.matchProperty("foo");

        assertNotNull(next);
        assertEquals("", next.toString());
        assertTrue(next.matches());
        assertEquals("", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertFalse(next.mayMatchProperty());
        assertFalse(next.mayMatchElement());
        assertNull(next.tail());
    }

    @Test
    public void testMatchProperty_nonMatchingProperty() {
        jsonPointer = JsonPointer.compile("/foo");
        JsonPointer next = jsonPointer.matchProperty("bar");

        assertNull(next);
    }

    @Test
    public void testMatchElement_matchingIndex() {
        jsonPointer = JsonPointer.compile("/1");
        JsonPointer next = jsonPointer.matchElement(1);

        assertNotNull(next);
        assertEquals("", next.toString());
        assertTrue(next.matches());
        assertEquals("", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertFalse(next.mayMatchProperty());
        assertFalse(next.mayMatchElement());
        assertNull(next.tail());
    }

    @Test
    public void testMatchElement_nonMatchingIndex() {
        jsonPointer = JsonPointer.compile("/1");
        JsonPointer next = jsonPointer.matchElement(0);

        assertNull(next);
    }

    @Test
    public void testTail_nonEmptyPointer() {
        jsonPointer = JsonPointer.compile("/foo/bar/baz");
        JsonPointer tail = jsonPointer.tail();

        assertNotNull(tail);
        assertEquals("/bar/baz", tail.toString());
        assertFalse(tail.matches());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNotNull(tail.tail());
        assertEquals("/baz", tail.tail().toString());
    }

    @Test
    public void testTail_emptyPointer() {
        JsonPointer tail = jsonPointer.tail();

        assertNull(tail);
    }

}