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
    Attribute attribute = new Attribute("oldKey", "value");
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
    Attribute attribute = new Attribute("key", "oldValue");
    String oldValue = attribute.setValue("newValue");
    Assert.assertEquals("oldValue", oldValue);
    Assert.assertEquals("newValue", attribute.getValue());
  }

  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    String expectedHtml = "key=\"value\"";
    Assert.assertEquals(expectedHtml, attribute.html());
  }

  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String expectedHtml = "key=\"value\"";
    Assert.assertEquals(expectedHtml, attribute.toString());
  }

  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    Assert.assertEquals(attribute1, attribute2);
  }

  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    int expectedHashCode = "key".hashCode() * 31 + "value".hashCode();
    Assert.assertEquals(expectedHashCode, attribute.hashCode());
  }

  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clone = attribute.clone();
    Assert.assertEquals(attribute, clone);
  }

  // Test edge cases and exception handling scenarios

  @Test
  public void testEmptyKey() {
    Attribute attribute = new Attribute("", "value");
    Assert.assertEquals("", attribute.getKey());
  }

  @Test(expected = NullPointerException.class)
  public void testNullKey() {
    Attribute attribute = new Attribute(null, "value");
  }

  @Test(expected = NullPointerException.class)
  public void testNullValue() {
    Attribute attribute = new Attribute("key", null);
  }

}