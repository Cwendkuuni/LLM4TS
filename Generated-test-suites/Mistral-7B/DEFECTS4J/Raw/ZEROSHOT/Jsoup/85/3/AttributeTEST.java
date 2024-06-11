package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @org.junit.Test
 public void testAttributeCreation() {
 Attribute attr = createAttribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @org.junit.Test
 public void testSettingKey() {
 Attribute attr = createAttribute("key", "value");
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());
 }

 @org.junit.Test
 public void testSettingValue() {
 Attribute attr = createAttribute("key", "value");
 attr.setValue("newValue");
 assertEquals("newValue", attr.getValue());
 }

 @org.junit.Test
 public void testHtml() throws Exception {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attr = createAttribute("key", "value");
 attr.html(sb, new Document("").outputSettings());
 String expected = "<key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @org.junit.Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attr = createAttribute("key", "value");
 assertTrue(attr.shouldCollapseAttribute(out));
 }

 @org.junit.Test
 public void testShouldCollapseAttributeFalse() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attr = createAttribute("key", "nonEmptyValue");
 assertFalse(attr.shouldCollapseAttribute(out));
 }

 @org.junit.Test
 public void testIsBooleanAttribute() {
 Attribute attr = createAttribute("booleanKey", null);
 assertTrue(attr.isBooleanAttribute());
 Attribute attr2 = createAttribute("nonBooleanKey", "value");
 assertFalse(attr2.isBooleanAttribute());
 }

 @org.junit.Test
 public void testIsBooleanAttributeFromString() {
 assertTrue(Attribute.isBooleanAttribute("checked"));
 assertFalse(Attribute.isBooleanAttribute("key"));
 }

 @org.junit.Test
 public void testEquals() {
 Attribute attr1 = createAttribute("key1", "value1");
 Attribute attr2 = createAttribute("key2", "value2");
 Attribute attr3 = createAttribute("key1", "value1");
 assertEquals(attr1, attr3);
 assertNotEquals(attr1, attr2);
 }

 @org.junit.Test
 public void testHashCode() {
 Map<Attribute, Integer> map = new HashMap<>();
 Attribute attr1 = createAttribute("key1", "value1");
 Attribute attr2 = createAttribute("key1", "value1");
 Attribute attr3 = createAttribute("key2", "value2");
 map.put(attr1, 1);
 map.put(attr2, 2);
 map.put(attr3, 3);
 assertEquals(1, map.get(attr1));
 }

 @org.junit.Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attr = createAttribute("key", "value");
 Attribute clonedAttr = attr.clone();
 assertEquals(attr, clonedAttr);
 }

 @org.junit.Test(expected = CloneNotSupportedException.class)
 public void testCloneException() throws CloneNotSupportedException {
 Attribute attr = createAttribute("key", "value");
 Attribute clonedAttr = (Attribute) attr.clone();
 }

 @org.junit.Test
 public void testToString() {
 Attribute attr = createAttribute("key", "value");
 assertEquals(attr.html(), attr.toString());
 }
}