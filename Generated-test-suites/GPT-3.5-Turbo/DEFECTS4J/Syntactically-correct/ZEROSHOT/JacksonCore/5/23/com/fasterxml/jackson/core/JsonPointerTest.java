package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        Assert.assertNotNull(pointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/abc/def");
        Assert.assertNotNull(pointer);
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        boolean result = pointer.matches();
        Assert.assertFalse(result);
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        String result = pointer.getMatchingProperty();
        Assert.assertNull(result);
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        int result = pointer.getMatchingIndex();
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        boolean result = pointer.mayMatchProperty();
        Assert.assertTrue(result);
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        boolean result = pointer.mayMatchElement();
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        JsonPointer result = pointer.matchProperty("abc");
        Assert.assertNotNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        JsonPointer result = pointer.matchElement(0);
        Assert.assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        JsonPointer result = pointer.tail();
        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        String result = pointer.toString();
        Assert.assertEquals("/abc/def", result);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = JsonPointer.compile("/abc/def");
        int result = pointer.hashCode();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/abc/def");
        JsonPointer pointer2 = JsonPointer.compile("/abc/def");
        boolean result = pointer1.equals(pointer2);
        Assert.assertTrue(result);
    }

}