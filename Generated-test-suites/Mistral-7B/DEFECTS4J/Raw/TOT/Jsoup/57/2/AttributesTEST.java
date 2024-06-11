package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

/**
 * Test for Attributes class.
 */
public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 /**
  * Test get method with valid key.
  */
 @Test
 public void testGet() {
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 /**
  * Test get method with invalid key.
  */
 @Test
 public void testGetEmpty() {
 assertEquals("", attributes.get("invalidKey"));
 }

 /**
  * Test getIgnoreCase method with valid key.
  */
 @Test
 public void testGetIgnoreCase() {
 attributes.put("Key", "value");
 assertEquals("value", attributes.getIgnoreCase("Key"));
 }

 /**
  * Test getIgnoreCase method with invalid key.
  */
 @Test
 public void testGetIgnoreCaseEmpty() {
 assertEquals("", attributes.getIgnoreCase("InvalidKey"));
 }

 /**
  * Test put method with valid key and value.
  */
 @Test
 public void testPut() {
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 /**
  * Test put method with valid boolean key and value.
  */
 @Test
 public void testPutBoolean() {
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.hasKey("invalidKey"));
 }

 /**
  * Test put method with duplicate key.
  */
 @Test(expected = UnsupportedOperationException.class)
 public void testPutDuplicateKey() {
 attributes.put("key", "value");
 attributes.put("key", "newValue");
 }

 /**
  * Test remove method with valid key.
  */
 @Test
 public void testRemove() {
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 /**
  * Test remove method with invalid key.
  */
 @Test
 public void testRemoveEmpty() {
 assertFalse(attributes.hasKey("invalidKey"));
 attributes.remove("invalidKey");
 }

 /**
  * Test hasKey method with valid key.
  */
 @Test
 public void testHasKey() {
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 /**
  * Test hasKey method with invalid key.
  */
 @Test
 public void testHasKeyEmpty() {
 assertFalse(attributes.hasKey("invalidKey"));
 }

 /**
  * Test hasKeyIgnoreCase method with valid key.
  */
 @Test
 public void testHasKeyIgnoreCase() {
 attributes.put("Key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("Key"));
 }

 /**
  * Test hasKeyIgnoreCase method with invalid key.
  */
 @Test
 public void testHasKeyIgnoreCaseEmpty() {
 assertFalse(attributes.hasKeyIgnoreCase("InvalidKey"));
 }

 /**
  * Test size method with empty Attributes.
  */
 @Test
 public void testSizeEmpty() {
 assertEquals(0, attributes.size());
 }

 /**
  * Test size method with non-empty Attributes.
  */
 @Test
 public void testSizeNonEmpty() {
 attributes.put("key", "value");
 assertEquals(1, attributes.size());
 }

 /**
  * Test addAll method with empty Attributes.
  */
 @Test
 public void testAddAllEmpty() {
 Attributes attributes2 = new Attributes();
 attributes.addAll(attributes2);
 assertEquals(0, attributes.size());
 }

 /**
  * Test addAll method with non-empty Attributes.
  */
 @Test
 public void testAddAllNonEmpty() {
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes.addAll(attributes2);
 assertEquals(2, attributes.size());
 }

 /**
  * Test iterator method.
  */
 @Test
 public void testIterator() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 Iterator<Attribute> it = list.iterator();
 assertEquals("value1", it.next().getValue());
 assertEquals("value2", it.next().getValue());
 }

 /**
  * Test dataset method.
  */
 @Test
 public void testDataset() {
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals("value1", dataset.get("key1"));
 assertEquals("value2", dataset.get("key2"));
 }

 /**
  * Test html method.
  */
 @Test
 public void testHtml() {
 attributes.put("key", "value");
 assertEquals(" key=\"value\"", attributes.html());
 }

 /**
  * Test equals method.
  */
 @Test
 public void testEquals() {
 Attributes attributes2 = new Attributes();
 attributes2.put("key", "value");
 assertTrue(attributes.equals(attributes));
 assertTrue(attributes.equals(attributes2));
 Attributes attributes3 = new Attributes();
 assertFalse(attributes.equals(attributes3));
 }

 /**
  * Test clone method.
  */
 @Test
 public void testClone() {
 Attributes attributes2 = attributes.clone();
 assertTrue(attributes.equals(attributes2));
 }
}