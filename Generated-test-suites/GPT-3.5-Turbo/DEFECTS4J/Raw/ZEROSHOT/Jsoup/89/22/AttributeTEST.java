package org.jsoup.nodes;

import org.jsoup.*;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.jsoup.expectedexceptions.*;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class AttributeTest {
  
  @Test
  public void testConstructor() {
    Attribute attribute = new Attribute("key", "value");
    assertNotNull(attribute);
    assertEquals("key", attribute.getKey());
    assertEquals("value", attribute.getValue());
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructor_NullKey() {
    Attribute attribute = new Attribute(null, "value");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_EmptyKey() {
    Attribute attribute = new Attribute("", "value");
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("oldKey", "value");
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }
  
  @Test(expected = NullPointerException.class)
  public void testSetKey_NullKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetKey_EmptyKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("");
  }
  
  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    String oldValue = attribute.setValue("newValue");
    assertEquals("value", oldValue);
    assertEquals("newValue", attribute.getValue());
  }
  
  @Test
  public void testHTML() {
    Attribute attribute = new Attribute("key", "value");
    String html = attribute.html();
    assertEquals("key=\"value\"", html);
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String str = attribute.toString();
    assertEquals("key=\"value\"", str);
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "value");
    assertNotNull(attribute);
    assertEquals("key", attribute.getKey());
    assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    assertTrue(attribute.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "key");
    OutputSettings outputSettings = new Document.OutputSettings();
    boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
    assertTrue(shouldCollapse);
  }
  
  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("allowfullscreen", "value");
    assertTrue(attribute.isBooleanAttribute());
  }
  
  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    assertTrue(attribute1.equals(attribute2));
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    int hashCode = attribute.hashCode();
    assertNotNull(hashCode);
  }
  
  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clonedAttribute = attribute.clone();
    assertNotNull(clonedAttribute);
    assertEquals(attribute.getKey(), clonedAttribute.getKey());
    assertEquals(attribute.getValue(), clonedAttribute.getValue());
  }
}