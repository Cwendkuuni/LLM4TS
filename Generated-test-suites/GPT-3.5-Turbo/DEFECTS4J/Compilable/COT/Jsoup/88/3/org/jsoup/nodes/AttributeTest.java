package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.*;

public class AttributeTest {

  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    assert(attribute.getKey().equals("key"));
  }

  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    assert(attribute.getKey().equals("newKey"));
  }

  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    assert(attribute.getValue().equals("value"));
  }

  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setValue("newValue");
    assert(attribute.getValue().equals("newValue"));
  }

  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    assert(attribute.html().equals("key=\"value\""));
  }

  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    assert(attribute.toString().equals("key=\"value\""));
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "value");
    assert(attribute.getKey().equals("key"));
    assert(attribute.getValue().equals("value"));
  }

  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    assert(attribute.isDataAttribute());
  }

  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "value");
    Document.OutputSettings out = new Document.OutputSettings();
    assert(!attribute.shouldCollapseAttribute(out));
  }

  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("key", null);
    assert(attribute.isBooleanAttribute());
  }

  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    assert(attribute1.equals(attribute2));
  }

  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    assert(attribute.hashCode() != 0);
  }

  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clone = attribute.clone();
    assert(clone.getKey().equals("key"));
    assert(clone.getValue().equals("value"));
  }

}