package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import org.junit.Test;

public class AttributeTest {

 private static final String DATA_PREFIX = "data-";
 private static final String KEY = "testKey";
 private static final String VALUE = "testValue";
 private static final String ENC_VALUE = "testValue%20encoded";
 private static final String ENC_KEY = "data:testKey";

 @Test
 public void testCreateAttributeFromRawKeyValue() {
 Attribute attribute = new Attribute(KEY, VALUE);
 assertEquals(KEY, attribute.getKey());
 assertEquals(VALUE, attribute.getValue());
 }

 @Test
 public void testCreateAttributeFromRawKeyValueWithParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(KEY, VALUE, parent);
 assertEquals(KEY, attribute.getKey());
 assertEquals(VALUE, attribute.getValue());
 assertSame(parent, attribute.parent);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute(KEY, VALUE);
 assertEquals(VALUE, attribute.getValue());
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("testKey=\"testValue\"", sb.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded(KEY, ENC_VALUE);
 assertEquals(KEY, attribute.getKey());
 assertEquals(VALUE, attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("dataTestKey", VALUE);
 assertTrue(attribute.isDataAttribute());
 Attribute attribute2 = new Attribute(KEY, VALUE);
 assertFalse(attribute2.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = new Attribute(KEY, "");
 assertTrue(attribute.shouldCollapseAttribute(out));
 Attribute attribute2 = new Attribute(DATA_PREFIX + KEY, VALUE);
 assertTrue(attribute2.shouldCollapseAttribute(out));
 Attribute attribute3 = new Attribute("booleanAttribute", VALUE);
 assertTrue(attribute3.shouldCollapseAttribute(out));
 Attribute attribute4 = new Attribute(KEY, VALUE);
 assertFalse(attribute4.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("booleanAttribute", VALUE);
 assertFalse(attribute.isBooleanAttribute());
 Attribute attribute2 = new Attribute(KEY, VALUE);
 assertFalse(attribute2.isBooleanAttribute());
 Attribute attribute3 = new Attribute("booleanAttribute", null);
 assertTrue(attribute3.isBooleanAttribute());
 Attribute attribute4 = new Attribute("allowfullscreen", null);
 assertTrue(attribute4.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute(KEY, VALUE);
 Attribute attribute2 = new Attribute(KEY, VALUE);
 Attribute attribute3 = new Attribute(KEY, VALUE);
 Attribute attribute4 = new Attribute(KEY, null);
 Attribute attribute5 = new Attribute("newKey", VALUE);
 Map<String, Attribute> map = new HashMap<>();
 map.put(KEY, attribute1);
 assertEquals(attribute1, attribute2);
 assertEquals(attribute1, attribute3);
 assertNotEquals(attribute1, attribute4);
 assertNotEquals(attribute1, attribute5);
 assertSame(attribute1, map.get(KEY));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute(KEY, VALUE);
 Attribute attribute2 = new Attribute(KEY, VALUE);
 Attribute attribute3 = new Attribute(KEY, VALUE);
 Attribute attribute4 = new Attribute(KEY, null);
 Attribute attribute5 = new Attribute("newKey", VALUE);
 Map<String, Attribute> map = new HashMap<>();
 map.put(KEY, attribute1);
 assertEquals(attribute1.hashCode(), attribute2.hashCode());
 assertEquals(attribute1.hashCode(), attribute3.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute5.hashCode());
 assertSame(attribute1, map.get(KEY));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute(KEY, VALUE);
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertNotSame(attribute.parent, clonedAttribute.parent);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneThrowsCloneNotSupportedException() throws CloneNotSupportedException { // NOPMD
 Attribute attribute = new Attribute(KEY, VALUE);
 }
}