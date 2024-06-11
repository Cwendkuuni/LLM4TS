package org.jsoup.nodes;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
  
  @Test
  public void testConstructorAndGetters() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("key", attribute.getKey());
    assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("oldKey", "value");
    
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }
  
  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "oldValue");
    
    String oldValue = attribute.setValue("newValue");
    assertEquals("newValue", attribute.getValue());
    assertEquals("oldValue", oldValue);
  }
  
  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("href", "https://www.example.com");
    
    String html = attribute.html();
    assertEquals("href=\"https://www.example.com\"", html);
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("class", "test-class");
    
    String str = attribute.toString();
    assertEquals("class=\"test-class\"", str);
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "https://www.example.com");
    
    assertEquals("key", attribute.getKey());
    assertEquals("https://www.example.com", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    
    assertTrue(attribute.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute1 = new Attribute("checked", null);
    Attribute attribute2 = new Attribute("name", "name");
    
    Document.OutputSettings out = new Document.OutputSettings();
    
    assertTrue(attribute1.shouldCollapseAttribute(out));
    assertFalse(attribute2.shouldCollapseAttribute(out));
  }
  
  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute1 = new Attribute("checked", null);
    Attribute attribute2 = new Attribute("name", "name");
    
    assertTrue(attribute1.isBooleanAttribute());
    assertFalse(attribute2.isBooleanAttribute());
  }
  
  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("class", "test-class");
    Attribute attribute2 = new Attribute("class", "test-class");
    Attribute attribute3 = new Attribute("class", "different-class");
    Attribute attribute4 = new Attribute("different-key", "test-class");
    
    assertTrue(attribute1.equals(attribute2));
    assertFalse(attribute1.equals(attribute3));
    assertFalse(attribute1.equals(attribute4));
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute1 = new Attribute("class", "test-class");
    Attribute attribute2 = new Attribute("class", "test-class");
    
    assertTrue(attribute1.hashCode() == attribute2.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attribute1 = new Attribute("class", "test-class");
    Attribute attribute2 = attribute1.clone();
    
    assertTrue(attribute1 != attribute2);
    assertTrue(attribute1.equals(attribute2));
  }
}