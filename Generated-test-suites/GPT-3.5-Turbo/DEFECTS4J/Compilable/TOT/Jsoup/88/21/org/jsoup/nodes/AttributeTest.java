package org.jsoup.nodes;
import org.junit.*;
import static org.junit.Assert.*;

public class AttributeTest {

  @Test
  public void testGetKey() {
    Attribute attr = new Attribute("key1", "value1");
    assertEquals("key1", attr.getKey());
  }

  @Test
  public void testSetKey() {
    Attribute attr = new Attribute("key1", "value1");
    attr.setKey("newKey");
    assertEquals("newKey", attr.getKey());
  }

  @Test
  public void testGetValue() {
    Attribute attr = new Attribute("key1", "value1");
    assertEquals("value1", attr.getValue());
  }

  @Test
  public void testSetValue() {
    Attribute attr = new Attribute("key1", "value1");
    attr.setValue("newValue");
    assertEquals("newValue", attr.getValue());
  }

  @Test
  public void testHtml() {
    Attribute attr = new Attribute("key1", "value1");
    assertEquals("key1=\"value1\"", attr.html());
  }

  @Test
  public void testToString() {
    Attribute attr = new Attribute("key1", "value1");
    assertEquals("key1=\"value1\"", attr.toString());
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute attr = Attribute.createFromEncoded("key1", "value1");
    assertEquals("key1", attr.getKey());
    assertEquals("value1", attr.getValue());
  }

  @Test
  public void testIsDataAttribute() {
    Attribute attr = new Attribute("data-key", "value");
    assertTrue(attr.isDataAttribute());
  }

  @Test
  public void testShouldCollapseAttribute() {
    Attribute attr = new Attribute("key1", "value1");
    Document.OutputSettings out = new Document.OutputSettings();
    assertFalse(attr.shouldCollapseAttribute(out));
  }

  @Test
  public void testIsBooleanAttribute() {
    Attribute attr = new Attribute("checked", null);
    assertTrue(attr.isBooleanAttribute());
  }

  @Test
  public void testEquals() {
    Attribute attr1 = new Attribute("key1", "value1");
    Attribute attr2 = new Attribute("key1", "value1");
    Attribute attr3 = new Attribute("key2", "value2");
    assertTrue(attr1.equals(attr2));
    assertFalse(attr1.equals(attr3));
  }

  @Test
  public void testHashCode() {
    Attribute attr1 = new Attribute("key1", "value1");
    Attribute attr2 = new Attribute("key1", "value1");
    assertEquals(attr1.hashCode(), attr2.hashCode());
  }

  @Test
  public void testClone() {
    Attribute attr = new Attribute("key1", "value1");
    Attribute clonedAttr = attr.clone();
    assertEquals(attr.getKey(), clonedAttr.getKey());
    assertEquals(attr.getValue(), clonedAttr.getValue());
  }
  
  // Additional test cases can be added here for all the methods in the Attribute class
  
}