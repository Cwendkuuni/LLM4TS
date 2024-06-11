package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert.*;

import java.util.Arrays;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testAttributeCreation() {
 // Basic test for Attribute constructor with key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 // Basic test for Attribute constructor with key, value and parent
 Attribute attributeWithParent = new Attribute("key", "value", attributes);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertSame(attributes, attributeWithParent.parent);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyEmpty() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

 @Test
 public void testGetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test(expected = IOException.class)
 public void testHtmlIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, null);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-test", "test");
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", "value");
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute());
 Attribute nonBooleanAttribute = new Attribute("nonBoolean", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("nonBoolean", "value");
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttributeWithPrefix() {
 assertTrue(attribute.isDataAttribute("data:test"));
 Attribute dataAttribute = new Attribute("data:test", "test");
 assertTrue(dataAttribute.isDataAttribute("data:test"));
 Attribute nonDataAttribute = new Attribute("key", "value");
 assertFalse(nonDataAttribute.isDataAttribute("data:test"));
 }
}