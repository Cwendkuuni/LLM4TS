package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class AttributeTest {

 @Test
 public void testAttributeConstructionWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeConstructionWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(0, attribute.parent.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeConstructionWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeConstructionWithNullKeyAndParent() {
 Attribute attribute = new Attribute(null, "value", new Attributes());
 }

 @Test
 public void testAttributeSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeSetKeyWithNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeSetKeyWithEmptyKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeSetKeyWithEmptyParentAndUpdate() {
 Attribute attribute = new Attribute("key", "value");
 Attributes parent = new Attributes();
 attribute.setKey("newKey");
 parent.put(attribute);
 assertEquals(1, parent.size());
 parent.keys[0] = "";
 assertEquals(0, parent.size());
 }

 @Test
 public void testAttributeGetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeSetValueWithNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testAttributeSetValueAndParentUpdate() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 Attributes parent = new Attributes();
 parent.put(attribute);
 assertEquals(1, parent.size());
 attribute.setValue("newValue");
 assertEquals("newValue", parent.get("key"));
 }

 @Test
 public void testAttributeHtml() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeHtmlWithEmptyValue() throws IOException {
 Attribute attribute = new Attribute("key", "");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeHtmlWithBooleanAttribute() throws IOException {
 Attribute attribute = new Attribute("checked", "");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "checked";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeHtmlWithBooleanAttributeAndEmptyValue() throws IOException {
 Attribute attribute = new Attribute("checked", "");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "checked";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeHtmlWithBooleanAttributeAndValue() throws IOException {
 Attribute attribute = new Attribute("checked", "true");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "checked";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testAttributeToString() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals(attribute.html(), attribute.toString());
 }

 @Test
 public void testAttributeCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "myData", "value");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testAttributeShouldCollapseAttribute() {
 Attribute attribute = new Attribute("checked", "checked");
 assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute attribute2 = new Attribute("checked", "");
 assertTrue(attribute2.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute attribute3 = new Attribute("key", "value");
 assertFalse(attribute3.shouldCollapseAttribute(new Document.OutputSettings()));
 }

 @Test(expected = IllegalStateException.class)
 public void testAttributeShouldCollapseAttributeWithNullParent() {
 Attribute attribute = new Attribute("checked", "");
 assertTrue(attribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testAttributeIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", "checked");
 assertTrue(attribute.isBooleanAttribute());
 Attribute attribute2 = new Attribute("key", "value");
 assertFalse(attribute2.isBooleanAttribute());
 }

 @Test
 public void testAttributeEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testAttributeClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}