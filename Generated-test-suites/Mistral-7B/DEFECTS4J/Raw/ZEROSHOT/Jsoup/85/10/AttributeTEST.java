package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value");
 attribute.setParent(attributes);
 }

 @Test
 public void testConstructor() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(null, attribute.getParent());
 }

 @Test
 public void testConstructorWithParent() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attributes, attribute.getParent());
 }

 @Test
 public void testGetKey() {
 assertEquals("key", attribute.getKey());
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
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");

 assertTrue(attribute.equals(attribute));
 assertTrue(attribute3.equals(attribute));
 assertFalse(attribute.equals(attribute1));
 assertFalse(attribute.equals(attribute2));
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 assertEquals("key", clonedAttribute.getKey());
 assertEquals("value", clonedAttribute.getValue());
 assertEquals(attributes, clonedAttribute.getParent());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 assertFalse(new Attribute("key", "").isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithPrefix() {
 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "test", null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 assertFalse(new Attribute("key1", "value1").shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 assertFalse(new Attribute("key1", "").isBooleanAttribute());
 assertFalse(new Attribute("key1", "value1").isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithPrefix() {
 Attribute booleanAttribute = new Attribute(Attributes.booleanPrefix + "test", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }
}