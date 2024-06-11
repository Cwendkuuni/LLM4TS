package com.fasterxml.jackson.core;

import org.junit.Test;
import org.junit.Assert;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.matches());
        Assert.assertEquals("", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertFalse(pointer.mayMatchProperty());
        Assert.assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testCompileValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        Assert.assertEquals("foo", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertEquals("foo/bar", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidPointer() {
        JsonPointer.compile("foo/bar");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer next = pointer.matchProperty("foo");
        Assert.assertNotNull(next);
        Assert.assertTrue(next.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer next = pointer.matchElement(0);
        Assert.assertNotNull(next);
        Assert.assertTrue(next.matches());
    }

    // Add more test cases for other public methods

}