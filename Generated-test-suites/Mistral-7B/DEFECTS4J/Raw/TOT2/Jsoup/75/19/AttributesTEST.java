import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for get method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assertions.assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 Assertions.assertEquals(expected, actual);
 }

 // Test case for put method with existing key
 @Test
 public void testPutMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String actual = attributes.get("key");
 String expected = "value2";
 Assertions.assertEquals(expected, actual);
 }

 // Test case for put method with new key
 @Test
 public void testPutMethodWithNewKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 String actualKey1 = attributes.get("key1");
 String expectedKey1 = "value1";
 String actualKey2 = attributes.get("key2");
 String expectedKey2 = "value2";
 Assertions.assertEquals(expectedKey1, actualKey1);
 Assertions.assertEquals(expectedKey2, actualKey2);
 }

 // Test case for put method with boolean value
 @Test
 public void testPutMethodWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Assertions.assertTrue(attributes.hasKey("key"));
 Assertions.assertEquals(Attributes.EmptyString, attributes.get("key"));
 }

 // Test case for remove method with existing key
 @Test
 public void testRemoveMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 Assertions.assertFalse(attributes.hasKey("key"));
 }

 // Test case for remove method with non-existing key
 @Test
 public void testRemoveMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.remove("key");
 Assertions.assertFalse(attributes.hasKey("key"));
 }

 // Test case for hasKey method with existing key
 @Test
 public void testHasKeyMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assertions.assertTrue(attributes.hasKey("key"));
 }

 // Test case for hasKey method with non-existing key
 @Test
 public void testHasKeyMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 Assertions.assertFalse(attributes.hasKey("key"));
 }

 // Test case for size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actual = attributes.size();
 int expected = 2;
 Assertions.assertEquals(expected, actual);
 }

 // Test case for addAll method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 String actualKey1 = attributes1.get("key1");
 String expectedKey1 = "value1";
 String actualKey2 = attributes1.get("key2");
 String expectedKey2 = "value2";
 Assertions.assertEquals(expectedKey1, actualKey1);
 Assertions.assertEquals(expectedKey2, actualKey2);
 }

 // Test case for iterator method
 @Test
 public void testIteratorMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Iterator<Attribute> iterator = attributes.iterator();
 Attribute actual = iterator.next();
 String expectedKey = "key1";
 String expectedValue = "value1";
 Assertions.assertEquals(expectedKey, actual.getKey());
 Assertions.assertEquals(expectedValue, actual.getValue());
 }

 // Test case fordataset method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> actual = attributes.dataset();
 Map<String, String> expected = new HashMap<>();
 expected.put("key1", "value1");
 expected.put("key2", "value2");
 Assertions.assertEquals(expected, actual);
 }

 // Test case for html method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 StringBuilder actual = new StringBuilder();
 try {
 attributes.html(actual, null);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 String expected = " key1=\"value1\" key2=\"value2\" ";
 Assertions.assertEquals(expected, actual.toString());
 }

 // Test case fortoString method
 @Test
 public void testToStringMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 String actual = attributes.toString();
 String expected = "attributes[key1=value1, key2=value2]";
 Assertions.assertEquals(expected, actual);
 }

 // Test case for equals method
 @Test
 public void testEqualsMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 attributes3.put("key1", "value1");
 attributes3.put("key3", "value3");
 Assertions.assertEquals(attributes1, attributes1);
 Assertions.assertEquals(attributes1, attributes2);
 Assertions.assertNotEquals(attributes1, attributes3);
 }

 // Test case for hashCode method
 @Test
 public void testHashCodeMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes2.put("key2", "value2");
 attributes3.put("key1", "value1");
 attributes3.put("key3", "value3");
 int actual1 = attributes1.hashCode();
 int actual2 = attributes2.hashCode();
 int actual3 = attributes3.hashCode();
 Assertions.assertEquals(actual1, actual1);
 Assertions.assertEquals(actual1, actual2);
 Assertions.assertNotEquals(actual1, actual3);
 }

 // Test case for clone method
 @Test
 public void testCloneMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 String actualKey1 = clonedAttributes.get("key1");
 String expectedKey1 = "value1";
 Assertions.assertEquals(expectedKey1, actualKey1);
 }

 // Test case for normalize method
 @Test
 public void testNormalizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.normalize();
 String actual = attributes.get("key1");
 String expected = "value1";
 Assertions.assertEquals(expected, actual);
 }
}