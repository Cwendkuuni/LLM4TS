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
 public void testGet() {
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInvalidKey() {
 attributes.put("key", "value");
 assertNull(attributes.get("invalidKey"));
 }

 @Test
 public void testGetIgnoreCase() {
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPut() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testPutIgnoreCase() {
 attributes.put("Key1", "Value1");
 attributes.put("key2", "value2");
 attributes.putIgnoreCase("Key1", "Value3");
 assertEquals("Value3", attributes.get("Key1"));
 }

 @Test
 public void testPutBoolean() {
 attributes.put("booleanKey", false);
 assertTrue(attributes.hasKey("booleanKey"));
 assertTrue(attributes.get("booleanKey").equals(""));
 attributes.put("booleanKey", true);
 assertFalse(attributes.hasKey("booleanKey"));
 }

 @Test
 public void testPutSameKey() {
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testSize() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testHasKey() {
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 attributes.put("Key1", "Value1");
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test
 public void testHasKeyFalse() {
 assertFalse(attributes.hasKey("doesnotexist"));
 }

 @Test
 public void testRemoveKey() {
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.get("key1"));
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveKeyIgnoreCase() {
 attributes.put("Key1", "Value1");
 assertEquals("Value1", attributes.getIgnoreCase("key1"));
 attributes.removeIgnoreCase("Key1");
 assertFalse(attributes.hasKeyIgnoreCase("key1"));
 }

 @Test
 public void testAddAll() {
 Attributes otherAttributes = new Attributes();
 otherAttributes.put("key1", "value1");
 otherAttributes.put("key2", "value2");
 attributes.addAll(otherAttributes);
 assertEquals(4, attributes.size());
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKey("key2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllNull() {
 Attributes otherAttributes = null;
 attributes.addAll(otherAttributes);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAsList() {
 List<Attribute> attributesList = attributes.asList();
 assertEquals("", attributesList.get(0).getValue());
 assertEquals("", attributesList.get(1).getValue());
 }

 @Test
 public void testDataset() {
 Map<String, String> dataset = attributes.dataset();
 assertEquals("", dataset.get(""));
 }

 @Test
 public void testHtml() {
 StringBuilder output = new StringBuilder();
 attributes.html(output, null);
 String expectedHtml = " key=\"\" key2=\"value2\"";
 assertEquals(expectedHtml, output.toString());
 }
}