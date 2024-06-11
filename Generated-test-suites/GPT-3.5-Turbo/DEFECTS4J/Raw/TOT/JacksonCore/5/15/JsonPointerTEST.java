
package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
  
  @Test
  public void testEmptyJsonPointer() {
    JsonPointer jsonPointer = new JsonPointer();
    
    assertTrue(jsonPointer.matches());
    assertEquals("", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertFalse(jsonPointer.mayMatchProperty());
    assertFalse(jsonPointer.mayMatchElement());
    assertNull(jsonPointer.matchProperty("name"));
    assertNull(jsonPointer.matchElement(0));
    assertNull(jsonPointer.tail());
    assertEquals("", jsonPointer.toString());
    assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    assertTrue(jsonPointer.equals(jsonPointer));
  }
  
  @Test
  public void testMatchingPropertyJsonPointer() {
    JsonPointer jsonPointer = new JsonPointer("name", null, null);
    
    assertFalse(jsonPointer.matches());
    assertEquals("name", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertTrue(jsonPointer.mayMatchProperty());
    assertFalse(jsonPointer.mayMatchElement());
    assertNull(jsonPointer.matchProperty("name2"));
    assertNull(jsonPointer.matchElement(0));
    assertNull(jsonPointer.tail());
    assertEquals("name", jsonPointer.toString());
    assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    assertTrue(jsonPointer.equals(jsonPointer));
  }
  
  @Test
  public void testMatchingElementJsonPointer() {
    JsonPointer jsonPointer = new JsonPointer("", -1, null);
    
    assertFalse(jsonPointer.matches());
    assertEquals("", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertFalse(jsonPointer.mayMatchProperty());
    assertTrue(jsonPointer.mayMatchElement());
    assertNull(jsonPointer.matchProperty("name"));
    assertNull(jsonPointer.matchElement(1));
    assertNull(jsonPointer.tail());
    assertEquals("", jsonPointer.toString());
    assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    assertTrue(jsonPointer.equals(jsonPointer));
  }
  
  @Test
  public void testNonEmptyJsonPointer() {
    JsonPointer jsonPointer = new JsonPointer("root", null, null);
    JsonPointer tailJsonPointer = new JsonPointer("child", null, null);
    
    assertFalse(jsonPointer.matches());
    assertEquals("root", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertTrue(jsonPointer.mayMatchProperty());
    assertFalse(jsonPointer.mayMatchElement());
    assertNull(jsonPointer.matchProperty("name2"));
    assertNull(jsonPointer.matchElement(0));
    assertNotNull(jsonPointer.tail());
    assertEquals("root", jsonPointer.toString());
    assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    assertTrue(jsonPointer.equals(jsonPointer));
    
    assertFalse(tailJsonPointer.matches());
    assertEquals("child", tailJsonPointer.getMatchingProperty());
    assertEquals(-1, tailJsonPointer.getMatchingIndex());
    assertTrue(tailJsonPointer.mayMatchProperty());
    assertFalse(tailJsonPointer.mayMatchElement());
    assertNull(tailJsonPointer.matchProperty("name2"));
    assertNull(tailJsonPointer.matchElement(1));
    assertNull(tailJsonPointer.tail());
    assertEquals("child", tailJsonPointer.toString());
    assertEquals(tailJsonPointer.hashCode(), tailJsonPointer.hashCode());
    assertTrue(tailJsonPointer.equals(tailJsonPointer));
  }
  
  @Test
  public void testJsonPointerWithQuotedTail() {
    JsonPointer jsonPointer = new JsonPointer("~property", null, null);
    
    assertFalse(jsonPointer.matches());
    assertEquals("~property", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertTrue(jsonPointer.mayMatchProperty());
    assertFalse(jsonPointer.mayMatchElement());
    assertNotNull(jsonPointer.matchProperty("~property"));
    assertNull(jsonPointer.matchProperty("property~"));
    assertNull(jsonPointer.matchElement(0));
    assertNull(jsonPointer.tail());
    assertEquals("~property", jsonPointer.toString());
    assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    assertTrue(jsonPointer.equals(jsonPointer));
  }
  
  @Test
  public void testJsonPointerWithEscapedSymbols() {
    JsonPointer jsonPointer = new JsonPointer("~0", null, null);
    
    assertFalse(jsonPointer.matches());
    assertEquals("~0", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertTrue(jsonPointer.mayMatchProperty());
    assertFalse(jsonPointer.mayMatchElement());
    assertNotNull(jsonPointer.matchProperty("~0"));
    assertNull(jsonPointer.matchProperty("~"));
    assertNull(jsonPointer.matchElement(0));
    assertNull(jsonPointer.tail());
    assertEquals("~0", jsonPointer.toString());
    assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    assertTrue(jsonPointer.equals(jsonPointer));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonPointer() {
    JsonPointer jsonPointer = JsonPointer.compile("property");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonPointerEmptyInput() {
    JsonPointer jsonPointer = JsonPointer.compile("");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonPointerNonSlashStarInput() {
    JsonPointer jsonPointer = JsonPointer.compile("*property");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonPointerNonSlashSlashInput() {
    JsonPointer jsonPointer = JsonPointer.compile("//property");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidJsonPointerNonDigitIndex() {
    JsonPointer jsonPointer = JsonPointer.compile("/property/abc");
  }
  
}

