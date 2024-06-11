package com.fasterxml.jackson.core; // package name should match the class

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals("", jsonPointer.toString());
        Assert.assertEquals(0, jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(JsonPointer.EMPTY));
    }

    @Test
    public void testNonEmptyConstructor() {
        String fullString = "/test/property";
        String segment = "property";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals(segment, jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals(fullString, jsonPointer.toString());
        Assert.assertEquals(fullString.hashCode(), jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(new JsonPointer(fullString, segment, null)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "test";
        JsonPointer.compile(input);
    }

    @Test
    public void testCompileMethod() {
        String input = "/test/property";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("property", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals(input, jsonPointer.toString());
        Assert.assertEquals(input.hashCode(), jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(new JsonPointer(input, null, null)));
    }

    @Test
    public void testFromSegmentMethod() {
        String[] segments = {"test", "property"};
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals(segments[1], jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals("/test/property", jsonPointer.toString());
        Assert.assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(new JsonPointer("/test/property", "property", null)));
    }

    @Test
    public void testMatchesMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        Assert.assertFalse(jsonPointer1.matches());
        Assert.assertTrue(jsonPointer1.matches());
        Assert.assertFalse(jsonPointer2.matches());
        Assert.assertTrue(jsonPointer3.matches());
    }

    @Test
    public void testGetMatchingPropertyMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        Assert.assertEquals("property", jsonPointer1.getMatchingProperty());
        Assert.assertEquals(null, jsonPointer2.getMatchingProperty());
        Assert.assertEquals("property", jsonPointer3.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndexMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/0");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test/1");
        Assert.assertEquals(-1, jsonPointer1.getMatchingIndex());
        Assert.assertEquals(-1, jsonPointer2.getMatchingIndex());
        Assert.assertEquals(0, jsonPointer3.getMatchingIndex());
    }

    @Test
    public void testMayMatchPropertyMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        Assert.assertTrue(jsonPointer1.mayMatchProperty());
        Assert.assertFalse(jsonPointer2.mayMatchProperty());
        Assert.assertTrue(jsonPointer3.mayMatchProperty());
    }

    @Test
    public void testMayMatchElementMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/0");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test/1");
        Assert.assertFalse(jsonPointer1.mayMatchElement());
        Assert.assertFalse(jsonPointer2.mayMatchElement());
        Assert.assertFalse(jsonPointer3.mayMatchElement());
    }

    @Test
    public void testMatchPropertyMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        JsonPointer jsonPointer4 = new JsonPointer("/test/property");
        Assert.assertNull(jsonPointer1.matchProperty(null));
        Assert.assertNull(jsonPointer1.matchProperty(""));
        Assert.assertNull(jsonPointer1.matchProperty("test"));
        Assert.assertNull(jsonPointer1.matchProperty("property2"));
        Assert.assertEquals(jsonPointer3, jsonPointer1.matchProperty("property"));
        Assert.assertNull(jsonPointer2.matchProperty("property"));
        Assert.assertNull(jsonPointer4.matchProperty("element"));
        Assert.assertEquals(jsonPointer3, jsonPointer4.matchProperty("property"));
    }

    @Test
    public void testMatchElementMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/0");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test/1");
        JsonPointer jsonPointer4 = new JsonPointer("/test/1");
        Assert.assertNull(jsonPointer1.matchElement(-1));
        Assert.assertNull(jsonPointer1.matchElement(0));
        Assert.assertNull(jsonPointer1.matchElement(1));
        Assert.assertNull(jsonPointer2.matchElement(-1));
        Assert.assertNull(jsonPointer2.matchElement(0));
        Assert.assertNull(jsonPointer2.matchElement(1));
        Assert.assertNull(jsonPointer3.matchElement(-1));
        Assert.assertNull(jsonPointer3.matchElement(0));
        Assert.assertEquals(jsonPointer3, jsonPointer3.matchElement(1));
        Assert.assertNull(jsonPointer4.matchElement(-1));
        Assert.assertNull(jsonPointer4.matchElement(0));
        Assert.assertEquals(jsonPointer3, jsonPointer4.matchElement(1));
    }

    @Test
    public void testTailMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property/element");
        Assert.assertNull(jsonPointer1.tail());
        Assert.assertEquals(jsonPointer1, jsonPointer2.tail());
    }

    @Test
    public void testToStringMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        Assert.assertEquals("/test/property", jsonPointer1.toString());
        Assert.assertEquals("/test/element", jsonPointer2.toString());
        Assert.assertEquals("/test/property", jsonPointer3.toString());
    }

    @Test
    public void testHashCodeMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
        Assert.assertNotSame(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/element");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");
        Assert.assertTrue(jsonPointer1.equals(jsonPointer3));
        Assert.assertFalse(jsonPointer1.equals(jsonPointer2));
        Assert.assertFalse(jsonPointer2.equals(jsonPointer1));
        Assert.assertFalse(jsonPointer2.equals(jsonPointer3));
        Assert.assertTrue(jsonPointer1.equals(new JsonPointer("/test/property", "property", null)));
        Assert.assertTrue(jsonPointer1.equals(JsonPointer.compile("/test/property")));
    }

    
}