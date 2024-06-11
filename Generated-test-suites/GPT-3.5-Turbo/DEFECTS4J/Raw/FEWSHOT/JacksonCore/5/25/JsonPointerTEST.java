package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        JsonPointer nonEmptyPointer = new JsonPointer("/foo");
        assertFalse(nonEmptyPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo");
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchEelement() {
        JsonPointer pointer = new JsonPointer("/0");
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo");
        assertNull(pointer.matchProperty("bar"));

        JsonPointer matchPointer = pointer.matchProperty("foo");
        assertNotNull(matchPointer);
        assertEquals("/foo", matchPointer.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0");
        assertNull(pointer.matchElement(1));

        JsonPointer matchPointer = pointer.matchElement(0);
        assertNotNull(matchPointer);
        assertEquals("/0", matchPointer.toString());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        JsonPointer tailPointer = pointer.tail();
        assertNotNull(pointer);
        assertEquals("/bar", tailPointer.toString());
    }

    @Test
    public void testToString() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertEquals("", emptyPointer.toString());

        JsonPointer nonEmptyPointer = new JsonPointer("/foo/bar");
        assertEquals("/foo/bar", nonEmptyPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer emptyPointer1 = JsonPointer.EMPTY;
        JsonPointer emptyPointer2 = JsonPointer.EMPTY;
        assertEquals(emptyPointer1.hashCode(), emptyPointer2.hashCode());

        JsonPointer nonEmptyPointer1 = new JsonPointer("/foo/bar");
        JsonPointer nonEmptyPointer2 = new JsonPointer("/foo/bar");
        assertEquals(nonEmptyPointer1.hashCode(), nonEmptyPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer emptyPointer1 = JsonPointer.EMPTY;
        JsonPointer emptyPointer2 = JsonPointer.EMPTY;
        assertTrue(emptyPointer1.equals(emptyPointer2));

        JsonPointer nonEmptyPointer1 = new JsonPointer("/foo/bar");
        JsonPointer nonEmptyPointer2 = new JsonPointer("/foo/bar");
        assertTrue(nonEmptyPointer1.equals(nonEmptyPointer2));
    }

    @Test
    public void testCompile() {
        JsonPointer emptyPointer = JsonPointer.compile("");
        assertEquals("", emptyPointer.toString());

        JsonPointer nonEmptyPointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", nonEmptyPointer.toString());
    }
}