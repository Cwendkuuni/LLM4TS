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
 public void testBasicUsage() {
 attributes.put("class", "test");
 attributes.put("id", "testId");
 assertEquals("test testId", attributes.html());
 }

 @Test
 public void testPutIgnoreCase() {
 attributes.putIgnoreCase("class", "Test");
 assertEquals("test Test", attributes.html());
 }

 @Test
 public void testGet() {
 attributes.put("class", "test");
 attributes.put("id", "testId");
 assertEquals("test", attributes.get("class"));
 assertEquals("testId", attributes.get("id"));
 }

 @Test
 public void testGetIgnoreCase() {
 attributes.put("Class", "Test");
 attributes.put("id", "testId");
 assertEquals("Test", attributes.getIgnoreCase("Class"));
 }

 @Test
 public void testPutBooleanAttribute() {
 attributes.put("checked", true);
 assertTrue(attributes.hasKey("checked"));
 assertEquals("checked", attributes.get("checked"));
 }

 @Test
 public void testPutBooleanAttributeAndRemove() {
 attributes.put("checked", true);
 attributes.put("checked", false);
 assertFalse(attributes.hasKey("checked"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 attributes.put(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 attributes.put("key", null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveInvalidIndex() {
 attributes.remove(Integer.MAX_VALUE);
 }

 @Test
 public void testSize() {
 assertEquals(0, attributes.size());
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testHasKey() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKey("key2"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 attributes.put("Key1", "value1");
 attributes.put("key2", "value2");
 assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 }

 @Test
 public void testAddAll() {
 Attributes otherAttributes = new Attributes();
 otherAttributes.put("otherKey1", "otherValue1");
 otherAttributes.put("otherKey2", "otherValue2");
 attributes.addAll(otherAttributes);
 assertEquals("test testId otherKey1 otherValue1 otherKey2 otherValue2", attributes.html());
 }

 @Test
 public void testPutWithExistingKey() {
 attributes.put("class", "test");
 attributes.put("class", "newTest");
 assertEquals("newTest", attributes.get("class"));
 }

 @Test
 public void testPutWithExistingValue() {
 attributes.put("class", "test");
 attributes.put("class", "testNewValue");
 assertEquals("testNewValue", attributes.get("class"));
 }

 @Test
 public void testEmptyStringAsValue() {
 attributes.put("class", "");
 assertEquals("class", attributes.get("class"));
 }

 @Test
 public void testEmptyArrayAsValues() {
 attributes.put("classes", new String[0]);
 assertEquals(0, attributes.size());
 }

}