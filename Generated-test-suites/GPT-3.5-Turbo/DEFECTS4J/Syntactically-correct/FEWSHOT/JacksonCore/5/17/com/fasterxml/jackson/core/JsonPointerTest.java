package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(true, pointer.matches());

        pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals(false, pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals(true, pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals(false, pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("test", "test", next);

        JsonPointer result = pointer.matchProperty("test");
        assertEquals(next, result);

        result = pointer.matchProperty("invalid");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("test", "1", next);

        JsonPointer result = pointer.matchElement(1);
        assertEquals(next, result);

        result = pointer.matchElement(0);
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("test", "test", next);

        JsonPointer result = pointer.tail();
        assertEquals(next, result);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals("test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("test", "test", JsonPointer.EMPTY);
        JsonPointer pointer2 = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test", "test", JsonPointer.EMPTY);
        JsonPointer pointer2 = new JsonPointer("test", "test", JsonPointer.EMPTY);
        assertEquals(true, pointer1.equals(pointer2));

        pointer2 = new JsonPointer("test", "test2", JsonPointer.EMPTY);
        assertEquals(false, pointer1.equals(pointer2));

        assertEquals(false, pointer1.equals(null));
        assertEquals(false, pointer1.equals("test"));
    }

}