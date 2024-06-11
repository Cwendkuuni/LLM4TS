package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
  
  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key1", "value1");
    assertEquals("key1", attribute.getKey());
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key1", "value1");
    attribute.setKey("key2");
    assertEquals("key2", attribute.getKey());
  }
  
  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key1", "value1");
    assertEquals("value1", attribute.getValue());
  }
  
  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key1", "value1");
    attribute.setValue("value2");
    assertEquals("value2", attribute.getValue());
  }
  
  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key1", "value1");
    assertEquals("key1=\"value1\"", attribute.html());
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key1", "value1");
    assertEquals("key1=\"value1\"", attribute.toString());
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key1", "value1");
    assertEquals("key1", attribute.getKey());
    assertEquals("value1", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute1 = new Attribute("data-key", "value1");
    assertTrue(attribute1.isDataAttribute());
    
    Attribute attribute2 = new Attribute("key", "value1");
    assertFalse(attribute2.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute1 = new Attribute("key1", "value1");
    assertFalse(attribute1.shouldCollapseAttribute(new Document.OutputSettings()));
    
    Attribute attribute2 = new Attribute("key2", "key2");
    assertTrue(attribute2.shouldCollapseAttribute(new Document.OutputSettings()));
  }
  
  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute1 = new Attribute("allowfullscreen", "value1");
    assertTrue(attribute1.isBooleanAttribute());
    
    Attribute attribute2 = new Attribute("key", null);
    assertTrue(attribute2.isBooleanAttribute());
    
    Attribute attribute3 = new Attribute("key", "value1");
    assertFalse(attribute3.isBooleanAttribute());
  }
  
  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key1", "value1");
    Attribute attribute2 = new Attribute("key1", "value1");
    assertTrue(attribute1.equals(attribute2));
    
    Attribute attribute3 = new Attribute("key1", "value1");
    Attribute attribute4 = new Attribute("key2", "value2");
    assertFalse(attribute3.equals(attribute4));
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute1 = new Attribute("key1", "value1");
    Attribute attribute2 = new Attribute("key1", "value1");
    assertTrue(attribute1.hashCode() == attribute2.hashCode());
    
    Attribute attribute3 = new Attribute("key1", "value1");
    Attribute attribute4 = new Attribute("key2", "value2");
    assertFalse(attribute3.hashCode() == attribute4.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attribute1 = new Attribute("key1", "value1");
    Attribute attribute2 = attribute1.clone();
    assertTrue(attribute1.equals(attribute2));
  }
}