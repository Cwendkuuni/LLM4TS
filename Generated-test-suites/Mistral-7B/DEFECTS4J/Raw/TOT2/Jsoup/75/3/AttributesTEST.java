import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String result = attributes.get("key");
 assertEquals("value", result);
 }

 // Test case for get() method with case-insensitive key
 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String result = attributes.getIgnoreCase("key");
 assertEquals("Value", result);
 }

 // Test case for get() method with non-existent key
 @Test
 public void testGetNotFound() {
 Attributes attributes = new Attributes();
 String result = attributes.get("key");
 assertEquals("", result);
 }

 // Test case for put() method
 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 // Test case for put() method with existing key
 @Test
 public void testPutExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 // Test case for put() method with null value
 @Test
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 assertEquals("", attributes.get("key"));
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.get("key").isEmpty());
 }

 // Test case for put() method with case-insensitive key
 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 assertEquals("Value", attributes.get("key"));
 }

 // Test case for put() method with existing key and new value
 @Test
 public void testPutExistingKeyNewValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 // Test case for remove() method with existing key
 @Test
 public void testRemoveExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 // Test case for remove() method with non-existent key
 @Test
 public void testRemoveNotFoundKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 // Test case for hasKey() method with existing key
 @Test
 public void testHasKeyExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 // Test case for hasKey() method with non-existent key
 @Test
 public void testHasKeyNotFoundKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKey("key"));
 }

 // Test case for hasKey() method with case-insensitive key
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test case for size() method with empty Attributes
 @Test
 public void testSizeEmpty() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 // Test case for size() method with non-empty Attributes
 @Test
 public void testSizeNonEmpty() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals(1, attributes.size());
 }

 // Test case for addAll() method with empty Attributes
 @Test
 public void testAddAllEmpty() {
 Attributes attributes = new Attributes();
 Attributes incoming = new Attributes();
 attributes.addAll(incoming);
 assertEquals(0, attributes.size());
 }

 // Test case for addAll() method with non-empty Attributes
 @Test
 public void testAddAllNonEmpty() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes incoming = new Attributes();
 incoming.put("key2", "value2");
 attributes.addAll(incoming);
 assertEquals(2, attributes.size());
 }

 // Test case for iterator() method
 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 Iterator<Attribute> iterator = list.iterator();
 assertTrue(iterator.hasNext());
 Attribute next = iterator.next();
 assertEquals("key1", next.getKey());
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals("key2", next.getKey());
 assertFalse(iterator.hasNext());
 }

 // Test case for dataset() method with empty Attributes
 @Test
 public void testDatasetEmpty() {
 Attributes attributes = new Attributes();
 Map<String, String> dataset = attributes.dataset();
 assertEquals(0, dataset.size());
 }

 // Test case for dataset() method with non-empty Attributes
 @Test
 public void testDatasetNonEmpty() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(1, dataset.size());
 assertEquals("key1", dataset.keySet().iterator().next());
 assertEquals("value1", dataset.get("key1"));
 }

 // Test case for html() method with empty Attributes
 @Test
 public void testHtmlEmpty() {
 Attributes attributes = new Attributes();
 assertEquals("", attributes.html());
 }

 // Test case for html() method with non-empty Attributes
 @Test
 public void testHtmlNonEmpty() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 try {
 attributes.html(new StringBuilder(), new Document.OutputSettings());
 } catch (IOException e) {
 throw new SerializationException(e);
 }
 assertEquals(" key=\"value\"", attributes.html());
 }

 // Test case for equals() method with identical Attributes
 @Test
 public void testEqualsIdentical() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key", "value");
 Attributes attributes2 = new Attributes();
 attributes2.put("key", "value");
 assertTrue(attributes1.equals(attributes2));
 }

 // Test case for equals() method with non-identical Attributes
 @Test
 public void testEqualsNonIdentical() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 assertFalse(attributes1.equals(attributes2));
 }

 // Test case for hashCode() method with identical Attributes
 @Test
 public void testHashCodeIdentical() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key", "value");
 Attributes attributes2 = new Attributes();
 attributes2.put("key", "value");
 int hashCode1 = attributes1.hashCode();
 int hashCode2 = attributes2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 // Test case for hashCode() method with non-identical Attributes
 @Test
 public void testHashCodeNonIdentical() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 int hashCode1 = attributes1.hashCode();
 int hashCode2 = attributes2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }
}