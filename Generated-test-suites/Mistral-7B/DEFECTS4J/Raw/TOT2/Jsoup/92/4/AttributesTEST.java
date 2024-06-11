import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

 // Test case for get() method with valid key
 @Test
 public void testGetValidAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actual = attributes.get("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for get() method with invalid key
 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidAttribute() {
 Attributes attributes = new Attributes();
 String actual = attributes.get("invalidKey");
 }

 // Test case for getIgnoreCase() method with valid key
 @Test
 public void testGetIgnoreCaseValidAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String actual = attributes.getIgnoreCase("key");
 String expected = "value";
 Assert.assertEquals(expected, actual);
 }

 // Test case for getIgnoreCase() method with invalid key
 @Test(expected = IllegalArgumentException.class)
 public void testGetIgnoreCaseInvalidAttribute() {
 Attributes attributes = new Attributes();
 String actual = attributes.getIgnoreCase("InvalidKey");
 }

 // Test case for put() method with valid key and value
 @Test
 public void testPutValidAttribute() {
 Attributes attributes = new Attributes();
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 String actual = attributes.get(key);
 String expected = value;
 Assert.assertEquals(expected, actual);
 }

 // Test case for put() method with invalid key
 @Test(expected = IllegalArgumentException.class)
 public void testPutInvalidAttributeKey() {
 Attributes attributes = new Attributes();
 String invalidKey = null;
 String value = "value";
 attributes.put(invalidKey, value);
 }

 // Test case for put() method with valid key and null value
 @Test
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 String key = "key";
 attributes.put(key, null);
 String actual = attributes.get(key);
 Assert.assertEquals(null, actual);
 }

 // Test case for put() method with duplicate key
 @Test
 public void testPutDuplicateKey() {
 Attributes attributes = new Attributes();
 String key = "key";
 String value1 = "value1";
 String value2 = "value2";
 attributes.put(key, value1);
 attributes.put(key, value2);
 String actual = attributes.get(key);
 String expected = value2; // since the last put overwrites the previous one
 Assert.assertEquals(expected, actual);
 }

 // Test case for putIgnoreCase() method with valid key and value
 @Test
 public void testPutIgnoreCaseValidAttribute() {
 Attributes attributes = new Attributes();
 String key = "Key";
 String value = "Value";
 attributes.putIgnoreCase(key, value);
 String actual = attributes.get(key);
 String expected = value;
 Assert.assertEquals(expected, actual);
 }

 // Test case for putIgnoreCase() method with duplicate case insensitive key
 @Test
 public void testPutIgnoreCaseDuplicateKey() {
 Attributes attributes = new Attributes();
 String key = "KEY";
 String value1 = "value1";
 String value2 = "value2";
 attributes.putIgnoreCase(key, value1);
 attributes.putIgnoreCase(key, value2);
 String actual = attributes.get(key);
 String expected = value2; // since the last put overwrites the previous one
 Assert.assertEquals(expected, actual);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBooleanValue() {
 Attributes attributes = new Attributes();
 String key = "key";
 attributes.put(key, true);
 Assert.assertTrue(attributes.hasKey(key));
 Assert.assertFalse(attributes.get(key).equals(""));
 attributes.put(key, false);
 Assert.assertTrue(attributes.hasKey(key));
 Assert.assertNull(attributes.get(key));
 }

 // Test case for put() method with Attribute object
 @Test
 public void testPutAttributeObject() {
 Attributes attributes = new Attributes();
 String key = "key";
 String value = "value";
 Attribute attribute = new Attribute(key, value);
 attributes.put(attribute);
 String actual = attributes.get(key);
 String expected = value;
 Assert.assertEquals(expected, actual);
 }

 // Test case for remove() method with valid index
 @Test
 public void testRemoveValidIndex() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 int sizeBeforeRemoval = attributes.size();
 attributes.remove(0);
 int sizeAfterRemoval = attributes.size();
 Assert.assertEquals(sizeBeforeRemoval - 1, sizeAfterRemoval);
 Assert.assertFalse(attributes.hasKey(key1));
 }

 // Test case for remove() method with invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveInvalidIndex() {
 Attributes attributes = new Attributes();
 String key = "key";
 String value = "value";
 attributes.put(key, value);
 attributes.remove(100);
 }

 // Test case for remove() method with key
 @Test
 public void testRemoveKey() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 attributes.remove(key1);
 Assert.assertFalse(attributes.hasKey(key1));
 }

 // Test case for removeIgnoreCase() method with valid key
 @Test
 public void testRemoveIgnoreCaseValidKey() {
 Attributes attributes = new Attributes();
 String key1 = "Key1";
 String value1 = "value1";
 attributes.putIgnoreCase(key1, value1);
 attributes.removeIgnoreCase(key1);
 Assert.assertFalse(attributes.hasKeyIgnoreCase(key1));
 }

 // Test case for removeIgnoreCase() method with invalid key
 @Test
 public void testRemoveIgnoreCaseInvalidKey() {
 Attributes attributes = new Attributes();
 String key1 = "Key1";
 attributes.putIgnoreCase(key1, "value1");
 attributes.removeIgnoreCase("InvalidKey");
 Assert.assertTrue(attributes.hasKeyIgnoreCase(key1));
 }

 // Test case for hasKey() method with valid key
 @Test
 public void testHasKeyValidKey() {
 Attributes attributes = new Attributes();
 String key = "key";
 attributes.put(key, "value");
 Assert.assertTrue(attributes.hasKey(key));
 }

 // Test case for hasKey() method with invalid key
 @Test
 public void testHasKeyInvalidKey() {
 Attributes attributes = new Attributes();
 Assert.assertFalse(attributes.hasKey("invalidKey"));
 }

 // Test case for hasKeyIgnoreCase() method with valid key
 @Test
 public void testHasKeyIgnoreCaseValidKey() {
 Attributes attributes = new Attributes();
 String key = "Key";
 attributes.putIgnoreCase(key, "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase(key));
 }

 // Test case for hasKeyIgnoreCase() method with invalid key
 @Test
 public void testHasKeyIgnoreCaseInvalidKey() {
 Attributes attributes = new Attributes();
 Assert.assertFalse(attributes.hasKeyIgnoreCase("InvalidKey"));
 }

 // Test case for size() method
 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 int size = attributes.size();
 attributes.put("key2", "value2");
 int newSize = attributes.size();
 Assert.assertEquals(size + 1, newSize);
 }

 // Test case for addAll() method
 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 String key2 = "key2";
 String value2 = "value2";
 attributes1.put(key1, value1);
 attributes2.put(key2, value2);
 attributes1.addAll(attributes2);
 Assert.assertEquals(attributes1.size(), attributes2.size() + attributes1.size());
 for (Map.Entry<String, String> entry : attributes1.asList().entrySet()) {
 Assert.assertTrue(attributes2.containsKey(entry.getKey()));
 Assert.assertEquals(entry.getValue(), attributes2.get(entry.getKey()));
 }
 }

 // Test case for iterator() method
 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 String key2 = "key2";
 String value2 = "value2";
 attributes.put(key1, value1);
 attributes.put(key2, value2);
 for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
 attributes.remove(entry.getKey());
 }
 Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
 Assert.assertFalse(iterator.hasNext());
 }

 // Test case for asList() method
 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 List<Map.Entry<String, String>> list = attributes.asList();
 Assert.assertEquals(1, list.size());
 Map.Entry<String, String> entry = list.get(0);
 Assert.assertEquals(key1, entry.getKey());
 Assert.assertEquals(value1, entry.getValue());
 }

 // Test case for dataset() method
 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 String key1 = "data-key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 Map<String, String> dataset = attributes.dataset();
 Assert.assertEquals(1, dataset.size());
 Assert.assertEquals(value1, dataset.get(key1));
 }

 // Test case for html() method
 @Test
 public void testHtml() throws IOException {
 Attributes attributes = new Attributes();
 String key1 = "key1";
 String value1 = "value1";
 attributes.put(key1, value1);
 StringBuilder html = new StringBuilder();
 attributes.html(html, new Document.OutputSettings());
 String expected = key1 + "=\"value1\" ";
 String actual = html.toString();
 Assert.assertEquals(expected, actual);
 }
}