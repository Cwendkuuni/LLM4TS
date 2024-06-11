package org.jsoup.nodes;
import org.junit.Test;

public class AttributeTest {
  
  @Test
  public void testConstructor() {
    Attribute attribute = new Attribute("key", "value");
    assert(attribute.getKey().equals("key"));
    assert(attribute.getValue().equals("value"));
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    assert(attribute.getKey().equals("newKey"));
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
    String html = attribute.html();
    assert(html.equals("key=\"value\""));
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String stringRep = attribute.toString();
    assert(stringRep.equals("key=\"value\""));
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
    assert(attribute.getKey().equals("key"));
    assert(attribute.getValue().equals("\"value\""));
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    assert(attribute.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "value");
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
    assert(!shouldCollapse);
  }
  
  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("checked", null);
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
    int hashCode = attribute.hashCode();
    assert(hashCode == attribute.getKey().hashCode() + attribute.getValue().hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clone = attribute.clone();
    assert(clone.getKey().equals(attribute.getKey()));
    assert(clone.getValue().equals(attribute.getValue()));
  }
}