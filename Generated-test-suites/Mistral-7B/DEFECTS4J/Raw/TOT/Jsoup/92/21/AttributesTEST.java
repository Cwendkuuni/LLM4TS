package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testGetEmptyAttributes() {
 Assert.assertEquals("", attributes.get(""));
 }

 @Test
 public void testGetAttributesWithValue() {
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testGetIgnoreCaseAttributes() {
 attributes.put("Key", "value");
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPutAttributes() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");

 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 attributes2.put("key3", "value3");

 attributes = new Attributes();
 attributes.put(attributes1);

 Assert.assertEquals(attributes1.size(), attributes.size());
 for (Attribute attribute : attributes1) {
 Assert.assertEquals(attribute.getValue(), attributes.get(attribute.getKey()));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutInvalidKey() {
 attributes.put("", "value");
 }

 @Test
 public void testPutDuplicateKey() {
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 }

 @Test
 public void testPutBooleanAttribute() {
 attributes.put("booleanKey", true);
 Assert.assertTrue(attributes.hasKey("booleanKey"));
 Assert.assertEquals("", attributes.get("booleanKey"));
 }

 @Test
 public void testPutAttributeWithNullValue() {
 attributes.put("key", null);
 Assert.assertEquals("", attributes.get("key"));
 }

 @Test
 public void testPutIgnoreCaseBooleanAttribute() {
 attributes.put("BooleanKey", true);
 Assert.assertTrue(attributes.hasKeyIgnoreCase("booleanKey"));
 Assert.assertEquals("", attributes.getIgnoreCase("booleanKey"));
 }

 @Test
 public void testRemoveAttribute() {
 attributes.put("key", "value");
 attributes.remove("key");
 Assert.assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCaseAttribute() {
 attributes.put("Key", "value");
 attributes.removeIgnoreCase("key");
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testHasKey() {
 attributes.put("key", "value");
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertFalse(attributes.hasKey("otherKey"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 attributes.put("Key", "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("OtherKey"));
 }

 @Test
 public void testSize() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals(2, attributes.size());
 }

 @Test
 public void testClear() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.clear();
 Assert.assertEquals(0, attributes.size());
 }
}