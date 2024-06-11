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
        Assert.assertNull(pointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertNull(pointer.tail());
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        Assert.assertNotNull(pointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        Assert.assertNotNull(pointer);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals(0, pointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        Assert.assertTrue(pointer1.equals(pointer2));
    }

}