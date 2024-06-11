
package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {
  
  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    Assert.assertEquals("key", attribute.getKey());
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    Assert.assertEquals("newKey", attribute.getKey());
  }
  
  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    Assert.assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setValue("newValue");
    Assert.assertEquals("newValue", attribute.getValue());
  }
  
  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    Assert.assertEquals("key=\"value\"", attribute.html());
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    Assert.assertEquals("key=\"value\"", attribute.toString());
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "value");
    Assert.assertEquals("key", attribute.getKey());
    Assert.assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    Assert.assertTrue(attribute.isDataAttribute());
    Assert.assertTrue(Attribute.isDataAttribute("data-key"));
    Assert.assertFalse(Attribute.isDataAttribute("key"));
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Document.OutputSettings out = new Document.OutputSettings();
    out.syntax(Document.OutputSettings.Syntax.html);
    Attribute attribute1 = new Attribute("key", null);
    Attribute attribute2 = new Attribute("key", "key");
    Attribute attribute3 = new Attribute("key", "");
    Assert.assertTrue(attribute1.shouldCollapseAttribute(out));
    Assert.assertTrue(attribute2.shouldCollapseAttribute(out));
    Assert.assertTrue(attribute3.shouldCollapseAttribute(out));
  }
  
  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute1 = new Attribute("key", null);
    Attribute attribute2 = new Attribute("key", "value");
    Assert.assertTrue(attribute1.isBooleanAttribute());
    Assert.assertFalse(attribute2.isBooleanAttribute());
    Assert.assertTrue(Attribute.isBooleanAttribute("key"));
    Assert.assertFalse(Attribute.isBooleanAttribute("otherKey"));
  }
  
  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    Attribute attribute3 = new Attribute("key", "otherValue");
    Attribute attribute4 = new Attribute("otherKey", "value");
    Assert.assertTrue(attribute1.equals(attribute2));
    Assert.assertFalse(attribute1.equals(attribute3));
    Assert.assertFalse(attribute1.equals(attribute4));
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = attribute1.clone();
    Assert.assertEquals(attribute1.getKey(), attribute2.getKey());
    Assert.assertEquals(attribute1.getValue(), attribute2.getValue());
  }
}
