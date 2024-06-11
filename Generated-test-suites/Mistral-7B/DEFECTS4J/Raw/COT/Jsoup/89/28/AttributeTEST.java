package org.jsoup.nodes.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testGetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetValueWithNullValue() {
 Attribute attribute = new Attribute("key", null);
 attribute.getValue();
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test(expected = IOException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, null);
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithSerializationException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 throw new IOException();
 }

 @Test
 public void testShouldCollapseAttributeWithNullOut() {
 assertFalse(Attribute.shouldCollapseAttribute(null, "value", null));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyVal() {
 assertTrue(Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings()));
 }

 @Test
 public void testShouldCollapseAttributeWithSameKeyAndValue() {
 assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings()));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(Attribute.shouldCollapseAttribute("checked", null, out));
 assertTrue(Attribute.shouldCollapseAttribute("checked", "", out));
 }

 @Test
 public void testShouldCollapseAttributeWithNonBooleanAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
 }

 @Test
 public void testToString() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncodedWithNullUnencodedKey() {
 assertNull(Attribute.createFromEncoded(null, "value"));
 }

 @Test
 public void testCreateFromEncodedWithNullEncodedValue() {
 assertNull(Attribute.createFromEncoded("key", null));
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isDataAttribute());
 attribute.setKey("data:key");
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 assertTrue(attribute.isBooleanAttribute());
 attribute.setKey("notBoolean");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2); // equals() is transitive, hashCode() should be as well
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}