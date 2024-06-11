package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithSegment() {
        String fullString = "/segment";
        String segment = "segment";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, JsonPointer.EMPTY);
        
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals(segment, jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithIndex() {
        String fullString = "/2";
        String segment = "2";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, JsonPointer.EMPTY);
        
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(2, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertTrue(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithSegmentAndNext() {
        String fullString = "/parent/child";
        String segment = "parent";
        String nextSegmentString = "/child";
        String nextSegment = "child";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, new JsonPointer(nextSegmentString, nextSegment, JsonPointer.EMPTY));
        
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals(segment, jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertEquals(nextSegment, jsonPointer.tail().getMatchingProperty());
    }

    @Test
    public void testJsonPointerWithIndexAndNext() {
        String fullString = "/1/child";
        String segment = "1";
        String nextSegmentString = "/child";
        String nextSegment = "child";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, new JsonPointer(nextSegmentString, nextSegment, JsonPointer.EMPTY));
        
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertTrue(jsonPointer.mayMatchElement());
        Assert.assertEquals(nextSegment, jsonPointer.tail().getMatchingProperty());
    }

    @Test
    public void testJsonPointerToString() {
        String fullString = "/parent/child";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        
        Assert.assertEquals(fullString, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerHashCode() {
        String fullString = "/parent/child";
        JsonPointer jsonPointer1 = JsonPointer.compile(fullString);
        JsonPointer jsonPointer2 = JsonPointer.compile(fullString);
        
        Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testJsonPointerEquals() {
        String fullString = "/parent/child";
        JsonPointer jsonPointer1 = JsonPointer.compile(fullString);
        JsonPointer jsonPointer2 = JsonPointer.compile(fullString);
        
        Assert.assertEquals(jsonPointer1, jsonPointer2);
    }
}