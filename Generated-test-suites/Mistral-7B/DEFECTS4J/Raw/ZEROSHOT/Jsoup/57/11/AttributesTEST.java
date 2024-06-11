package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttributesTest {

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String value = attributes.get("key");
 assertEquals("value", value);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String value = attributes.getIgnoreCase("key");
 assertEquals("value", value);
 }

 @Test
 public void testGetWhenNoAttribute() {
 Attributes attributes = new Attributes();
 String value = attributes.get("key");
 assertEquals("", value);
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.get("key").getValue().isEmpty());
 }

 @Test
 public void testPutSameKeyTwice() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
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
 public void testHasKeyWhenNoAttribute() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.removeIgnoreCase("Key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveWhenNoAttribute() {
 Attributes attributes = new Attributes();
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 assertEquals(2, attributes1.size());
 assertTrue(attributes1.hasKey("key1"));
 assertTrue(attributes1.hasKey("key2"));
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(1, attributes.size());
 assertEquals(1, clonedAttributes.size());
 assertTrue(attributes.hasKey("key1"));
 assertTrue(clonedAttributes.hasKey("key1"));
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(1, dataset.size());
 assertTrue(dataset.containsKey("data-key2"));
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append(" key1=\"value1\"");
 String html = attributes.html();
 assertEquals(expectedHtml.toString(), html);
 }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String toString = attributes.toString();
 String html = attributes.html();
 assertEquals(html, toString);
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 assertTrue(attributes1.equals(attributes2));
 }

 @Test
 public void testEqualsWhenDifferentSize() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 assertFalse(attributes1.equals(attributes2));
 }

 @Test
 public void testEqualsWhenDifferentAttributes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value1");
 assertFalse(attributes1.equals(attributes2));
 }

 @Test
 public void testEqualsWhenNull() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.equals(null));
 }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 @Test
 public void testHashCodeWhenDifferentAttributes() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value1");
 assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 @Test
 public void testHashCodeWhenNull() {
 Attributes attributes = new Attributes();
 assertNotEquals(attributes.hashCode(), null);
 }
}