package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class AttributeTest {

 private static final String KEY = "testKey";
 private static final String VALUE = "testValue";

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute(KEY, VALUE);
 assertEquals(KEY, attribute.getKey());
 assertEquals(VALUE, attribute.getValue());
 assertNull(attribute.getParent());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute(KEY, VALUE, parentAttributes);
 assertEquals(KEY, attribute.getKey());
 assertEquals(VALUE, attribute.getValue());
 assertSame(parentAttributes, attribute.getParent());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.html(sb, (new Document("")).outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("<" + KEY + "=\"" + VALUE + "\">", html);
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(Attribute.shouldCollapseAttribute(out));
 out.syntax(Document.OutputSettings.Syntax.xml);
 assertFalse(Attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-test", VALUE);
 assertTrue(attribute.isDataAttribute());
 Attribute attribute2 = new Attribute("test", VALUE);
 assertFalse(attribute2.isDataAttribute());
 }

 @Test (expected = IllegalStateException.class)
 public void testIsBooleanAttributeDeprecated() {
 Attribute attribute = new Attribute("test", VALUE);
 attribute.isBooleanAttribute();
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attributeBoolean = new Attribute("checked", null);
 assertTrue(attributeBoolean.isBooleanAttribute(KEY));
 Attribute attribute = new Attribute(KEY, VALUE);
 assertFalse(attribute.isBooleanAttribute(KEY));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute(KEY, VALUE);
 Attribute attribute2 = new Attribute(KEY, VALUE);
 assertEquals(attribute1, attribute2);
 Attribute attribute3 = new Attribute("newKey", VALUE);
 assertNotEquals(attribute1, attribute3);
 Attribute attribute4 = new Attribute(KEY, "newValue");
 assertNotEquals(attribute1, attribute4);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute(KEY, VALUE);
 Attribute attribute2 = new Attribute(KEY, VALUE);
 assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Attribute attribute3 = new Attribute("newKey", VALUE);
 assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 Attribute attribute4 = new Attribute(KEY, "newValue");
 assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute(KEY, VALUE);
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

}