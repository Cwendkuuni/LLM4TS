package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributesTest {

 @Test
 public void testAttributesEmpty() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testAttributesSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals(2, attributes.size());
 }

 @Test
 public void testAttributesGet() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals(EmptyString, attributes.get("key2"));
 }

 @Test
 public void testAttributesGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals(EmptyString, attributes.getIgnoreCase("Key2"));
 }

 @Test
 public void testAttributesPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 Assert.assertEquals("value2", attributes.get("key1"));
 }

 @Test
 public void testAttributesPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("KEY1", "value3");
 Assert.assertEquals("value3", attributes.get("key1")); // case changed, updated
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testAttributesPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("booleanKey", false);
 Assert.assertFalse(attributes.get("booleanKey").equals("booleanKey"));
 Assert.assertTrue(attributes.hasKey("booleanKey"));
 }

 @Test
 public void testAttributesPutBooleanRemove() {
 Attributes attributes = new Attributes();
 attributes.put("booleanKey", true);
 Assert.assertTrue(attributes.get("booleanKey").equals("booleanKey"));
 attributes.put("booleanKey", false);
 Assert.assertFalse(attributes.hasKey("booleanKey"));
 }

 @Test
 public void testAttributesHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Assert.assertTrue(attributes.hasKey("key1"));
 Assert.assertFalse(attributes.hasKey("key2"));
 }

 @Test
 public void testAttributesHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("Key2"));
 }

 @Test
 public void testAttributesRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.remove("key1");
 Assert.assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testAttributesRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.removeIgnoreCase("Key1");
 Assert.assertFalse(attributes.hasKey("Key1"));
 }

 @Test
 public void testAttributesAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 Assert.assertEquals(2, attributes3.size());
 Assert.assertTrue(attributes3.hasKey("key1"));
 Assert.assertTrue(attributes3.hasKey("key2"));
 }

 @Test
 public void testAttributesIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Map<String, String> map = new HashMap<>();
 for (Map.Entry<String, String> entry : attributes) {
 map.put(entry.getKey(), entry.getValue());
 }
 Assert.assertEquals(map, attributes.asList());
 }

 @Test
 public void testAttributesDataset() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");attributes.put("data-key2", "value2");Attributes dataset = attributes.dataset();Assert.assertEquals(2, dataset.size());Assert.assertEquals("value1", attributes.get("key1"));Assert.assertEquals("value2", dataset.get("data-key2"));}

 @Test
 public void testAttributesClone() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes cloned = attributes.clone();
 Assert.assertEquals(attributes, cloned);
 Assert.assertEquals(attributes.hashCode(), cloned.hashCode());
 }

 @Test
 public void testAttributesNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("KEY1", "value1");
 attributes.normalize();
 Assert.assertEquals("value1", attributes.get("key1"));
 }
}