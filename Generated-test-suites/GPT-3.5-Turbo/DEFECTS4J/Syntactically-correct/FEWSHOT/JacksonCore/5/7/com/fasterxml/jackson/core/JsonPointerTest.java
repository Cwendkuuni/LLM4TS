
package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());

        pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.getMatchingProperty());

        pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertEquals("segment", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("test", "0", JsonPointer.EMPTY);
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchProperty());

        pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("test", "0", JsonPointer.EMPTY);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer next = pointer.matchProperty("");
        assertNull(next);

        pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        next = pointer.matchProperty("segment");
        assertNotNull(next);
        assertEquals(JsonPointer.EMPTY, next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer next = pointer.matchElement(0);
        assertNull(next);

        pointer = new JsonPointer("test", "0", JsonPointer.EMPTY);
        next = pointer.matchElement(0);
        assertNotNull(next);
        assertEquals(JsonPointer.EMPTY, next);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer next = pointer.tail();
        assertEquals(JsonPointer.EMPTY, next);

        pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        next = pointer.tail();
        assertEquals(JsonPointer.EMPTY, next);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile(null);
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.compile("test");
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.compile("/test");
        assertNotEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf(null);
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.valueOf("");
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.valueOf("test");
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.valueOf("/test");
        assertNotEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());

        pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertEquals("test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.EMPTY;
        pointer2 = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertFalse(pointer1.equals(pointer2));
    }

}
