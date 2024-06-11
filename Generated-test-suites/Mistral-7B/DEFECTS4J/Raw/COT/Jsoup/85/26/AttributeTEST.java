package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());

 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertNotNull(attributeWithParent.getParent());
 }

 @Test
 public void testAttributeSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");

 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = AssertionError.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testAttributeSetValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");

 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = AssertionError.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testAttributeHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);

 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testAttributeEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");

 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testAttributeHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");

 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();

 assertEquals(hashCode1, hashCode2); // equals => hashcode should be the same
 assertNotSame(hashCode1, hashCode3); // not equals => hashcode should be different
 }

 @Test
 public void testAttributeClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();

 assertEquals(attribute, clonedAttribute); // should be equal
 assertNotSame(attribute, clonedAttribute); // should not be the same object
 }

 @Test(expected = AssertionError.class)
 public void testCloneNotSupportedException() {
 Attribute attribute = new Attribute("key", "value");
 try {
 Attribute clonedAttribute = (Attribute) attribute.clone();
 } catch (CloneNotSupportedException e) {
 // expected
 }
 }

 @Test
 public void testBooleanAttributes() {
 String[] booleanAttributes = Attribute.booleanAttributes;
 for (String booleanAttribute : booleanAttributes) {
 assertTrue(Attribute.isBooleanAttribute(booleanAttribute));
 }
 }

 @Test
 public void testShouldCollapseAttributeTrue() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute booleanAttribute = new Attribute("checked", "");
 Attribute nonBooleanAttribute = new Attribute("key", "value");

 booleanAttribute.html(sb, outputSettings);
 nonBooleanAttribute.html(sb, outputSettings);

 String html = StringUtil.releaseBuilder(sb);
 assertEquals("checked", html.substring(0, html.indexOf('=')));
 assertEquals("", html.substring(html.indexOf('=') + 1));
 }

 @Test
 public void testShouldCollapseAttributeFalse() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute booleanAttribute = new Attribute("checked", "checked");
 Attribute nonBooleanAttribute = new Attribute("key", "value");

 booleanAttribute.html(sb, outputSettings);
 nonBooleanAttribute.html(sb, outputSettings);

 String html = StringUtil.releaseBuilder(sb);
 assertEquals("checked=\"checked\"", html.substring(0, html.indexOf('=')));
 assertEquals("value", html.substring(html.indexOf('>') + 1));
 }

}