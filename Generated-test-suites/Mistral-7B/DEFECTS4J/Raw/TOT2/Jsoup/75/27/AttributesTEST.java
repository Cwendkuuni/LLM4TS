import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test for basic functionality of get method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.get("key");
 String expected = "value";
 assertEquals(expected, result);
 }

 // Test for basic functionality of getIgnoreCase method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 String result = attributes.getIgnoreCase("key");
 String expected = "value";
 assertEquals(expected, result);
 }

 // Test for basic functionality of put method
 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String result = attributes.get("key");
 String expected = "value2";
 assertEquals(expected, result);
 }

 // Test for basic functionality of putIgnoreCase method
 @Test
 public void testPutIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value1");
 attributes.putIgnoreCase("key", "value2");
 String result = attributes.get("key");
 String expected = "value2";
 assertEquals(expected, result);
 }

 // Test for basic functionality of put method with boolean value
 @Test
 public void testPutBooleanMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", false);
 Assert.assertEquals(false, attributes.hasKey("key"));
 attributes.put("key", true);
 Assert.assertEquals(true, attributes.hasKey("key"));
 }

 // Test for basic functionality of put method with Attribute object
 @Test
 public void testPutAttributeMethod() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value");
 attributes.put(attribute);
 String result = attributes.get("key");
 String expected = "value";
 assertEquals(expected, result);
 }

 // Test for basic functionality of remove method
 @Test
 public void testRemoveMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 Assert.assertEquals(false, attributes.hasKey("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 // Test for basic functionality of hasKey method
 @Test
 public void testHasKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Assert.assertEquals(true, attributes.hasKey("key1"));
 Assert.assertEquals(false, attributes.hasKey("key2"));
 }

 // Test for basic functionality of hasKeyIgnoreCase method
 @Test
 public void testHasKeyIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 Assert.assertEquals(true, attributes.hasKeyIgnoreCase("key1"));
 Assert.assertEquals(false, attributes.hasKeyIgnoreCase("Key2"));
 }

 // Test for basic functionality of size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int result = attributes.size();
 int expected = 2;
 assertEquals(expected, result);
 }

 // Test for basic functionality of addAll method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes result = new Attributes();
 result.addAll(attributes1);
 result.addAll(attributes2);
 assertTrue(result.hasKey("key1"));
 assertTrue(result.hasKey("key2"));
 }

 // Test for basic functionality of asList method
 @Test
 public void testAsListMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> result = attributes.asList();
 int size = result.size();
 for (int i = 0; i < size; i++) {
 String key = result.get(i).getKey();
 String value = result.get(i).getValue();
 if (i == 0) {
 assertEquals("key1", key);
 assertEquals("value1", value);
 } else if (i == 1) {
 assertEquals("key2", key);
 assertEquals("value2", value);
 }
 }
 }

 // Test for edge case: adding duplicate keys
 @Test(expected = RuntimeException.class)
 public void testAddDuplicateKeys() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.put("key", "value2");
 }

 // Test for edge case: removing non-existent key
 @Test
 public void testRemoveNonExistentKey() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(false, attributes.hasKey("nonExistentKey"));
 attributes.remove("nonExistentKey");
 Assert.assertEquals(false, attributes.hasKey("nonExistentKey"));
 }

 // Test for error scenario: passing null key or value to put method
 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKeyOrValue() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 attributes.put("key", null);
 }

 // Test for error scenario: passing null key or value to putIgnoreCase method
 @Test(expected = IllegalArgumentException.class)
 public void testPutIgnoreCaseNullKeyOrValue() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase(null, "value");
 attributes.putIgnoreCase("key", null);
 }

 // Test for error scenario: passing negative size to checkCapacity method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckCapacityNegativeSize() {
 Attributes attributes = new Attributes();
 attributes.checkCapacity(-1);
 }

 // Test for error scenario: passing null array to copyOf method
 @Test(expected = NullPointerException.class)
 public void testCopyOfNullArray() {
 Attributes attributes = new Attributes();
 String[] nullArray = null;
 attributes.copyOf(nullArray, 1);
 }
}