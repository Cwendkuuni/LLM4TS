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
    public void testEmptyConstructor() {
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.getMatchingProperty().isEmpty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("test"));
        assertNull(jsonPointer.matchElement(0));
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testCompileWithValidInput() {
        String input = "/test";
        jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("test"));
        assertNull(jsonPointer.matchElement(0));
        assertEquals(input, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        String input = "test";
        jsonPointer = JsonPointer.compile(input);
    }

    @Test
    public void testMatchPropertyWithExactMatch() {
        String input = "/test";
        jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchElement(0));
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testMatchElementWithExactMatch() {
        String input = "/0";
        jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.getMatchingProperty().isEmpty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("test"));
        assertNull(jsonPointer.matchElement(1));
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testTailWithMatchingProperty() {
        String input = "/test";
        jsonPointer = JsonPointer.compile(input);
        JsonPointer tail = jsonPointer.tail();
        assertTrue(tail.matches());
        assertTrue(tail.getMatchingProperty().isEmpty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(0));
        assertEquals("", tail.toString());
    }

    @Test
    public void testTailWithMatchingElement() {
        String input = "/0";
        jsonPointer = JsonPointer.compile(input);
        JsonPointer tail = jsonPointer.tail();
        assertTrue(tail.matches());
        assertTrue(tail.getMatchingProperty().isEmpty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(1));
        assertEquals("", tail.toString());
    }

    @Test
    public void testQuotedTailWithMatchingProperty() {
        String input = "/~0~0";
        jsonPointer = JsonPointer.compile(input);
        JsonPointer tail = jsonPointer.tail();
        assertTrue(tail.matches());
        assertTrue(tail.getMatchingProperty().isEmpty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(0));
        assertEquals("", tail.toString());
    }

    @Test
    public void testQuotedTailWithMatchingElement() {
        String input = "/~0";
        jsonPointer = JsonPointer.compile(input);
        JsonPointer tail = jsonPointer.tail();
        assertTrue(tail.matches());
        assertTrue(tail.getMatchingProperty().isEmpty());
        assertEquals(-1, tail.getMatchingIndex());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.matchProperty("test"));
        assertNull(tail.matchElement(1));
        assertEquals("", tail.toString());
    }

}