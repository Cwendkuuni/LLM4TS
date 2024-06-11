package com.fasterxml.jackson.core;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer._nextSegment);
        Assert.assertEquals("", jsonPointer._matchingPropertyName);
        Assert.assertEquals(-1, jsonPointer._matchingElementIndex);
        Assert.assertEquals("", jsonPointer._asString);
    }

    @Test
    public void testNonEmptyConstructor() {
        String fullString = "fullString";
        String segment = "segment";
        JsonPointer next = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        Assert.assertEquals(fullString, jsonPointer._asString);
        Assert.assertEquals(next, jsonPointer._nextSegment);
        Assert.assertEquals(segment, jsonPointer._matchingPropertyName);
        Assert.assertEquals(-1, jsonPointer._matchingElementIndex);
    }

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.matches());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertFalse(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertEquals("segment", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertFalse(jsonPointer.mayMatchElement());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchProperty("name"));
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertNull(jsonPointer.matchProperty("name"));
        Assert.assertNotNull(jsonPointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchElement(0));
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertNotNull(jsonPointer.matchElement(-1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.tail());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertNotNull(jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.toString());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertEquals("fullString", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("".hashCode(), jsonPointer.hashCode());
        jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertEquals("fullString".hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        JsonPointer jsonPointer3 = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
        Assert.assertFalse(jsonPointer1.equals(null));
        Assert.assertFalse(jsonPointer1.equals(new Object()));
        Assert.assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile(null);
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer);
        jsonPointer = JsonPointer.compile("");
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer);
        jsonPointer = JsonPointer.compile("/test");
        Assert.assertNotNull(jsonPointer);
    }
    
    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf(null);
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer);
        jsonPointer = JsonPointer.valueOf("");
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer);
        jsonPointer = JsonPointer.valueOf("/test");
        Assert.assertNotNull(jsonPointer);
    }
    
}