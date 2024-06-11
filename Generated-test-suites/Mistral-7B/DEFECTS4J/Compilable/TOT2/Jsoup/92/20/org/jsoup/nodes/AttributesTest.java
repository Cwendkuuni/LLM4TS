package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

 // Test case for get() method with valid key
 @Test
 public void testGetValidKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for get() method with invalid key
 @Test
 public void testGetInvalidKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("invalidKey");
 String expected = "";
 Assert.assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase() method with valid key
 @Test
 public void testGetIgnoreCaseValidKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase() method with invalid key
 @Test
 public void testGetIgnoreCaseInvalidKey() {
 Attributes attributes = new Attributes();
 String actual = attributes.getIgnoreCase("InvalidKey");
 String expected = "";
 Assert.assertEquals(expected, actual);
 }

 // Test case for put() method with valid key and value
 @Test
 public void testPutValidKeyAndValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for put() method with duplicate key
 @Test(expected = IllegalArgumentException.class)
 public void testPutDuplicateKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 }

 // Test case for put() method with null value
 @Test
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 String actual = attributes.get("key");
 String expected = "";
 Assert.assertEquals(expected, actual);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 boolean actual = attributes.hasKey("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for remove() method with valid key
 @Test
 public void testRemoveValidKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 boolean actual = attributes.hasKey("key");
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 // Test case for remove() method with invalid key
 @Test
 public void testRemoveInvalidKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("invalidKey");
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 attributes.remove("invalidKey");
 boolean actual2 = attributes.hasKey("invalidKey");
 boolean expected2 = false;
 Assert.assertEquals(expected2, actual2);
 }

 // Test case for hasKey() method with valid key
 @Test
 public void testHasKeyValidKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actual = attributes.hasKey("key");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 // Test case for hasKey() method with invalid key
 @Test
 public void testHasKeyInvalidKey() {
 Attributes attributes = new Attributes();
 boolean actual = attributes.hasKey("invalidKey");
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 // Test case for size() method with empty Attributes
 @Test
 public void testSizeEmptyAttributes() {
 Attributes attributes = new Attributes();
 int actual = attributes.size();
 int expected = 0;
 Assert.assertEquals(expected, actual);
 }

 // Test case for size() method with non-empty Attributes
 @Test
 public void testSizeNonEmptyAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 int actual = attributes.size();
 int expected = 1;
 Assert.assertEquals(expected, actual);
 }

 // Test case for addAll() method with empty Attributes and null incoming Attributes
 @Test
 public void testAddAllEmptyAttributesAndNullIncomingAttributes() {
 Attributes attributes = new Attributes();
 Attributes incoming = null;
 attributes.addAll(incoming);
 int actual = attributes.size();
 int expected = 0;
 Assert.assertEquals(expected, actual);
 }

 // Test case for addAll() method with empty Attributes and non-empty incoming Attributes
 @Test
 public void testAddAllEmptyAttributesAndNonEmptyIncomingAttributes() {
 Attributes attributes = new Attributes();
 Attributes incoming = new Attributes();
 incoming.put("key", "value");
 attributes.addAll(incoming);
 int actual = attributes.size();
 int expected = 1;
 Assert.assertEquals(expected, actual);
 }

 // Test case for addAll() method with non-empty Attributes and null incoming Attributes
 @Test
 public void testAddAllNonEmptyAttributesAndNullIncomingAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes incoming = null;
 attributes.addAll(incoming);
 int actual = attributes.size();
 int expected = 1;
 Assert.assertEquals(expected, actual);
 }

 // Test case for addAll() method with non-empty Attributes and non-empty incoming Attributes
 @Test
 public void testAddAllNonEmptyAttributesAndNonEmptyIncomingAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes incoming = new Attributes();
 incoming.put("key2", "value2");
 attributes.addAll(incoming);
 int actual = attributes.size();
 int expected = 2;
 Assert.assertEquals(expected, actual);
 }
}