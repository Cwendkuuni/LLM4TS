package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testGetPut() {
 // Test getting and setting an attribute
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testGetIgnoreCase() {
 // Test getting an attribute with case-insensitive key
 attributes.put("Key", "Value");
 Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testAdd() {
 // Test adding multiple attributes
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testPutNullValue() {
 // Test putting a null value
 attributes.put("key", null);
 Assert.assertNull(attributes.get("key"));
 }

 @Test
 public void testPutBoolean() {
 // Test putting a boolean attribute
 attributes.put("key", false);
 Assert.assertFalse(attributes.hasKey("key"));
 attributes.put("key", true);
 Assert.assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testRemove() {
 // Test removing an attribute by key
 attributes.put("key", "value");
 attributes.remove("key");
 Assert.assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 // Test removing an attribute by case-insensitive key
 attributes.put("Key", "Value");
 attributes.removeIgnoreCase("key");
 Assert.assertFalse(attributes.hasKey("Key"));
 }

 @Test
 public void testHasKey() {
 // Test checking if an attribute is present by key
 attributes.put("key", "value");
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertFalse(attributes.hasKey("other"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 // Test checking if an attribute is present by case-insensitive key
 attributes.put("Key", "Value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("Other"));
 }

 @Test
 public void testSize() {
 // Test getting the number of attributes
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals(2, attributes.size());
 }

 @Test(expected = SerializationException.class)
 public void testHtml() {
 // Test getting the HTML representation of the attributes
 attributes.put("key", "value");
 String html = attributes.html();
 }

 @Test
 public void testAddAll() {
 // Test adding all attributes from another Attributes object
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes.addAll(attributes2);
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testDataset() {
 // Test retrieving a filtered view of custom data attributes
 Map<String, String> dataset = attributes.dataset();
 attributes.put("data-key", "data-value");
 Assert.assertEquals("data-value", dataset.get("data-key"));
 }

 @Test
 public void testToString() {
 // Test getting the string representation of the attributes
 Assert.assertEquals("", attributes.toString());
 attributes.put("key", "value");
 Assert.assertEquals("key=\"value\"", attributes.toString());
 }

 @Test
 public void testEquals() {
 // Test comparing Attributes objects
 Attributes attributes3 = new Attributes();
 Attributes attributes4 = new Attributes();
 attributes3.put("key", "value");
 attributes4.put("key", "value");
 Assert.assertEquals(attributes3, attributes4);
 attributes4.put("other", "value");
 Assert.assertNotEquals(attributes3, attributes4);
 }

 @Test
 public void testClone() {
 // Test cloning the Attributes object
 Attributes clonedAttributes = attributes.clone();
 attributes.put("key", "value");
 Assert.assertNotEquals(attributes, clonedAttributes);
 Assert.assertEquals("value", clonedAttributes.get("key"));
 }

 @Test
 public void testNormalize() {
 // Test normalizing all keys
 attributes.put("KEY", "VALUE");
 attributes.normalize();
 Assert.assertEquals("key", attributes.keys()[0]);
 }
}