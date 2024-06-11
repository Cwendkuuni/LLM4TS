package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testAttributeCreation() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = createAttribute(key, value);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 }

 @Test
 public void testSettingKey() {
 String key = "testKey";
 String newKey = "newTestKey";
 Attribute attribute = createAttribute(key, null);
 attribute.setKey(newKey);
 Assert.assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = createAttribute(key, value);
 String newValue = "newTestValue";
 attribute.setValue(newValue);
 Assert.assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("testKey", "testValue");
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "testkey=\"testValue\"";
 Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = createAttribute("test", "");
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));

 attribute.setKey("testBool");
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));

 attribute.setKey("test");
 attribute.setValue("test");
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));

 attribute.setValue("testValue");
 Assert.assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test(expected = SerializationException.class)
 public void testIOExceptionInHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("testKey", null);
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = createAttribute("testBool", null);
 Assert.assertTrue(attribute.isBooleanAttribute());

 attribute.setKey("test");
 Assert.assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = createAttribute("data-test", null);
 Assert.assertTrue(attribute.isDataAttribute());

 attribute.setKey("test");
 Assert.assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testEquals() {
 String key1 = "testKey1";
 String value1 = "testValue1";
 Attribute attribute1 = createAttribute(key1, value1);

 String key2 = "testKey2";
 String value2 = "testValue2";
 Attribute attribute2 = createAttribute(key2, value2);

 String key3 = "testKey1";
 String value3 = "testValue1";
 Attribute attribute3 = createAttribute(key3, value3);

 String key4 = "testKey1";
 String value4 = "testValue3";
 Attribute attribute4 = createAttribute(key4, value4);

 Attribute[] attributes = { attribute1, attribute2, attribute3, attribute4 };
 for (int i = 0; i < attributes.length; i++) {
 for (int j = i + 1; j < attributes.length; j++) {
 Assert.assertNotEquals(attributes[i], attributes[j]);
 }
 }

 Assert.assertEquals(attribute1, attribute3); // same key and value
 }

 @Test
 public void testHashCode() {
 String key1 = "testKey1";
 String value1 = "testValue1";
 Attribute attribute1 = createAttribute(key1, value1);
 String key2 = "testKey2";
 String value2 = "testValue2";
 Attribute attribute2 = createAttribute(key2, value2);
 String key3 = "testKey1";
 String value3 = "testValue1";
 Attribute attribute3 = createAttribute(key3, value3);
 String key4 = "testKey1";
 String value4 = "testValue2";
 Attribute attribute4 = createAttribute(key4, value4);

 int[] hashCodes = { attribute1.hashCode(), attribute2.hashCode(), attribute3.hashCode(), attribute4.hashCode() };
 Arrays.sort(hashCodes);
 Assert.assertArrayEquals(new int[] { attribute1.hashCode(), attribute3.hashCode(), attribute2.hashCode(), attribute4.hashCode() }, hashCodes);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = createAttribute("testKey", "testValue");
 Attribute clonedAttribute = (Attribute) attribute.clone();
 Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
 Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneException() throws CloneNotSupportedException {
 Attribute attribute = createAttribute("testKey", "testValue");
 Attribute clonedAttribute = (Attribute) attribute.clone();
 clonedAttribute.clone();
 }

}