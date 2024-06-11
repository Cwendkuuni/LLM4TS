package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
  
  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("key", attribute.getKey());
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }
  
  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("value", attribute.setValue("newValue"));
    assertEquals("newValue", attribute.getValue());
  }
  
  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("href", "index.html");
    assertEquals("href=\"index.html\"", attribute.html());
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
    assertEquals("key", attribute.getKey());
    assertEquals("encodedValue", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-attr", "value");
    assertTrue(attribute.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Document.OutputSettings out = new Document.OutputSettings();
    Attribute attribute = new Attribute("name", "");
    assertTrue(attribute.shouldCollapseAttribute(out));
  }
  
  @Test
  public void testIsBooleanAttribute() {
    assertTrue(Attribute.isBooleanAttribute("checked"));
  }
  
  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    assertEquals(attribute1, attribute2);
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals(attribute.hashCode(), attribute.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clonedAttribute = attribute.clone();
    assertEquals(attribute, clonedAttribute);
  }
}