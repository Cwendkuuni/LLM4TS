import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String retrievedValue = attributes.get("key");
 assertEquals("value", retrievedValue);
 }

 // Test case for getIgnoreCase() method
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String retrievedValue = attributes.getIgnoreCase("key");
 assertEquals("Value", retrievedValue);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 String retrievedValue = attributes.get("key");
 assertEquals("value2", retrievedValue);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 attributes.remove("key");
 attributes.put("key", false);
 assertFalse(attributes.hasKey("key"));
 }

 // Test case for put() method with Attribute object
 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value");
 attributes.put(attribute);
 String retrievedValue = attributes.get("key");
 assertEquals("value", retrievedValue);
 }

 // Test case for remove() method
 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 // Test case for remove() method with ignoreCase option
 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 attributes.removeIgnoreCase("key");
 assertFalse(attributes.hasKey("Key"));
 }

 // Test case for hasKey() method
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 assertTrue(attributes1.hasKey("key2"));
 }

 // Test case for iterator() method
 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Iterator<Attribute> iterator = attributes.iterator();
 List<Attribute> expectedList = new ArrayList<>();
 expectedList.add(new Attribute("key1", "value1"));
 expectedList.add(new Attribute("key2", "value2"));
 List<Attribute> actualList = new ArrayList<>();
 while (iterator.hasNext()) {
 actualList.add(iterator.next());
 }
 assertEquals(expectedList, actualList);
 }

 // Test case for asList() method
 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> expectedList = new ArrayList<>();
 expectedList.add(new Attribute("key1", "value1"));
 expectedList.add(new Attribute("key2", "value2"));
 List<Attribute> actualList = attributes.asList();
 assertEquals(expectedList, actualList);
 }

 // Test case for dataset() method
 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "data-value");
 Map<String, String> dataset = attributes.dataset();
 assertEquals("data-value", dataset.get("data-key"));
 }

 // Test case for html() method
 @Test
 public void testHtml() throws IOException {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append("<div key=\"value\"></div>");
 String actualHtml = attributes.html();
 assertEquals(expectedHtml.toString(), actualHtml);
 }
}