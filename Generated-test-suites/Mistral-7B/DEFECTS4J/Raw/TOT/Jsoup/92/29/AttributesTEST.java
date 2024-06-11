package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGetAttribute() {
 Attributes attributes = new Attributes().put("class", "example");
 assertEquals("example", attributes.get("class"));
 assertEquals("", attributes.get("nonExistentAttribute"));
 }

 @Test
 public void testGetAttributeIgnoreCase() {
 Attributes attributes = new Attributes().put("CLASS", "example");
 assertEquals("example", attributes.getIgnoreCase("class"));
 }

 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("class", "example");
 attributes.put("class", "newExample");
 assertEquals("newExample", attributes.get("class"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Class", "example");
 attributes.putIgnoreCase("class", "newExample");
 assertEquals("newExample", attributes.get("class"));
 }

 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("checked", true);
 assertTrue(attributes.hasKey("checked"));
 assertFalse(attributes.get("checked").isEmpty());
 attributes.put("checked", false);
 assertFalse(attributes.hasKey("checked"));
 }

 @Test
 public void testPutAttributeWithNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("name", null);
 assertNull(attributes.get("name"));
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes().put("key1", "value1").put("key2", "value2");
 Attributes attributes2 = new Attributes().put("key3", "value3");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 assertEquals(3, attributes3.size());
 assertEquals("value1", attributes3.get("key1"));
 assertEquals("value2", attributes3.get("key2"));
 assertEquals("value3", attributes3.get("key3"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.put("key", "newValue");
 }

 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes().put("key", "value").put("key2", "value2");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCaseAttribute() {
 Attributes attributes = new Attributes().put("Key", "value").put("Key2", "value2");
 attributes.removeIgnoreCase("key");
 assertFalse(attributes.hasKey("Key"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes().put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes().put("Key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testHasNoKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes().put("key1", "value1").put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testIsEmpty() {
 Attributes attributes = new Attributes();
 assertTrue(attributes.isEmpty());
 Attributes attributes2 = new Attributes().put("key", "value");
 assertFalse(attributes2.isEmpty());
 }
}