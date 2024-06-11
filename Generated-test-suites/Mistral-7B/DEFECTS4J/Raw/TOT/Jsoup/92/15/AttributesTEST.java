package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testGetEmptyAttribute() {
 String emptyAttribute = attributes.get("empty");
 assertEquals("", emptyAttribute);
 }

 @Test
 public void testGetAttribute() {
 attributes.put("testKey", "testValue");
 String retrievedAttribute = attributes.get("testKey");
 assertEquals("testValue", retrievedAttribute);
 }

 @Test
 public void testGetIgnoreCaseAttribute() {
 attributes.put("TestKey", "TestValue");
 String retrievedAttributeIgnoreCase = attributes.getIgnoreCase("testKey");
 assertEquals("TestValue", retrievedAttributeIgnoreCase);
 }

 @Test
 public void testPutAttribute() {
 attributes.put("testKey", "testValue");
 Attributes clone = attributes.clone();
 clone.put("testKey", "newTestValue");
 assertEquals("testValue", attributes.get("testKey"));
 assertEquals("newTestValue", clone.get("testKey"));
 }

 @Test
 public void testPutIgnoreCaseAttribute() {
 attributes.put("TestKey", "TestValue");
 Attributes clone = attributes.clone();
 clone.putIgnoreCase("testKey", "newTestValue");
 assertEquals("TestValue", attributes.get("TestKey"));
 assertEquals("newTestValue", clone.get("testKey"));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullAttributeKey() {
 attributes.put(null, "testValue");
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullAttributeValue() {
 attributes.put("testKey", null);
 }

 @Test
 public void testAddAllAttributes() {
 Attributes newAttributes = new Attributes();
 newAttributes.put("newKey1", "newValue1");
 newAttributes.put("newKey2", "newValue2");
 attributes.addAll(newAttributes);
 assertEquals(3, attributes.size());
 assertEquals("newValue1", attributes.get("newKey1"));
 assertEquals("newValue2", attributes.get("newKey2"));
 }

 @Test
 public void testHasKeyAttribute() {
 attributes.put("testKey", "testValue");
 assertTrue(attributes.hasKey("testKey"));
 }

 @Test
 public void testHasKeyIgnoreCaseAttribute() {
 attributes.put("TestKey", "TestValue");
 assertTrue(attributes.hasKeyIgnoreCase("testKey"));
 }

 @Test
 public void testHasKeyNonExistentAttribute() {
 assertFalse(attributes.hasKey("nonExistentKey"));
 }

 @Test
 public void testRemoveAttribute() {
 attributes.put("testKey", "testValue");
 attributes.remove("testKey");
 assertFalse(attributes.hasKey("testKey"));
 }

 @Test
 public void testRemoveIgnoreCaseAttribute() {
 attributes.put("TestKey", "TestValue");
 attributes.removeIgnoreCase("testKey");
 assertFalse(attributes.hasKey("TestKey"));
 }

 @Test
 public void testRemoveNonExistentAttribute() {
 assertFalse(attributes.hasKey("nonExistentKey"));
 attributes.remove("nonExistentKey");
 assertFalse(attributes.hasKey("nonExistentKey"));
 }

 @Test
 public void testPutBooleanAttribute() {
 attributes.put("testKey", true);
 assertTrue(attributes.hasKey("testKey"));
 assertFalse(attributes.get("testKey").equals(""));
 attributes.put("testKey", false);
 assertFalse(attributes.hasKey("testKey"));
 }

 @Test
 public void testPutBooleanIgnoreCaseAttribute() {
 attributes.put("TestKey", true);
 assertTrue(attributes.hasKeyIgnoreCase("testKey"));
 assertFalse(attributes.getIgnoreCase("testKey").equals(""));
 attributes.put("TestKey", false);
 assertFalse(attributes.hasKeyIgnoreCase("testKey"));
 }

 @Test
 public void testPutOverwriteAttribute() {
 attributes.put("testKey", "testValue1");
 attributes.put("testKey", "testValue2");
 assertEquals("testValue2", attributes.get("testKey"));
 }

 @Test
 public void testPutOverwriteIgnoreCaseAttribute() {
 attributes.put("TestKey", "TestValue1");
 attributes.put("testKey", "TestValue2");
 assertEquals("TestValue2", attributes.getIgnoreCase("TestKey"));
 }

 @Test
 public void testPutDuplicateAttribute() {
 attributes.put("testKey", "testValue1");
 attributes.put("testKey", "testValue2");
 assertEquals("testValue2", attributes.get("testKey"));
 }

 @Test
 public void testPutDuplicateIgnoreCaseAttribute() {
 attributes.put("TestKey", "TestValue1");
 attributes.put("testKey", "TestValue2");
 assertEquals("TestValue2", attributes.getIgnoreCase("TestKey"));
 }

 @Test
 public void testDatasetEmpty() {
 Map<String, String> dataset = attributes.dataset();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testDatasetNotEmpty() {
 attributes.put("testDataKey1", "testDataValue1");
 attributes.put("testDataKey2", "testDataValue2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("testDataValue1", dataset.get("testDataKey1"));
 assertEquals("testDataValue2", dataset.get("testDataKey2"));
 }

 @Test
 public void testDatasetDuplicateKey() {
 attributes.put("testDataKey1", "testDataValue1");
 attributes.put("testDataKey1", "testDataValue2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(1, dataset.size());
 assertEquals("testDataValue2", dataset.get("testDataKey1"));
 }

 @Test
 public void testDatasetIgnoreCaseDuplicateKey() {
 attributes.put("TestDataKey1", "TestDataValue1");
 attributes.put("testDataKey1", "TestDataValue2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(1, dataset.size());
 assertEquals("TestDataValue2", dataset.get("testDataKey1"));
 }
}