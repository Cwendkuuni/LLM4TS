import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for get method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String expectedValue = "value";
 String actualValue = attributes.get("key");
 Equals(expectedValue, actualValue);
 }

 // Test case for getIgnoreCase method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String expectedValue = "Value";
 String actualValue = attributes.getIgnoreCase("key");
 Equals(expectedValue, actualValue);
 }

 // Test case for put method with existing key
 @Test
 public void testPutMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "newValue");
 String expectedNewValue = "newValue";
 String actualNewValue = attributes.get("key1");
 Equals(expectedNewValue, actualNewValue);
 }

 // Test case for put method with new key
 @Test
 public void testPutMethodWithNewKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String expectedValue = "value2";
 attributes.put("key2", expectedValue);
 String actualValue = attributes.get("key2");
 Equals(expectedValue, actualValue);
 }

 // Test case for put method with boolean value
 @Test
 public void testPutMethodWithBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertTrue(attributes.get("key").equals(""));
 }

 // Test case for put method with Attribute object
 @Test
 public void testPutMethodWithAttributeObject() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value");
 attributes.put(attribute);
 String expectedValue = "value";
 String actualValue = attributes.get("key");
 Equals(expectedValue, actualValue);
 }

 // Test case for remove method with existing key
 @Test
 public void testRemoveMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 }

 // Test case for remove method with non-existing key
 @Test
 public void testRemoveMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key1"));
 attributes.remove("key1");
 }

 // Test case for hasKey method with existing key
 @Test
 public void testHasKeyMethodWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 }

 // Test case for hasKey method with non-existing key
 @Test
 public void testHasKeyMethodWithNonExistingKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key1"));
 }

 // Test case for size method with empty Attributes object
 @Test
 public void testSizeMethodWithEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 int expectedSize = 0;
 int actualSize = attributes.size();
 Equals(expectedSize, actualSize);
 }

 // Test case for size method with non-empty Attributes object
 @Test
 public void testSizeMethodWithNonEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 int expectedSize = 1;
 int actualSize = attributes.size();
 Equals(expectedSize, actualSize);
 }

 // Test case for addAll method with empty Attributes object as target
 @Test
 public void testAddAllMethodWithEmptyAttributesObject() {
 Attributes target = new Attributes();
 Attributes source = new Attributes();
 source.put("key1", "value1");
 target.addAll(source);
 assertTrue(target.hasKey("key1"));
 }

 // Test case for addAll method with non-empty Attributes object as target
 @Test
 public void testAddAllMethodWithNonEmptyAttributesObject() {
 Attributes target = new Attributes();
 Attributes source = new Attributes();
 source.put("key1", "value1");
 target.put("key2", "value2");
 source.put("key1", "newValue");
 target.addAll(source);
 assertTrue(target.hasKey("key1"));
 Equals("newValue", target.get("key1"));
 assertTrue(target.hasKey("key2"));
 Equals("value2", target.get("key2"));
 }

 // Test case for iterator method with empty Attributes object
 @Test
 public void testIteratorMethodWithEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 List<Attribute> expectedList = Collections.emptyList();
 List<Attribute> actualList = attributes.asList();
 Equals(expectedList, actualList);
 }

 // Test case for iterator method with non-empty Attributes object
 @Test
 public void testIteratorMethodWithNonEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> expectedList = Arrays.asList(
 new Attribute("key1", "value1"),
 new Attribute("key2", "value2")
 );
 List<Attribute> actualList = attributes.asList();
 Equals(expectedList, actualList);
 }

 // Test case for dataset method with empty Attributes object
 @Test
 public void testDatasetMethodWithEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 Map<String, String> expectedMap = Collections.emptyMap();
 Map<String, String> actualMap = attributes.dataset();
 Equals(expectedMap, actualMap);
 }

 // Test case for dataset method with non-empty Attributes object
 @Test
 public void testDatasetMethodWithNonEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("data-key1", "value1");
 expectedMap.put("data-key2", "value2");
 Map<String, String> actualMap = attributes.dataset();
 Equals(expectedMap, actualMap);
 }

 // Test case for html method with empty Attributes object
 @Test
 public void testHtmlMethodWithEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 String expectedHtml = "";
 String actualHtml = attributes.html();
 Equals(expectedHtml, actualHtml);
 }

 // Test case for html method with non-empty Attributes object
 @Test
 public void testHtmlMethodWithNonEmptyAttributesObject() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String expectedHtml = " key1=\"value1\"";
 String actualHtml = attributes.html();
 Equals(expectedHtml, actualHtml);
 }
}