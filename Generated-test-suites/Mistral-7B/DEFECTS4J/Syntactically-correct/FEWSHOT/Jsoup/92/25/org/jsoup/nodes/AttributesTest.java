package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttributesTest {

 private Attributes createAttributes() {
 return new Attributes();
 }

 @Test
 public void testSize() {
 Attributes attributes = createAttributes();
 assertEquals(0, attributes.size());

 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key1", "value3"); // duplicate key

 assertEquals(3, attributes.size());
 }

 @Test
 public void testPutGet() {
 Attributes attributes = createAttributes();

 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = createAttributes();

 attributes.put("key1", "value1");
 attributes.put("Key2", "value2");

 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("Key2"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = createAttributes();

 attributes.put("booleanKey", true);
 assertTrue(attributes.hasKey("booleanKey"));
 assertFalse(attributes.get("booleanKey").isEmpty());

 attributes.put("booleanKey", false);
 assertFalse(attributes.hasKey("booleanKey"));
 }

 @Test
 public void testPutAttribute() {
 Attributes attributes = createAttributes();
 Attribute attribute = new Attribute("key", "value");

 attributes.put(attribute);
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = createAttributes();

 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key1", "value3"); // duplicate key

 assertEquals(3, attributes.size());

 attributes.remove("key1");
 assertEquals(2, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = createAttributes();

 attributes.put("key1", "value1");
 attributes.put("Key2", "value2");

 assertEquals(2, attributes.size());

 attributes.remove("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("key1"));

 attributes.remove("Key2");
 assertEquals(0, attributes.size());
 assertFalse(attributes.hasKey("Key2"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = createAttributes();

 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));

 assertFalse(attributes.hasKey("key3"));
 assertFalse(attributes.hasKeyIgnoreCase("KEY3"));
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = createAttributes();
 Attributes attributes2 = createAttributes();

 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");

 attributes2.put("key2", "value3");
 attributes2.put("key3", "value3");

 attributes1.addAll(attributes2);

 assertEquals(4, attributes1.size());
 assertTrue(attributes1.hasKey("key1"));
 assertTrue(attributes1.hasKey("key2"));
 assertTrue(attributes1.hasKey("key3"));
 }

 @Test
 public void testIterator() {
 Attributes attributes = createAttributes();

 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Attribute> attributesList = attributes.asList();

 for (Attribute attribute : attributesList) {
 System.out.println(attribute.getKey() + " = " + attribute.getValue());
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = createAttributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");

 Map<String, String> dataset = attributes.dataset();

 assertEquals("value1", dataset.get("data-key1"));
 assertEquals("value2", dataset.get("data-key2"));
 }

 @Test
 public void testHtml() {
 Attributes attributes = createAttributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 StringBuilder sb = new StringBuilder();
 attributes.html(sb, null);

 assertEquals("key1=\"value1\" key2=\"value2\"", sb.toString());
 }
}