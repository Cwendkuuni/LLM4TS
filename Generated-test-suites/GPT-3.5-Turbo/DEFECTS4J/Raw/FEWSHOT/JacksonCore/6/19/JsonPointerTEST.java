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
    public void testConstructorWithArguments() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
        
        Assert.assertEquals("fullString", jsonPointer._asString);
        Assert.assertEquals("segment", jsonPointer._matchingPropertyName);
        Assert.assertEquals(-1, jsonPointer._matchingElementIndex);
        Assert.assertNotNull(jsonPointer._nextSegment);
    }

    @Test
    public void testCompileWithNullInput() {
        JsonPointer jsonPointer = JsonPointer.compile(null);
        
        Assert.assertEquals("", jsonPointer._asString);
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("name"));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals(0, jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
        Assert.assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        
        Assert.assertEquals("", jsonPointer._asString);
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("name"));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals(0, jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
        Assert.assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testCompileWithInvalidInput() {
        try {
            JsonPointer.compile("invalid");
            Assert.fail("IllegalArgumentException not thrown for invalid input");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
        }
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("");
        
        Assert.assertEquals("", jsonPointer._asString);
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("name"));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertNull(jsonPointer.tail());
        Assert.assertEquals(0, jsonPointer.hashCode());
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
        Assert.assertFalse(jsonPointer.equals(null));
        Assert.assertFalse(jsonPointer.equals("test"));
        Assert.assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        
        Assert.assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        
        Assert.assertFalse(jsonPointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.matchProperty("name");
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.matchElement(0);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.tail();
        
        Assert.assertNull(result);
    }
}