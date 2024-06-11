package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
  
  @Test
  public void testEmptyConstructor() {
    JsonPointer pointer = new JsonPointer();
    
    // Test matches
    Assert.assertTrue(pointer.matches());
    
    // Test getMatchingProperty
    Assert.assertEquals("", pointer.getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer.getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertTrue(pointer.mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer.mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer.matchProperty("name"));
    
    // Test matchElement
    Assert.assertNull(pointer.matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer.tail());
    
    // Test toString
    Assert.assertEquals("", pointer.toString());
    
    // Test hashCode
    Assert.assertEquals(0, pointer.hashCode());
    
    // Test equals
    Assert.assertTrue(pointer.equals(new JsonPointer()));
  }
  
  @Test
  public void testNonEmptyConstructor() {
    JsonPointer pointer = new JsonPointer("fullString", "segment", null);
    
    // Test matches
    Assert.assertFalse(pointer.matches());
    
    // Test getMatchingProperty
    Assert.assertEquals("segment", pointer.getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer.getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertTrue(pointer.mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer.mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer.matchProperty("name"));
    Assert.assertEquals(null, pointer.matchProperty("segment"));
    
    // Test matchElement
    Assert.assertNull(pointer.matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer.tail());
    
    // Test toString
    Assert.assertEquals("fullString", pointer.toString());
    
    // Test hashCode
    Assert.assertEquals("fullString".hashCode(), pointer.hashCode());
    
    // Test equals
    Assert.assertFalse(pointer.equals(new JsonPointer()));
    Assert.assertTrue(pointer.equals(new JsonPointer("fullString", "segment", null)));
  }
  
  @Test
  public void testCompile() {
    JsonPointer pointer1 = JsonPointer.compile("/property");
  
    // Test matches
    Assert.assertFalse(pointer1.matches());
    
    // Test getMatchingProperty
    Assert.assertEquals("property", pointer1.getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer1.getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertTrue(pointer1.mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer1.mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer1.matchProperty("name"));
    Assert.assertEquals(null, pointer1.matchProperty("segment"));
    
    // Test matchElement
    Assert.assertNull(pointer1.matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer1.tail());
    
    // Test toString
    Assert.assertEquals("/property", pointer1.toString());
    
    // Test hashCode
    Assert.assertEquals("/property".hashCode(), pointer1.hashCode());
    
    // Test equals
    Assert.assertFalse(pointer1.equals(new JsonPointer()));
    Assert.assertTrue(pointer1.equals(JsonPointer.valueOf("/property")));
    
    JsonPointer pointer2 = JsonPointer.compile("/array[0]");
  
    // Test matches
    Assert.assertFalse(pointer2.matches());
    
    // Test getMatchingProperty
    Assert.assertEquals(null, pointer2.getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(0, pointer2.getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertFalse(pointer2.mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer2.mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer2.matchProperty("name"));
    Assert.assertNull(pointer2.matchProperty("segment"));
    
    // Test matchElement
    Assert.assertNull(pointer2.matchElement(1));
    Assert.assertEquals(pointer2, pointer2.matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer2.tail());
    
    // Test toString
    Assert.assertEquals("/array[0]", pointer2.toString());
    
    // Test hashCode
    Assert.assertEquals("/array[0]".hashCode(), pointer2.hashCode());
    
    // Test equals
    Assert.assertFalse(pointer2.equals(new JsonPointer()));
    Assert.assertTrue(pointer2.equals(JsonPointer.valueOf("/array[0]")));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCompile() {
    JsonPointer pointer = JsonPointer.compile("property");
  }
  
  @Test
  public void testValueOf() {
    JsonPointer pointer = JsonPointer.valueOf("/property");
  
    // Test matches
    Assert.assertFalse(pointer.matches());
    
    // Test getMatchingProperty
    Assert.assertEquals("property", pointer.getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer.getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertTrue(pointer.mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer.mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer.matchProperty("name"));
    Assert.assertEquals(null, pointer.matchProperty("segment"));
    
    // Test matchElement
    Assert.assertNull(pointer.matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer.tail());
    
    // Test toString
    Assert.assertEquals("/property", pointer.toString());
    
    // Test hashCode
    Assert.assertEquals("/property".hashCode(), pointer.hashCode());
    
    // Test equals
    Assert.assertFalse(pointer.equals(new JsonPointer()));
    Assert.assertTrue(pointer.equals(JsonPointer.valueOf("/property")));
  }
  
  @Test
  public void testMatchProperty() {
    JsonPointer pointer = JsonPointer.valueOf("/property");
  
    // Test matches
    Assert.assertFalse(pointer.matchProperty("name").matches());
    
    // Test getMatchingProperty
    Assert.assertEquals(null, pointer.matchProperty("name").getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer.matchProperty("name").getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertTrue(pointer.matchProperty("name").mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertFalse(pointer.matchProperty("name").mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer.matchProperty("name").matchProperty("property"));
    Assert.assertNull(pointer.matchProperty("name").matchProperty("name"));
    
    // Test matchElement
    Assert.assertNull(pointer.matchProperty("name").matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer.matchProperty("name").tail());
    
    // Test toString
    Assert.assertEquals("", pointer.matchProperty("name").toString());
    
    // Test hashCode
    Assert.assertEquals(0, pointer.matchProperty("name").hashCode());
    
    // Test equals
    Assert.assertTrue(pointer.matchProperty("name").equals(new JsonPointer()));
  }
  
  @Test
  public void testMatchElement() {
    JsonPointer pointer = JsonPointer.valueOf("/array[0]");
  
    // Test matches
    Assert.assertFalse(pointer.matchElement(1).matches());
    
    // Test getMatchingProperty
    Assert.assertEquals(null, pointer.matchElement(1).getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer.matchElement(1).getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertFalse(pointer.matchElement(1).mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer.matchElement(1).mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer.matchElement(1).matchProperty("property"));
    
    // Test matchElement
    Assert.assertNull(pointer.matchElement(1).matchElement(0));
    Assert.assertNull(pointer.matchElement(1).matchElement(1));
    
    // Test tail
    Assert.assertNull(pointer.matchElement(1).tail());
    
    // Test toString
    Assert.assertEquals("", pointer.matchElement(1).toString());
    
    // Test hashCode
    Assert.assertEquals(0, pointer.matchElement(1).hashCode());
    
    // Test equals
    Assert.assertTrue(pointer.matchElement(1).equals(new JsonPointer()));
  }
  
  @Test
  public void testParseTail() {
    JsonPointer pointer = JsonPointer.valueOf("/property");
  
    // Test matches
    Assert.assertFalse(pointer.tail().matches());
    
    // Test getMatchingProperty
    Assert.assertEquals("property", pointer.tail().getMatchingProperty());
    
    // Test getMatchingIndex
    Assert.assertEquals(-1, pointer.tail().getMatchingIndex());
    
    // Test mayMatchProperty
    Assert.assertTrue(pointer.tail().mayMatchProperty());
    
    // Test mayMatchElement
    Assert.assertTrue(pointer.tail().mayMatchElement());
    
    // Test matchProperty
    Assert.assertNull(pointer.tail().matchProperty("name"));
    Assert.assertEqual(pointer.tail(), pointer.tail().matchProperty("property"));
    
    // Test matchElement
    Assert.assertNull(pointer.tail().matchElement(0));
    
    // Test tail
    Assert.assertNull(pointer.tail().tail());
    
    // Test toString
    Assert.assertEquals("property", pointer.tail().toString());
    
    // Test hashCode
    Assert.assertEquals("property".hashCode(), pointer.tail().hashCode());
    
    // Test equals
    Assert.assertFalse(pointer.tail().equals(new JsonPointer()));
    Assert.assertTrue(pointer.tail().equals(new JsonPointer("fullString", "property", null)));
  }
  
  @Test
  public void testToJSON() {
    JsonPointer pointer = JsonPointer.valueOf("/property");
  
    // Test toString
    Assert.assertEquals("/property", pointer.toString());
    
    // Test hashCode
    Assert.assertEquals("/property".hashCode(), pointer.hashCode());
    
    // Test equals
    Assert.assertFalse(pointer.equals(new JsonPointer()));
    Assert.assertTrue(pointer.equals(JsonPointer.valueOf("/property")));
  }
}