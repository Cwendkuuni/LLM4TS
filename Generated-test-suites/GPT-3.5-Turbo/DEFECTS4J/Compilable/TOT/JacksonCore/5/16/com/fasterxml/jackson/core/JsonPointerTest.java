package com.fasterxml.jackson.core;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

  @Test
  public void testEmptyJsonPointer() {
    JsonPointer jsonPointer = JsonPointer.compile("");
    
    Assert.assertTrue(jsonPointer.matches());
    Assert.assertEquals("", jsonPointer.getMatchingProperty());
    Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    Assert.assertFalse(jsonPointer.mayMatchProperty());
    Assert.assertFalse(jsonPointer.mayMatchElement());
    Assert.assertNull(jsonPointer.matchProperty("name"));
    Assert.assertNull(jsonPointer.matchElement(0));
    Assert.assertEquals("", jsonPointer.toString());
    Assert.assertEquals(jsonPointer, JsonPointer.EMPTY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonPointer() {
    JsonPointer jsonPointer = JsonPointer.compile("invalid");
  }

  @Test
  public void testJsonPointerWithMatchingProperty() {
    JsonPointer jsonPointer = JsonPointer.compile("/property");

    Assert.assertFalse(jsonPointer.matches());
    Assert.assertEquals("property", jsonPointer.getMatchingProperty());
    Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    Assert.assertTrue(jsonPointer.mayMatchProperty());
    Assert.assertFalse(jsonPointer.mayMatchElement());
    Assert.assertNotNull(jsonPointer.matchProperty("property"));
    Assert.assertNull(jsonPointer.matchProperty("name"));
    Assert.assertNull(jsonPointer.matchElement(0));
    Assert.assertEquals("/property", jsonPointer.toString());
  }

  @Test
  public void testJsonPointerWithMatchingIndex() {
    JsonPointer jsonPointer = JsonPointer.compile("/0");

    Assert.assertFalse(jsonPointer.matches());
    Assert.assertEquals("", jsonPointer.getMatchingProperty());
    Assert.assertEquals(0, jsonPointer.getMatchingIndex());
    Assert.assertFalse(jsonPointer.mayMatchProperty());
    Assert.assertTrue(jsonPointer.mayMatchElement());
    Assert.assertNull(jsonPointer.matchProperty("property"));
    Assert.assertNotNull(jsonPointer.matchElement(0));
    Assert.assertNull(jsonPointer.matchElement(1));
    Assert.assertEquals("/0", jsonPointer.toString());
  }

  @Test
  public void testJsonPointerWithMultipleSegments() {
    JsonPointer jsonPointer = JsonPointer.compile("/segment1/segment2");

    Assert.assertFalse(jsonPointer.matches());
    Assert.assertEquals("segment1", jsonPointer.getMatchingProperty());
    Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    Assert.assertTrue(jsonPointer.mayMatchProperty());
    Assert.assertFalse(jsonPointer.mayMatchElement());
    Assert.assertNotNull(jsonPointer.matchProperty("segment1"));
    Assert.assertNull(jsonPointer.matchProperty("segment3"));
    Assert.assertNull(jsonPointer.matchElement(0));
    Assert.assertEquals("/segment1/segment2", jsonPointer.toString());

    JsonPointer nextSegment = jsonPointer.tail();

    Assert.assertFalse(nextSegment.matches());
    Assert.assertEquals("segment2", nextSegment.getMatchingProperty());
    Assert.assertEquals(-1, nextSegment.getMatchingIndex());
    Assert.assertTrue(nextSegment.mayMatchProperty());
    Assert.assertFalse(nextSegment.mayMatchElement());
    Assert.assertNotNull(nextSegment.matchProperty("segment2"));
    Assert.assertNull(nextSegment.matchProperty("segment4"));
    Assert.assertNull(nextSegment.matchElement(0));
    Assert.assertEquals("/segment2", nextSegment.toString());
  }

}