import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 @Test
 public void testGetAndPut() {
 Attributes attributes = new Attributes();

 // Test getting and setting a non-existent attribute
 String key = "testKey";
 String value = "testValue";
 String retrievedValue = attributes.get(key);
 Assertions.assertEquals(Attributes.EmptyString, retrievedValue);
 attributes.put(key, value);
 String putValue = attributes.get(key);
 Assertions.assertEquals(value, putValue);

 // Test getting and setting an existing attribute
 key = "testKey";
 attributes.put(key, "initialValue");
 String existingValue = attributes.get(key);
 Assertions.assertEquals("initialValue", existingValue);
 attributes.put(key, "updatedValue");
 String updatedValue = attributes.get(key);
 Assertions.assertEquals("updatedValue", updatedValue);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();

 // Test getting an existing attribute with a case insensitive key
 String key = "TestKey";
 String value = "testValue";
 attributes.put(key, value);
 String retrievedValue = attributes.getIgnoreCase(key);
 Assertions.assertEquals(value, retrievedValue);
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();

 // Test setting an attribute with a case insensitive key
 String key = "TestKey";
 String value = "testValue";
 attributes.putIgnoreCase(key, value);
 String retrievedValue = attributes.getIgnoreCase(key);
 Assertions.assertEquals(value, retrievedValue);
 }

 @Test
 public void testPutWithBooleanValue() {
 Attributes attributes = new Attributes();

 // Test setting a boolean attribute
 String key = "testBool";
 attributes.put(key, "true");
 boolean boolValue = attributes.getBoolean(key);
 Assertions.assertTrue(boolValue);
 attributes.put(key, "false");
 boolValue = attributes.getBoolean(key);
 Assertions.assertFalse(boolValue);
 }

 @Test
 public void testPutWithExistingBooleanValue() {
 Attributes attributes = new Attributes();

 // Test setting an existing boolean attribute
 String key = "testBool";
 attributes.put(key, "true");
 boolean boolValue = attributes.getBoolean(key);
 Assertions.assertTrue(boolValue);
 attributes.put(key, "false");
 boolValue = attributes.getBoolean(key);
 Assertions.assertFalse(boolValue);
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();

 // Test removing an existing attribute
 String key = "testKey";
 attributes.put(key, "testValue");
 int initialSize = attributes.size();
 attributes.remove(key);
 int finalSize = attributes.size();
 Assertions.assertEquals(initialSize - 1, finalSize);
 String retrievedValue = attributes.get(key);
 Assertions.assertEquals(Attributes.EmptyString, retrievedValue);
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();

 // Test removing an attribute with a case insensitive key
 String key = "TestKey";
 attributes.put(key, "testValue");
 int initialSize = attributes.size();
 attributes.removeIgnoreCase(key);
 int finalSize = attributes.size();
 Assertions.assertEquals(initialSize - 1, finalSize);
 String retrievedValue = attributes.getIgnoreCase(key);
 Assertions.assertEquals(Attributes.EmptyString, retrievedValue);
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();

 // Test checking for an existing key
 String key = "testKey";
 attributes.put(key, "testValue");
 boolean hasKey = attributes.hasKey(key);
 Assertions.assertTrue(hasKey);

 // Test checking for a non-existent key
 String nonExistentKey = "nonExistentKey";
 boolean hasNonExistentKey = attributes.hasKey(nonExistentKey);
 Assertions.assertFalse(hasNonExistentKey);
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();

 // Test checking for an existing key with a case insensitive key
 String key = "TestKey";
 attributes.put(key, "testValue");
 boolean hasKey = attributes.hasKeyIgnoreCase(key);
 Assertions.assertTrue(hasKey);

 // Test checking for a non-existent key with a case insensitive key
 String nonExistentKey = "NonExistentKey";
 boolean hasNonExistentKey = attributes.hasKeyIgnoreCase(nonExistentKey);
 Assertions.assertFalse(hasNonExistentKey);
 }

 @Test
 public void testAddAll() {
 // Test adding attributes to an empty attributes object
 Attributes emptyAttributes = new Attributes();
 Attributes attributesToAdd = new Attributes();
 attributesToAdd.put("key1", "value1");
 attributesToAdd.put("key2", "value2");
 emptyAttributes.addAll(attributesToAdd);

 int initialEmptySize = emptyAttributes.size();
 int initialAddedSize = attributesToAdd.size();
 emptyAttributes.addAll(attributesToAdd);
 int finalEmptySize = emptyAttributes.size();
 int finalAddedSize = attributesToAdd.size();

 Assertions.assertEquals(initialEmptySize + initialAddedSize, finalEmptySize);
 String retrievedValue1 = emptyAttributes.get("key1");
 Assertions.assertEquals("value1", retrievedValue1);
 String retrievedValue2 = emptyAttributes.get("key2");
 Assertions.assertEquals("value2", retrievedValue2);

 // Test adding attributes to an existing attributes object
 Attributes existingAttributes = new Attributes();
 existingAttributes.put("existingKey", "existingValue");
 int initialExistingSize = existingAttributes.size();
 int initialAttributesToAddSize = attributesToAdd.size();
 existingAttributes.addAll(attributesToAdd);
 int finalExistingSize = existingAttributes.size();
 int finalAttributesToAddSize = attributesToAdd.size();

 Assertions.assertEquals(initialExistingSize + initialAttributesToAddSize, finalExistingSize);
 String retrievedValue1FromExisting = existingAttributes.get("key1");
 Assertions.assertEquals("value1", retrievedValue1FromExisting);
 String retrievedValue2FromExisting = existingAttributes.get("key2");
 Assertions.assertEquals("value2", retrievedValue2FromExisting);
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();

 // Test getting the size of an empty attributes object
 int initialSize = attributes.size();
 Assertions.assertEquals(0, initialSize);

 // Test getting the size of an attributes object with attributes
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int finalSize = attributes.size();
 Assertions.assertEquals(2, finalSize);
 }
}