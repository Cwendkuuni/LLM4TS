package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerConstructionWithSingleSegment() {
        JsonPointer jsonPointer = new JsonPointer("/foo");
        assertEquals("/foo", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("foo", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testJsonPointerConstructionWithTwoSegments() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar");
        assertEquals("/foo/bar", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar", tail.toString());
        assertFalse(tail.matches());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
    }

    @Test
    public void testJsonPointerConstructionWithTwoSegmentsAndPropertyMatch() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "bar", null);
        assertEquals("/foo/bar", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("bar", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testJsonPointerConstructionWithTwoSegmentsAndElementMatch() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", null, new JsonPointer("/1"));
        assertEquals("/foo/bar/1", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar/1", tail.toString());
        assertFalse(tail.matches());
        assertFalse(tail.mayMatchProperty());
        assertTrue(tail.mayMatchElement());
        assertNull(tail.tail());
    }

    @Test
    public void testJsonPointerToString() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar", "bar", null);
        JsonPointer jsonPointer3 = new JsonPointer("/foo/bar", null, new JsonPointer("/1"));

        assertEquals("/foo/bar", jsonPointer1.toString());
        assertEquals("/foo/bar", jsonPointer1.toString());
        assertEquals("/foo/bar", jsonPointer2.toString());
        assertEquals("/foo/bar", jsonPointer3.toString());
    }

    @Test
    public void testJsonPointerHashCodeEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar");
        JsonPointer jsonPointer3 = new JsonPointer("/foo/baz");

        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerIllegalArgumentException() {
        JsonPointer jsonPointer = new JsonPointer("~/foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerIllegalArgumentException2() {
        JsonPointer jsonPointer = new JsonPointer("/foo~");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerIllegalArgumentException3() {
        JsonPointer jsonPointer = new JsonPointer("~foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerIllegalArgumentException4() {
        JsonPointer jsonPointer = new JsonPointer("/foo~");
    }

    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar", "bar", null);

        JsonPointer matchingJsonPointer = jsonPointer2.matchProperty("bar");
        assertEquals(jsonPointer2, matchingJsonPointer);

        JsonPointer nonMatchingJsonPointer = jsonPointer1.matchProperty("foo");
        assertNull(nonMatchingJsonPointer);
    }

    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar", null, new JsonPointer("/1"));

        JsonPointer matchingJsonPointer = jsonPointer2.matchElement(1);
        assertEquals(jsonPointer2, matchingJsonPointer);

        JsonPointer nonMatchingJsonPointer = jsonPointer1.matchElement(2);
        assertNull(nonMatchingJsonPointer);
    }
}