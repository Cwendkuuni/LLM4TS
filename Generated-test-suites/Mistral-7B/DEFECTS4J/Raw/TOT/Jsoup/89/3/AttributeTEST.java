package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 Attribute expectedAttribute = new Attribute(key, value, null);
 assertEquals(expectedAttribute, attribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeNullKey() {
 Attribute attribute = new Attribute(null, "testValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeEmptyKey() {
 Attribute attribute = new Attribute("", "testValue");
 }

 @Test
 public void testAttributeTrimmedKey() {
 String key = "   testKey   ";
 Attribute attribute = new Attribute(key, "testValue");
 assertEquals("testKey", attribute.getKey());
 }

 @Test
 public void testAttributeCasePreservedKey() {
 Attribute attribute = new Attribute("testKEY", "testValue");
 assertEquals("testKEY", attribute.getKey());
 }

 @Test
 public void testSettingAttributeKey() {
 Attribute attribute = new Attribute("testKey", "testValue");
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testSettingAttributeKeyToExistingKey() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 Attribute attribute2 = new Attribute("testKey2", "testValue2");
 attributes.put(attribute1);
 Attribute attribute3 = new Attribute("testKey1", "newTestValue");
 attribute2.setKey("testKey1");
 assertEquals("newTestValue", attribute3.getValue());
 assertEquals("testKey2", attribute2.getKey());
 }

 @Test
 public void testAttributeValueIsEmpty() {
 Attribute attribute = new Attribute("testKey", "");
 assertNull(attribute.getValue());
 }

 @Test
 public void testSettingAttributeValue() {
 Attribute attribute = new Attribute("testKey", null);
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testSettingAttributeValueToExistingValue() {
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 Attribute attribute2 = new Attribute("testKey1", "newTestValue");
 assertEquals("testValue1", attribute1.getValue());
 assertEquals("newTestValue", attribute2.getValue());
 }

 @Test
 public void testSettingAttributeValueToNull() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attribute.setValue(null);
 assertNull(attribute.getValue());
 }

 @Test
 public void testSettingAttributeValueToParent() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 attributes.put(attribute1);
 Attribute attribute2 = new Attribute("testKey2", "testValue2");
 attribute1.setParent(attributes);
 attribute2.setValue("newValue");
 assertEquals("newValue", attribute2.getValue());
 assertEquals("testValue1", attribute1.getValue());
 }

 @Test
 public void testAttributeHtml() {
 Attribute attribute = new Attribute("testKey", "testValue");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "testKey=\"testValue\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeHtmlWithParent() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("testKey1", "testValue1");
 attributes.put(attribute1);
 Attribute attribute2 = new Attribute("testKey2", null);
 attribute1.setParent(attributes);
 attribute2.setValue("newValue");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute2.html(sb, new Document("").outputSettings());
 String expectedHtml = "testKey2=\"newValue\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 assertEquals("testValue1", attributes.get("testKey1").getValue());
 }

 @Test
 public void testAttributeToString() {
 Attribute attribute = new Attribute("testKey", "testValue");
 String expectedString = "testKey=\"testValue\"";
 assertEquals(expectedString, attribute.toString());
 }

 @Test
 public void testCreatingAttributeFromEncoded() {
 String encodedKey = "testKey=\"testEncodedValue\"";
 String decodedKey = "testKey";
 Attribute attribute = Attribute.createFromEncoded(decodedKey, encodedKey);
 assertEquals(decodedKey, attribute.getKey());
 assertEquals("testEncodedValue", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("testKey", "testValue");
 assertFalse(attribute.isDataAttribute());
 attribute.setKey("data-test-key");
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Attribute attribute = new Attribute("testKey", "testValue");
 assertTrue(attribute.shouldCollapseAttribute(null));
 attribute.setKey("checked");
 attribute.setValue(null);
 assertTrue(attribute.shouldCollapseAttribute(null));
 attribute.setValue("checked");
 assertTrue(attribute.shouldCollapseAttribute(null));
 attribute.setValue("false");
 assertFalse(attribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("testKey", "testValue");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}