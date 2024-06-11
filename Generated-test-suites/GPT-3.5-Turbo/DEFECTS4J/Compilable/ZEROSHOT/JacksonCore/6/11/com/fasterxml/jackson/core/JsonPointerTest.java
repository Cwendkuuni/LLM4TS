package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/property");
        Assert.assertNotNull(pointer);
        Assert.assertEquals("/property", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("property");
    }

    @Test
    public void testValueOfValidInput() {
        JsonPointer pointer = JsonPointer.valueOf("/property");
        Assert.assertNotNull(pointer);
        Assert.assertEquals("/property", pointer.toString());
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/");
        Assert.assertTrue(pointer.matches());
        pointer = JsonPointer.compile("/property");
        Assert.assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/property");
        Assert.assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/1");
        Assert.assertEquals(1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/property");
        Assert.assertTrue(pointer.mayMatchProperty());
        pointer = JsonPointer.compile("/1");
        Assert.assertFalse(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/property");
        Assert.assertFalse(pointer.mayMatchElement());
        pointer = JsonPointer.compile("/1");
        Assert.assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/property");
        Assert.assertEquals(pointer, pointer.matchProperty("property"));
        Assert.assertNull(pointer.matchProperty("nonexistent"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/1");
        Assert.assertEquals(pointer, pointer.matchElement(1));
        Assert.assertNull(pointer.matchElement(2));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/property/1");
        JsonPointer tail = pointer.tail();
        Assert.assertNotNull(tail);
        Assert.assertEquals("/1", tail.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/property/1");
        Assert.assertEquals("/property/1", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/property/1");
        JsonPointer pointer2 = JsonPointer.compile("/property/1");
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/property/1");
        JsonPointer pointer2 = JsonPointer.compile("/property/1");
        Assert.assertTrue(pointer1.equals(pointer2));
        Assert.assertTrue(pointer2.equals(pointer1));
    }
}