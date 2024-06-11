package com.fasterxml.jackson.core;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals("", jsonPointer.toString());
        Assert.assertEquals(0, jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
    }

    @Test
    public void testJsonPointerWithMatchingSegment() {
        JsonPointer nextPointer = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("/test", "test", nextPointer);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("test", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("unknown"));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertEquals(nextPointer, jsonPointer.tail());
        Assert.assertEquals("/test", jsonPointer.toString());
        Assert.assertEquals("/test".hashCode(), jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
    }

    @Test
    public void testJsonPointerWithMatchingIndex() {
        JsonPointer nextPointer = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("/0", "0", nextPointer);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("0", jsonPointer.getMatchingProperty());
        Assert.assertEquals(0, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertTrue(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("unknown"));
        Assert.assertNull(jsonPointer.matchElement(1));
        Assert.assertEquals(nextPointer, jsonPointer.tail());
        Assert.assertEquals("/0", jsonPointer.toString());
        Assert.assertEquals("/0".hashCode(), jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
    }

    @Test
    public void testJsonPointerWithQuotedSegment() {
        JsonPointer nextPointer = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("/~0~1", "~0~1", nextPointer);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("~0~1", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("unknown"));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertEquals(nextPointer, jsonPointer.tail());
        Assert.assertEquals("/~0~1", jsonPointer.toString());
        Assert.assertEquals("/~0~1".hashCode(), jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerCompileWithEmptyInput() {
        JsonPointer.compile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerCompileWithInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testJsonPointerCompileWithValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/0");
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("test", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("unknown"));
        Assert.assertNull(jsonPointer.matchElement(1));
        Assert.assertNotNull(jsonPointer.tail());
        Assert.assertEquals("/test/0", jsonPointer.toString());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
    }
}