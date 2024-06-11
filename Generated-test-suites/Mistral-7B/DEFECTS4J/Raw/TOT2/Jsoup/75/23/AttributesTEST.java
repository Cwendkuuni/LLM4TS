import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.EmptyString;

public class AttributesTest {

 // Basic functionality test cases for each method

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.get("key");
 assertEquals("value", result);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String result = attributes.getIgnoreCase("key");
 assertEquals("Value", result);
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String newValue = "newValue";
 attributes.put("key", newValue);
 String result = attributes.get("key");
 assertEquals("newValue", result);
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 attributes.put("key", "newValue"); // case insensitive
 String result = attributes.get("Key");
 assertEquals("newValue", result);
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 boolean result = attributes.getBoolean("key");
 assertTrue(result);
 }

 @Test
 public void testPutBooleanAndRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 attributes.put("key", false);
 boolean result = attributes.getBoolean("key");
 assertFalse(result);
 }

 // Edge cases and exception handling test cases

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveOutOfBounds() {
 Attributes attributes = new Attributes();
 attributes.remove(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidKey() {
 Attributes attributes = new Attributes();
 attributes.put("   \t key  \t ", EmptyString);
 String result = attributes.get(" key ");
 assertEquals(EmptyString, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentKey() {
 Attributes attributes = new Attributes();
 attributes.remove("nonExistentKey");
 }

 @Test
 public void testRemoveByIndex() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key3", "value3");
 attributes.remove(1);
 String result1 = attributes.get("key1");
 String result2 = attributes.get("key2");
 String result3 = attributes.get("key3");
 assertEquals("value1", result1);
 assertEquals("value2", result2);
 assertEquals("value3", result3);
 }
}