import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

 // Test case for get method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.get("key"));
 }

 // Test case for getIgnoreCase method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 }

 // Test case for put method
 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 Assert.assertEquals("value2", attributes.get("key"));
 }

 // Test case for putIgnoreCase method
 @Test
 public void testPutIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value1");
 attributes.put("key", "value2"); // this should be case insensitive
 Assert.assertEquals("value2", attributes.get("Key"));
 }

 // Test case for put method with boolean value
 @Test
 public void testPutBooleanMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 Assert.assertFalse(attributes.hasKey("key"));
 }

 // Test case for addAll method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes expectedAttributes = new Attributes();
 expectedAttributes.put("key1", "value1");
 expectedAttributes.put("key2", "value2");

 Attributes attributes = new Attributes();
 attributes.addAll(attributes1);
 attributes.addAll(attributes2);

 Assert.assertEquals(expectedAttributes.size(), attributes.size());
 for (Map.Entry<String, String> entry : expectedAttributes.entrySet()) {
 String key = entry.getKey();
 String value = entry.getValue();
 Assert.assertEquals(value, attributes.get(key));
 }
 }

 // Test case for remove method
 @Test
 public void testRemoveMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 Assert.assertFalse(attributes.hasKey("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 // Test case for removeIgnoreCase method
 @Test
 public void testRemoveIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("key2", "value2");
 attributes.removeIgnoreCase("Key1");
 Assert.assertFalse(attributes.hasKey("Key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 // Test case for hasKey method
 @Test
 public void testHasKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertTrue(attributes.hasKey("key"));
 }

 // Test case for hasKeyIgnoreCase method
 @Test
 public void testHasKeyIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test case for hasKey method with empty attribute
 @Test
 public void testHasKeyEmptyAttribute() {
 Attributes attributes = new Attributes();
 Assert.assertFalse(attributes.hasKey(""));
 }

 // Test case for hasKey method with null attribute
 @Test(expected = NullPointerException.class)
 public void testHasKeyNullAttribute() {
 Attributes attributes = new Attributes();
 attributes.hasKey(null);
 }

 // Test case for size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals(2, attributes.size());
 }

 // Test case for dataset method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "data-value1");
 attributes.put("data-key2", "data-value2");
 Map<String, String> dataset = attributes.dataset();
 Assert.assertEquals(2, dataset.size());
 Assert.assertEquals("data-value1", dataset.get("data-key1"));
 Assert.assertEquals("data-value2", dataset.get("data-key2"));
 }

 // Test case for html method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append("key=\"value\"");
 StringBuilder actualHtml = new StringBuilder();
 try {
 attributes.html(actualHtml, null);
 } catch (IOException e) {
 e.printStackTrace();
 }
 Assert.assertEquals(expectedHtml.toString(), actualHtml.toString());
 }

 // Test case for equals method
 @Test
 public void testEqualsMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes3.put("key2", "value2");

 Assert.assertTrue(attributes1.equals(attributes1));
 Assert.assertTrue(attributes1.equals(attributes2));
 Assert.assertFalse(attributes1.equals(attributes3));
 }

 // Test case for hashCode method
 @Test
 public void testHashCodeMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");

 Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 // Test case for clone method
 @Test
 public void testCloneMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Attributes clonedAttributes = attributes.clone();
 Assert.assertEquals(attributes.size(), clonedAttributes.size());
 for (Map.Entry<String, String> entry : attributes.entrySet()) {
 String key = entry.getKey();
 String value = entry.getValue();
 Assert.assertEquals(value, clonedAttributes.get(key));
 }
 }

 // Test case for normalize method
 @Test
 public void testNormalizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "value");
 attributes.normalize();
 Assert.assertEquals("value", attributes.get("key"));
 }
}