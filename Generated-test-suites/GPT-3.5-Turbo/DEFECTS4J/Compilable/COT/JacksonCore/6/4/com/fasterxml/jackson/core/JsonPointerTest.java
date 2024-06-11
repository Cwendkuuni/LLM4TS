package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertTrue(pointer.matches());
        pointer = new JsonPointer("abc", "def", null);
        Assert.assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertEquals("", pointer.getMatchingProperty());
        pointer = new JsonPointer("abc", "def", null);
        Assert.assertEquals("def", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        pointer = new JsonPointer("abc", "def", null);
        Assert.assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertFalse(pointer.mayMatchProperty());
        pointer = new JsonPointer("abc", "def", null);
        Assert.assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertFalse(pointer.mayMatchElement());
        pointer = new JsonPointer("abc", "def", null);
        Assert.assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer result = pointer.matchProperty("abc");
        Assert.assertNull(result);
        pointer = new JsonPointer("abc", "def", null);
        result = pointer.matchProperty("abc");
        Assert.assertNull(result);
        result = pointer.matchProperty("def");
        Assert.assertEquals(null, result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer result = pointer.matchElement(1);
        Assert.assertNull(result);
        pointer = new JsonPointer("abc", "def", null);
        result = pointer.matchElement(-1);
        Assert.assertNull(result);
        result = pointer.matchElement(0);
        Assert.assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer result = pointer.tail();
        Assert.assertNull(result);
        pointer = new JsonPointer("abc", "def", null);
        result = pointer.tail();
        Assert.assertNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertEquals("", pointer.toString());
        pointer = new JsonPointer("abc", "def", null);
        Assert.assertEquals("abc", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
        pointer1 = new JsonPointer("abc", "def", null);
        pointer2 = new JsonPointer("abc", "def", null);
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        Assert.assertTrue(pointer1.equals(pointer2));
        pointer1 = new JsonPointer("abc", "def", null);
        pointer2 = new JsonPointer("abc", "def", null);
        Assert.assertTrue(pointer1.equals(pointer2));
        pointer2 = new JsonPointer("abc", "xyz", null);
        Assert.assertFalse(pointer1.equals(pointer2));
    }
}

