package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attr = new Attribute("key", "value");
 Assert.assertEquals("key", attr.getKey());
 Assert.assertEquals("value", attr.getValue());
 }

 @Test
 public void testBooleanAttributes() {
 String[] booleanAttributes = Attribute.booleanAttributes;
 for (String attribute : booleanAttributes) {
 Attribute boolAttr = new Attribute(attribute, "");
 Assert.assertTrue(Attribute.isBooleanAttribute(attribute));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 Attribute attr = new Attribute(null, "value");
 fail("Should not be able to create an Attribute with a null key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attr = new Attribute("", "value");
 fail("Should not be able to create an Attribute with an empty key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValue() {
 Attribute attr = new Attribute("key", null);
 fail("Should not be able to create an Attribute with a null value");
 }

 @Test
 public void testKeySetter() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("newKey");
 Assert.assertEquals("newKey", attr.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeySetter() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("");
 fail("Should not be able to set an empty key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeySetter() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey(null);
 fail("Should not be able to set a null key");
 }

 @Test
 public void testValueSetter() {
 Attribute attr = new Attribute("key", "value");
 attr.setValue("newValue");
 Assert.assertEquals("newValue", attr.getValue());
 }

 @Test
 public void testEquals() {
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key1", "value1");
 Attribute attr3 = new Attribute("key2", "value2");
 Assert.assertEquals(attr1, attr1);
 Assert.assertEquals(attr1, attr2);
 Assert.assertNotEquals(attr1, attr3);
 }

 @Test
 public void testHashCode() {
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key1", "value1");
 Attribute attr3 = new Attribute("key2", "value2");
 Assert.assertEquals(attr1.hashCode(), attr1.hashCode());
 Assert.assertEquals(attr1.hashCode(), attr2.hashCode());
 Assert.assertEquals(attr1.hashCode(), attr1.hashCode());
 Assert.assertNotEquals(attr1.hashCode(), attr3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attr = new Attribute("key", "value");
 Attribute clone = attr.clone();
 Assert.assertEquals(attr, clone);
 }
}