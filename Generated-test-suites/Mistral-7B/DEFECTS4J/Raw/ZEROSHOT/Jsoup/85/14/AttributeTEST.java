package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class AttributeTest {

 private static final String key = "testKey";
 private static final String value = "testValue";
 private static final String encodedValue = "testValue%20%26%20%3D%3D%20testKey";

 @Test
 public void testCreateAttribute() {
 Attribute attribute = new Attribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute(key, value, attributes);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertEquals(attributes, attribute.getParent());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute(key, value);
 attribute.setKey("testKey2");
 assertEquals("testKey2", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute(key, value);
 attribute.setKey(null);
 fail("Should not be able to set key to null");
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute(key, value);
 String newValue = "newValue";
 assertEquals(value, attribute.getValue());
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute(key, value);
 attribute.setValue(null);
 fail("Should not be able to set value to null");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(key, value);
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = String.format("%s=\"%s\"", key, value);
 assertEquals(expectedHtml, sb.toString());
 }

 @Test
 public void testHtmlWithEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(key, "");
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = String.format("%s", key);
 assertEquals(expectedHtml, sb.toString());
 }

 @Test
 public void testHtmlWithBooleanAttribute() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("checked", null);
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = String.format("%s", "checked");
 assertEquals(expectedHtml, sb.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded(key, encodedValue);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute(key, value);
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + key, value);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("nonDataAttribute", value);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute(key, value);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 String emptyValue = "";
 Attribute attributeWithEmptyValue = new Attribute(key, emptyValue);
 assertTrue(attributeWithEmptyValue.shouldCollapseAttribute(outputSettings));
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Attributes attributes = new Attributes();
 attributes.put(attributeWithEmptyValue);
 assertFalse(attributeWithEmptyValue.shouldCollapseAttribute(outputSettings, attributes));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute(key, value);
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute(key, value);
 Attribute attribute2 = new Attribute(key, value);
 Attribute attribute3 = new Attribute("differentKey", value);
 Attribute attribute4 = new Attribute(key, "differentValue");
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute4));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute(key, value);
 Attribute attribute2 = new Attribute(key, value);
 Attribute attribute3 = new Attribute("differentKey", value);
 Attribute attribute4 = new Attribute(key, "differentValue");
 int expectedHashCode = 31 * (31 * key.hashCode() + value.hashCode());
 assertEquals(expectedHashCode, attribute1.hashCode());
 assertEquals(expectedHashCode, attribute2.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute(key, value);
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}