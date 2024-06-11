package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
  
  private Attribute attribute;
  
  @Before
  public void setUp() {
    attribute = new Attribute("test", "value");
  }
  
  @Test
  public void testGetKey() {
    assertEquals("test", attribute.getKey());
  }
  
  @Test
  public void testSetKey() {
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }
  
  @Test
  public void testGetValue() {
    assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testSetValue() {
    attribute.setValue("newValue");
    assertEquals("newValue", attribute.getValue());
  }
  
  @Test
  public void testHtml() {
    assertEquals("test=\"value\"", attribute.html());
  }
  
  @Test
  public void testToString() {
    assertEquals("test=\"value\"", attribute.toString());
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute newAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
    assertEquals("unencodedKey", newAttribute.getKey());
    assertEquals("encodedValue", newAttribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    assertFalse(attribute.isDataAttribute());
  }
  
  @Test
  public void testIsBooleanAttribute() {
    assertFalse(attribute.isBooleanAttribute());
  }
  
  @Test
  public void testEquals() {
    Attribute sameAttribute = new Attribute("test", "value");
    Attribute differentAttribute = new Attribute("different", "value");
    assertTrue(attribute.equals(sameAttribute));
    assertFalse(attribute.equals(differentAttribute));
  }
  
  @Test
  public void testHashCode() {
    Attribute sameAttribute = new Attribute("test", "value");
    assertEquals(attribute.hashCode(), sameAttribute.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute clonedAttribute = attribute.clone();
    assertEquals(attribute.getKey(), clonedAttribute.getKey());
    assertEquals(attribute.getValue(), clonedAttribute.getValue());
  }
}