package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testCompileValidJsonPointerExpression() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertEquals(input, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointerExpression() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/0";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        String propertyName = "foo";
        JsonPointer jsonPointer2 = jsonPointer1.matchProperty(propertyName);
        assertTrue(jsonPointer2.matches());
        assertEquals(propertyName, jsonPointer2.getMatchingProperty());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertFalse(jsonPointer2.mayMatchElement());
        assertEquals(jsonPointer1.toString(), jsonPointer2.toString());
    }

    @Test
    public void testMatchPropertyMissing() {
        String input = "/foo/0";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        String propertyName = "bar";
        JsonPointer jsonPointer2 = jsonPointer1.matchProperty(propertyName);
        assertNull(jsonPointer2);
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/0";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        int index = 0;
        JsonPointer jsonPointer2 = jsonPointer1.matchElement(index);
        assertTrue(jsonPointer2.matches());
        assertEquals(input, jsonPointer2.toString());
    }

    @Test
    public void testMatchElementMissing() {
        String input = "/foo/0";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        int index = 1;
        JsonPointer jsonPointer2 = jsonPointer1.matchElement(index);
        assertNull(jsonPointer2);
    }

    @Test
    public void testTail() {
        String input = "/foo/0";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        JsonPointer jsonPointer2 = jsonPointer1.tail();
        assertFalse(jsonPointer2.matches());
        assertEquals("/foo", jsonPointer2.toString());
    }

    @Test
    public void testEquals() {
        String input1 = "/foo/0";
        String input2 = "/foo/0";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        int expectedHashCode = input.hashCode();
        int actualHashCode = jsonPointer.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}