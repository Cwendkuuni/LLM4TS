package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.PutAttribute;
import org.jsoup.nodes.TextNode;
import org.jsoup.serializer.OutputSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;
 private Document document;
 private Element element;
 private OutputSettings outputSettings;

 @Before
 public void setup() {
 attributes = new Attributes();
 document = new Document();
 outputSettings = new OutputSettings();
 }

 @Test
 public void testCreateFromUnencodedKeyAndValue() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testCreateFromUnencodedKeyAndValueAndParent() {
 String key = "class";
 String value = "test";
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute(key, value, parentAttributes);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertSame(parentAttributes, attribute.parent);
 }

 @Test
 public void testGetKey() {
 String key = "id";
 Attribute attribute = new Attribute(key, null, null);
 assertEquals(key, attribute.getKey());
 }

 @Test
 public void testSetKey() {
 String key = "id";
 Attribute attribute = new Attribute("name", null, null);
 attribute.setKey(key);
 assertEquals(key, attribute.getKey());
 }

 @Test
 public void testGetValue() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value, null);
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testSetValue() {
 String key = "class";
 String oldValue = "test";
 String newValue = "newTest";
 Attribute attribute = new Attribute(key, oldValue, null);
 assertEquals(oldValue, attribute.getValue());
 String newValueBeforeSet = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, newValueBeforeSet);
 }

 @Test
 public void testHtml() throws IOException {
 String key = "href";
 String value = "http://example.com";
 Attribute attribute = new Attribute(key, value);
 StringBuilder sb = new StringBuilder();
 attribute.html(sb, outputSettings);
 assertEquals("href=\"http://example.com\"", sb.toString());
 }

 @Test
 public void testToString() {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value);
 assertEquals("class=test", attribute.toString());
 }

 @Test
 public void testCreateFromEncodedKeyAndValue() {
 String encodedKey = "class=\"test\"";
 String decodedKey = "class";
 String decodedValue = "test";
 Attribute attribute = Attribute.createFromEncoded(decodedKey, encodedKey);
 assertEquals(decodedKey, attribute.getKey());
 assertEquals(decodedValue, attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 String key = "data-test";
 Attribute attribute = new Attribute(key, null);
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 OutputSettings outputSettings = new OutputSettings();
 Attribute attribute = new Attribute("checked", "");
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithNonEmptyValue() {
 OutputSettings outputSettings = new OutputSettings();
 Attribute attribute = new Attribute("checked", "false");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithNonBooleanValue() {
 OutputSettings outputSettings = new OutputSettings();
 Attribute attribute = new Attribute("id", "test");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 String key1 = "class";
 String value1 = "test";
 String key2 = "class";
 String value2 = "test";
 Attribute attribute1 = new Attribute(key1, value1);
 Attribute attribute2 = new Attribute(key2, value2);
 assertTrue(attribute1.equals(attribute2));
 Attribute attribute3 = new Attribute("id", value1);
 assertFalse(attribute1.equals(attribute3));
 Attribute attribute4 = new Attribute(key1, null);
 assertFalse(attribute1.equals(attribute4));
 }

 @Test
 public void testHashCode() {
 String key1 = "class";
 String value1 = "test";
 String key2 = "class";
 String value2 = "test";
 Attribute attribute1 = new Attribute(key1, value1);
 Attribute attribute2 = new Attribute(key2, value2);
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 assertEquals(hashCode1, hashCode2);
 String key3 = "id";
 Attribute attribute3 = new Attribute(key3, value1);
 int hashCode3 = attribute3.hashCode();
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 String key = "class";
 String value = "test";
 Attribute attribute = new Attribute(key, value);
 Attribute clonedAttribute = attribute.clone();
 assertEquals(key, clonedAttribute.getKey());
 assertEquals(value, clonedAttribute.getValue());
 }

 @Test
 public void testPutAttribute() {
 String key = "class";
 String value = "test";
 Element element = new Element("div");
 PutAttribute putAttribute = new PutAttribute(element, key, value);
 assertEquals(putAttribute.getAttributeKey(), key);
 assertEquals(putAttribute.getAttributeValue(), value);
 Attribute attribute = putAttribute.getAttribute();
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testCreateAttributes() {
 List<Map.Entry<String, String>> entries = new ArrayList<>();
 entries.add(new Attribute("id", "test1"));
 entries.add(new Attribute("class", "test2 test3"));
 Attributes attributes = new Attributes(entries);
 assertEquals(2, attributes.size());
 assertEquals("test1", attributes.get("id"));
 assertEquals("test2 test3", attributes.get("class"));
 }

 @Test
 public void testIsBooleanAttribute() {
 String key = "checked";
 Attribute attribute = new Attribute(key, null);
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 attributes.remove("id");
 }

 @Test
 public void testIsBooleanAttributeWithBooleanValue() {
 String key = "checked";
 Attribute attribute = new Attribute(key, "true");
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttributeWithNonDataPrefixKey() {
 String key = "id";
 Attribute attribute = new Attribute(key, null);
 assertFalse(attribute.isDataAttribute());
 }
}