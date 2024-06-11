package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals("", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertNull(pointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertNull(pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals(pointer, pointer.tail());
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        Assert.assertEquals("", pointer.toString());
    }
}