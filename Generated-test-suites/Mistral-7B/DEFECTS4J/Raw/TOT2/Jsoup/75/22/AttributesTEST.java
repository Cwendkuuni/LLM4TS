import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.AttributesTest;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String result = attributes.get("key1");
 Assert.assertEquals("value1", result);
 }

 // Test case for get() method with case-insensitive key
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String result = attributes.getIgnoreCase("Key1");
 Assert.assertEquals("value1", result);
 }

 // Test case for get() method with non-existent key
 @Test
 public void testGetNotFound() {
 Attributes attributes = new Attributes();
 String result = attributes.get("nonExistentKey");
 Assert.assertEquals(Attributes.EmptyString, result);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String result = attributes.get("key1");
 Assert.assertEquals("value1", result);
 }

 // Test case for put() method with existing key
 @Test(expected = IllegalArgumentException.class)
 public void testPutExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 }

 // Test case for putIgnoreCase() method
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 String result = attributes.getIgnoreCase("key1");
 Assert.assertEquals("value1", result);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key1", true);
 boolean result = attributes.hasKey("key1");
 Assert.assertTrue(result);
 }

 // Test case for put() method with null value
 @Test
 public void testPutNull() {
 Attributes attributes = new Attributes();
 attributes.put("key1", null);
 String result = attributes.get("key1");
 Assert.assertEquals(Attributes.EmptyString, result);
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 String result1 = attributes3.get("key1");
 String result2 = attributes3.get("key2");
 Assert.assertEquals("value1", result1);
 Assert.assertEquals("value2", result2);
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int result = attributes.size();
 Assert.assertEquals(2, result);
 }

 // Test case for hasKey() method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 boolean result = attributes.hasKey("key1");
 Assert.assertTrue(result);
 }

 // Test case for hasKey() method with non-existent key
 @Test
 public void testHasKeyNotFound() {
 Attributes attributes = new Attributes();
 boolean result = attributes.hasKey("nonExistentKey");
 Assert.assertFalse(result);
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 boolean result = attributes.hasKeyIgnoreCase("key1");
 Assert.assertTrue(result);
 }

 // Test case for remove() method with existing key
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");
 attributes.remove("key1");
 String result = attributes.get("key1");
 Assert.assertEquals(Attributes.EmptyString, result);
 }

 // Test case for remove() method with non-existent key
 @Test
 public void testRemoveNotFound() {
 Attributes attributes = new Attributes();
 String result = attributes.get("nonExistentKey");
 attributes.remove("nonExistentKey");
 String result2 = attributes.get("nonExistentKey");
 Assert.assertEquals(Attributes.EmptyString, result);
 Assert.assertEquals(Attributes.EmptyString, result2);
 }

 // Test case for removeIgnoreCase() method with existing key
 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.removeIgnoreCase("key1");
 String result = attributes.get("Key1");
 Assert.assertEquals(Attributes.EmptyString, result);
 }

 // Test case for removeIgnoreCase() method with non-existent key
 @Test
 public void testRemoveIgnoreCaseNotFound() {
 Attributes attributes = new Attributes();
 String result = attributes.get("nonExistentKey");
 attributes.removeIgnoreCase("nonExistentKey");
 String result2 = attributes.get("nonExistentKey");
 Assert.assertEquals(Attributes.EmptyString, result);
 Assert.assertEquals(Attributes.EmptyString, result2);
 }
}