package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;
 private Document document;

 @Before
 public void setUp() {
 attributes = new Attributes();
 document = new Document();
 attribute = new Attribute("key", "value", attributes);
 }

 @After
 public void tearDown() {
 attribute = null;
 attributes = null;
 document = null;
 }

 @Test
 public void testConstructor_StringString() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 }

 @Test
 public void testConstructor_StringStringAttributes() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertSame(attributes, attribute.getParent());
 }

 @Test
 public void testConstructor_StringStringAttributesNull() {
 Attribute attribute = new Attribute("key", "value", null);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value", attributes);
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute("key", "value", attributes);
 attribute.setKey(null);
 }

 @Test
 public void testGetValue() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("value", attribute.getValue());
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute("key", "value", attributes);
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, document.outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testToString() {
 assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttribute_False() {
 Map<String, String> map = new HashMap<>();
 map.put("key", "value");
 Attribute attribute = new Attribute("key", "value", (Attributes) map);
 assertFalse(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttribute_False_EmptyValue() {
 Attribute attribute = new Attribute("key", "", attributes);
 assertFalse(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttribute_False_DifferentValue() {
 Attribute attribute = new Attribute("key", "value", attributes);
 attribute.setValue("newValue");
 assertFalse(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttribute_True() {
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1", attributes);
 Attribute attribute2 = new Attribute("key2", "value2", attributes);
 assertFalse(attribute1.equals(attribute2));
 Attribute attribute3 = new Attribute("key1", "value1", attributes);
 assertTrue(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1", attributes);
 Attribute attribute2 = new Attribute("key2", "value2", attributes);
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 Attribute attribute3 = new Attribute("key1", "value1", attributes);
 assertEquals(hashCode1, attribute3.hashCode());
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertSame(attribute.getParent(), clonedAttribute.getParent());
 }
}