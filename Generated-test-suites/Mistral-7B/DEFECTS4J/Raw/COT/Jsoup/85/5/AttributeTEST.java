package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes.NotFound;
import org.junit.Test;

public class AttributeTest {

 private static Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testAttributeCreation() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = createAttribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyAttributeCreation() {
 String key = null;
 String value = "testValue";
 Attribute attribute = createAttribute(key, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyAttributeCreation() {
 String key = "";
 String value = "testValue";
 Attribute attribute = createAttribute(key, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueAttributeCreation() {
 String key = "testKey";
 String value = null;
 Attribute attribute = createAttribute(key, value);
 }

 @Test
 public void testSetKey() {
 String oldKey = "oldKey";
 String newKey = "newKey";
 Attribute attribute = createAttribute(oldKey, null);
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 assertNull(attribute.getOldKey());
 }

 @Test
 public void testGetOldKey() {
 String oldKey = "oldKey";
 String newKey = "newKey";
 Attribute attribute = createAttribute(oldKey, null);
 attribute.setKey(newKey);
 assertSame(oldKey, attribute.getOldKey());
 }

 @Test
 public void testValueSetter() {
 String key = "testKey";
 String oldValue = "oldValue";
 String newValue = "newValue";
 Attribute attribute = createAttribute(key, oldValue);
 String actualValue = attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueSetter() {
 String key = "testKey";
 Attribute attribute = createAttribute(key, null);
 attribute.setValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyValueSetter() {
 String key = "testKey";
 Attribute attribute = createAttribute(key, null);
 attribute.setValue("");
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 Attribute attribute = createAttribute("test", null);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setKey("test");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testCreateFromEncoded() {
 String unencodedKey = "data-test";
 String encodedValue = "data-test=\"test\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 assertEquals(unencodedKey, attribute.getKey());
 assertEquals("test", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullUnencodedKeyCreateFromEncoded() {
 String encodedValue = "data-test=\"test\"";
 Attribute attribute = Attribute.createFromEncoded(null, encodedValue);
 }

 @Test
 public void testEmptyUnencodedKeyCreateFromEncoded() {
 String encodedValue = "data-test=\"test\"";
 Attribute attribute = Attribute.createFromEncoded("", encodedValue);
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = createAttribute("checked", null);
 assertTrue(attribute.isBooleanAttribute());
 attribute.setKey("notBoolean");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = createAttribute("data-test", null);
 assertTrue(attribute.isDataAttribute());
 attribute.setKey("test");
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute original = createAttribute("testKey", "testValue");
 Attribute clone = original.clone();
 assertEquals(original.getKey(), clone.getKey());
 assertEquals(original.getValue(), clone.getValue());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = createAttribute("testKey1", "testValue1");
 Attribute attribute2 = createAttribute("testKey1", "testValue1");
 Attribute attribute3 = createAttribute("testKey2", "testValue2");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testGetHashCode() {
 Attribute attribute1 = createAttribute("testKey1", "testValue1");
 Attribute attribute2 = createAttribute("testKey2", "testValue2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Attribute attribute = createAttribute("test", "testValue");
 assertEquals(attribute.html(), attribute.toString());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlThrowsSerializationException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("test", null);
 attribute.html(sb, null);
 }

 @Test
 public void testCreateFromEncodedWithNullValue() {
 String unencodedKey = "data-test";
 String encodedValue = "data-test=";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 assertNull(attribute.getValue());
 }

 @Test
 public void testIsBooleanAttributeWithEmptyValue() {
 Attribute attribute = createAttribute("checked", "");
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithNullValue() {
 Attribute attribute = createAttribute("checked", null);
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 Attribute attribute = createAttribute("checked", "");
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeValue() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 Attribute attribute = createAttribute("checked", "checked");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithNonBooleanAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 Attribute attribute = createAttribute("test", "test");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testHtmlThrowsIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("test", null);
 attribute.html(sb, null);
 }

 @Test
 public void testHtmlWithEmptyParent() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Element parent = new Element("div");
 Attribute attribute = new Attribute("test", "test");
 attribute.html(sb, null);
 String result = StringUtil.releaseBuilder(sb);
 assertEquals("test=\"test\"", result);
 }

 @Test
 public void testHtmlWithParent() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Element parent = new Element("div");
 Attribute attribute = new Attribute("test", "test");
 attribute.html(sb, parent.outputSettings());
 String result = StringUtil.releaseBuilder(sb);
 assertEquals("test=\"test\"", result);
 }

 @Test
 public void testHtmlWithParentAndEncodedValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Element parent = new Element("div");
 Attribute attribute = new Attribute("test", "test%20value");
 attribute.html(sb, parent.outputSettings());
 String result = StringUtil.releaseBuilder(sb);
 assertEquals("test=\"test value\"", result);
 }

 @Test
 public void testHtmlWithParentAndOutputSettings() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Element parent = new Element("div");
 Attribute attribute = new Attribute("test", "test");
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml);
 attribute.html(sb, outputSettings);
 String result = StringUtil.releaseBuilder(sb);
 assertEquals("test=\"test\"", result);
 }

 @Test
 public void testHtmlWithParentAndOutputSettingsAndNoCollapse() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Element parent = new Element("div");
 Attribute attribute = new Attribute("checked", null);
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 attribute.html(sb, outputSettings, true);
 String result = StringUtil.releaseBuilder(sb);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testHtmlWithParentAndOutputSettingsAndCollapse() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Element parent = new Element("div");
 Attribute attribute = new Attribute("checked", null);
 Document.OutputSettings outputSettings = Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html);
 attribute.html(sb, outputSettings, false);
 String result = StringUtil.releaseBuilder(sb);
 assertEquals("checked", result);
 }

 @Test
 public void testIndexOfKeyWithFoundAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 Attribute attribute3 = new Attribute("test1", "test1Value");
 attributes.put(attribute1);
 attributes.put(attribute2);
 attributes.put(attribute3);
 int index = attributes.indexOfKey(attribute3.getKey());
 assertEquals(0, index);
 }

 @Test
 public void testIndexOfKeyWithNotFoundAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 int index = attributes.indexOfKey("test3");
 assertEquals(Attributes.NotFound, index);
 }

 @Test
 public void testKeysAndValsWithSingleAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("test", "test");
 attributes.put(attribute);
 Attribute[] keys = attributes.keySet().toArray(new Attribute[0]);
 Attribute[] values = attributes.values().toArray(new Attribute[0]);
 assertEquals(1, keys.length);
 assertEquals(1, values.length);
 assertEquals(attribute, keys[0]);
 assertEquals(attribute, values[0]);
 }

 @Test
 public void testKeysAndValsWithMultipleAttributes() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 Attribute attribute3 = new Attribute("test1", "test1Value2");
 attributes.put(attribute1);
 attributes.put(attribute2);
 attributes.put(attribute3);
 Attribute[] keys = attributes.keySet().toArray(new Attribute[0]);
 Attribute[] values = attributes.values().toArray(new Attribute[0]);
 assertEquals(3, keys.length);
 assertEquals(3, values.length);
 assertNotSame(attribute1, attribute2);
 assertNotSame(attribute1, attribute3);
 assertNotSame(attribute2, attribute3);
 assertEquals(attribute1, keys[0]);
 assertEquals(attribute1, values[0]);
 assertEquals(attribute2, keys[1]);
 assertEquals(attribute2, values[1]);
 assertEquals(attribute3, keys[2]);
 assertEquals(attribute3, values[2]);
 }

 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 Attribute attribute3 = new Attribute("test1", "test1Value2");
 attributes.put(attribute1);
 attributes.put(attribute2);
 attributes.put(attribute3);
 assertEquals(3, attributes.size());
 attributes.remove(attribute1);
 assertEquals(2, attributes.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAttributeTwice() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 Attribute attribute3 = new Attribute("test1", "test1Value2");
 attributes.put(attribute1);
 attributes.put(attribute2);
 attributes.put(attribute3);
 assertEquals(3, attributes.size());
 attributes.remove(attribute1);
 assertEquals(2, attributes.size());
 attributes.remove(attribute1);
 }

 @Test
 public void testClear() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 Attribute attribute3 = new Attribute("test1", "test1Value2");
 attributes.put(attribute1);
 attributes.put(attribute2);
 attributes.put(attribute3);
 assertEquals(3, attributes.size());
 attributes.clear();
 assertEquals(0, attributes.size());
 }

 @Test
 public void testPutIfAbsent() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test1", "test1Value2");
 assertEquals(null, attributes.putIfAbsent(attribute1, attribute2));
 assertEquals(attribute1, attributes.get("test1"));
 }

 @Test
 public void testPutIfAbsentWithNullAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 assertEquals(null, attributes.putIfAbsent(null, attribute1));
 assertEquals(attribute1, attributes.get("test1"));
 }

 @Test
 public void testPutIfAbsentWithSameKeyDifferentValue() {
 Attributes attributes = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test1", "test1Value2");
 assertEquals(attribute1, attributes.putIfAbsent(attribute1, attribute2));
 assertEquals(attribute2, attributes.get("test1"));
 }

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("test", "test");
 attributes.put(attribute);
 assertEquals(attribute, attributes.get("test"));
 }

 @Test
 public void testGetWithNullKey() {
 Attributes attributes = new Attributes();
 assertEquals(null, attributes.get(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNonExistentKey() {
 Attributes attributes = new Attributes();
 assertEquals(null, attributes.get("nonExistentKey"));
 }

 @Test
 public void testPutAll() {
 Attributes attributes1 = new Attributes();
 Attribute attribute1 = new Attribute("test1", "test1Value");
 Attribute attribute2 = new Attribute("test2", "test2Value");
 Attributes attributes2 = new Attributes();
 attributes2.put(attribute1);
 attributes2.put(attribute2);
 attributes1.putAll(attributes2);
 assertEquals(2, attributes1.size());
 }

 @Test
 public void testPutAllWithNullAttributes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = null;
 attributes1.putAll(attributes2);
 assertEquals(0, attributes1.size());
 }

 @Test
 public void testPutAllWithSameAttributes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.putAll(attributes2);
 assertEquals(0, attributes1.size());
 }

}