package org.jsoup.nodes;

import org.junit.Assert; import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

 @Test
 public void testAttributesEmpty() {
 Attributes attributes = new Attributes();

 Assert.assertEquals(0, attributes.size());
 Assert.assertFalse(attributes.hasKey("key"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
 Assert.assertFalse(attributes.hasKey("KEY"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
 Assert.assertEquals(Attributes.Empty, attributes.get("key"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("key"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("KEY"));
 }

 @Test
 public void testAttributesBasic() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");

 Assert.assertEquals(1, attributes.size());
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 Assert.assertFalse(attributes.hasKey("KEY"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
 Assert.assertEquals("value", attributes.get("key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 Assert.assertEquals(Attributes.EmptyString, attributes.get("KEY"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("KEY"));
 }

 @Test
 public void testAttributesCaseInsensitive() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");

 Assert.assertEquals(1, attributes.size());
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
 Assert.assertEquals("value", attributes.get("key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("Key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("KEY"));
 }

 @Test
 public void testAttributesBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", "true");
 attributes.put("key", null);

 Assert.assertEquals(1, attributes.size());
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 Assert.assertFalse(attributes.hasKey("KEY"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
 Assert.assertEquals(Attributes.EmptyString, attributes.get("key"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("key"));
 Assert.assertEquals(Attributes.EmptyString, attributes.get("KEY"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("KEY"));
 Assert.assertFalse(attributes.hasKey("key2"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
 Assert.assertFalse(attributes.get("key2").equals("true"));
 }

 @Test
 public void testAttributesSame() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");

 Assert.assertEquals(attributes1, attributes2);
 }

 @Test
 public void testAttributesRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");

 Assert.assertEquals(0, attributes.size());
 Assert.assertFalse(attributes.hasKey("key"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
 Assert.assertFalse(attributes.hasKey("KEY"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY"));
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
 Assert.assertTrue(attributes3.hasKeyIgnoreCase("key1"));
 Assert.assertTrue(attributes3.hasKey("key2"));
 Assert.assertTrue(attributes3.hasKeyIgnoreCase("key2"));
 Assert.assertEquals("value1", attributes3.get("key1"));
 Assert.assertEquals("value1", attributes3.getIgnoreCase("key1"));
 Assert.assertEquals("value2", attributes3.get("key2"));
 Assert.assertEquals("value2", attributes3.getIgnoreCase("key2"));
 }

 @Test
 public void testAttributesClone() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 Attributes attributesClone = attributes.clone();

 Assert.assertEquals(attributes, attributesClone);
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals(1, attributesClone.size());
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value1", attributesClone.get("key1"));
 }

 @Test
 public void testAttributesToString() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 Assert.assertEquals("key1=\"value1\"", attributes.toString());
 }

 @Test
 public void testAttributesDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");

 Map<String, String> dataset = attributes.dataset();

 Assert.assertEquals(2, dataset.size());
 Assert.assertEquals("value1", dataset.get("data-key1"));
 Assert.assertEquals("value2", dataset.get("data-key2"));
 }
}