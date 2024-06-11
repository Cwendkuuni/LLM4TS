package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGetAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.get("key");
 assertEquals("value", result);
 }

 @Test
 public void testGetAttributeIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.getIgnoreCase("key");
 assertEquals("value", result);
 }

 @Test
 public void testGetAttributeNotFound() {
 Attributes attributes = new Attributes();
 String result = attributes.get("key");
 assertEquals("", result);
 }

 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1").put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testPutAttributeIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value1").put("key", "Value2");
 assertEquals("Value2", attributes.get("key"));
 }

 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", false);
 assertTrue(attributes.hasKey("key"));
 assertTrue(attributes.get("key").equals(""));
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertTrue(attributes.get("key") == null);
 }

 @Test
 public void testPutAttributeWithNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 assertTrue(attributes.hasKey("key"));
 assertTrue(attributes.get("key").equals(""));
 }

 @Test
 public void testPutDuplicateAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1").put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(null, "value");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutEmptyStringKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put("", "value");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutEmptyArrayKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(new String[0], "value");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 try {
 attributes.put("key", null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("Key"));
 }

 @Test
 public void testHasKeyNotFound() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1").put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.remove(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveEmptyStringKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.remove("");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveEmptyArrayKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.remove(new String[0]);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveAttributeNotFound() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key2");
 assertFalse(attributes.hasKey("key2"));
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1").put("key2", "value2");
 attributes.remove("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 assertFalse(attributes.hasKey("key2"));
 }

 @Test
 public void testClear() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1").put("key2", "value2");
 attributes.clear();
 assertEquals(0, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 assertFalse(attributes.hasKey("key2"));
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 Attributes cloned = attributes.clone();
 attributes.put("key1", "value1");
 assertFalse(attributes.equals(cloned));
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 assertEquals(2, cloned.size());
 assertFalse(attributes.equals(cloned));
 }

 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "Value");
 attributes.normalize();
 assertEquals("key", attributes.get("key"));
 assertEquals("value", attributes.get("key"));
 }
}