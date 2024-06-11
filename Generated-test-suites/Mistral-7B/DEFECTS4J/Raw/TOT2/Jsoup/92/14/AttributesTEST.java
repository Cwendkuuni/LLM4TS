package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String retrievedValue = attributes.get("key");
 assertEquals("value", retrievedValue);
 }

 // Test case for get() method with case-insensitive key
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String retrievedValue = attributes.getIgnoreCase("Key");
 assertEquals("value", retrievedValue);
 }

 // Test case for get() method with non-existent key
 @Test
 public void testGetNotFound() {
 Attributes attributes = new Attributes();
 String retrievedValue = attributes.get("nonExistentKey");
 assertEquals("", retrievedValue);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 String retrievedValue1 = attributes.get("key1");
 assertEquals("value1", retrievedValue1);

 String retrievedValue2 = attributes.get("key2");
 assertEquals("value2", retrievedValue2);
 }

 // Test case for put() method with existing key
 @Test
 public void testPutExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");

 String retrievedValue = attributes.get("key");
 assertEquals("value2", retrievedValue);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);

 boolean retrievedValue = attributes.getBoolean("key");
 assertTrue(retrievedValue);

 attributes.put("key", false);
 retrievedValue = attributes.getBoolean("key");
 assertFalse(retrievedValue);
 }

 // Test case for put() method with empty value
 @Test
 public void testPutEmptyValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", "");

 assertEquals("", attributes.get("key"));
 }

 // Test case for putIgnoreCase() method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.putIgnoreCase("Key1", "newValue");

 String retrievedValue = attributes.get("key1");
 assertEquals("newValue", retrievedValue);
 }

 // Test case for putIgnoreCase() method with non-existent key
 @Test
 public void testPutIgnoreCaseNotFound() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("nonExistentKey", "newValue");

 assertEquals("", attributes.get("nonExistentKey"));
 }

 // Test case for putIgnoreCase() method with existing key
 @Test
 public void testPutIgnoreCaseExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.putIgnoreCase("key", "value2");

 String retrievedValue = attributes.get("key");
 assertEquals("value2", retrievedValue);
 }

 // Test case for remove() method with existing key
 @Test
 public void testRemoveExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");

 assertEquals("", attributes.get("key"));
 }

 // Test case for remove() method with non-existent key
 @Test
 public void testRemoveNonExistentKey() {
 Attributes attributes = new Attributes();

 assertEquals("", attributes.get("nonExistentKey"));
 attributes.remove("nonExistentKey");

 assertEquals("", attributes.get("nonExistentKey"));
 }

 // Test case for hasKey() method with existing key
 @Test
 public void testHasKeyExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");

 assertTrue(attributes.hasKey("key"));
 }

 // Test case for hasKey() method with non-existent key
 @Test
 public void testHasKeyNonExistentKey() {
 Attributes attributes = new Attributes();

 assertTrue(attributes.hasKey("nonExistentKey") == false);
 }

 // Test case for hasKeyIgnoreCase() method with existing key
 @Test
 public void testHasKeyIgnoreCaseExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");

 assertTrue(attributes.hasKeyIgnoreCase("key"));
 assertTrue(attributes.hasKeyIgnoreCase("Key"));
 }

 // Test case for hasKeyIgnoreCase() method with non-existent key
 @Test
 public void testHasKeyIgnoreCaseNonExistentKey() {
 Attributes attributes = new Attributes();

 assertTrue(attributes.hasKeyIgnoreCase("nonExistentKey") == false);
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 assertEquals(2, attributes.size());
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);

 assertEquals(2, attributes1.size());

 String retrievedValue1 = attributes1.get("key1");
 assertEquals("value1", retrievedValue1);

 String retrievedValue2 = attributes1.get("key2");
 assertEquals("value2", retrievedValue2);
 }

 // Test case for asList() method
 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Attribute> list = attributes.asList();

 assertEquals(2, list.size());

 Attribute firstAttribute = list.get(0);
 assertEquals("key1", firstAttribute.getKey());
 assertEquals("value1", firstAttribute.getValue());

 Attribute secondAttribute = list.get(1);
 assertEquals("key2", secondAttribute.getKey());
 assertEquals("value2", secondAttribute.getValue());
 }

 // Test case for dataset() method
 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("data-key2", "value2");
 attributes.put("data-key3", "value3");

 Map<String, String> dataset = attributes.dataset();

 assertEquals(3, dataset.size());

 String retrievedValue1 = attributes.get("key1");
 assertEquals("value1", retrievedValue1);

 String retrievedValue2 = attributes.get("data-key2");
 assertEquals("value2", retrievedValue2);

 String retrievedValue3 = attributes.get("data-key3");
 assertEquals("value3", retrievedValue3);
 }

 // Test case for html() method
 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append(" key1=\"value1\" key2=\"value2\"");

 StringBuilder actualHtml = new StringBuilder();
 try {
 attributes.html(actualHtml, null);
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }

 assertEquals(expectedHtml.toString(), actualHtml.toString());
 }

 // Test case for equals() method
 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");

 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");

 Attributes attributes3 = new Attributes();
 attributes3.put("key1", "value1");
 attributes3.put("key3", "value3");

 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 }

 // Test case for hashCode() method
 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");

 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");

 Attributes attributes3 = new Attributes();
 attributes3.put("key1", "value1");
 attributes3.put("key3", "value3");

 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
 }

 // Test case for clone() method
 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 Attributes clonedAttributes = attributes.clone();

 assertEquals(attributes.size(), clonedAttributes.size());

 String retrievedValue1 = attributes.get("key1");
 assertEquals(retrievedValue1, clonedAttributes.get("key1"));

 String retrievedValue2 = attributes.get("key2");
 assertEquals(retrievedValue2, clonedAttributes.get("key2"));
 }

 // Test case for normalize() method
 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("KEY2", "value2");

 attributes.normalize();

 String retrievedValue1 = attributes.get("key1");
 assertEquals("value1", retrievedValue1);

 String retrievedValue2 = attributes.get("key2");
 assertEquals("key2", retrievedValue2);
 }
}