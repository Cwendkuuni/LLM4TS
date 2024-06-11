import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test constructor
 @Test
 public void testAttributesConstructor() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 // Test get method
 @Test
 public void testGetAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 assertEquals(EmptyString, attributes.get("emptyKey"));
 }

 // Test getIgnoreCase method
 @Test
 public void testGetIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 assertEquals("value", attributes.getIgnoreCase("key"));
 assertEquals("value", attributes.getIgnoreCase("Key"));
 assertEquals(EmptyString, attributes.getIgnoreCase("EmptyKey"));
 }

 // Test put method
 @Test
 public void testPutAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 // Test putIgnoreCase method
 @Test
 public void testPutIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("Key2", "value2");
 attributes.putIgnoreCase("key1", "newValue1");
 assertEquals(3, attributes.size());
 assertEquals("value1", attributes.get("Key1"));
 assertEquals("newValue1", attributes.get("key1"));
 }

 // Test put method with boolean value
 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertEquals(EmptyString, attributes.get("key"));
 }

 // Test put method with existing key
 @Test(expected = IllegalStateException.class)
 public void testPutExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 }

 // Test remove method
 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 // Test removeIgnoreCase method
 @Test
 public void testRemoveIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("Key2", "value2");
 attributes.removeIgnoreCase("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("Key1"));
 assertEquals("value2", attributes.get("Key2"));
 }

 // Test hasKey method
 @Test
 public void testHasKeyAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKey("key2"));
 assertFalse(attributes.hasKey("nonExistentKey"));
 }

 // Test hasKeyIgnoreCase method
 @Test
 public void testHasKeyIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "value1");
 attributes.put("Key2", "value2");
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key2"));
 assertTrue(attributes.hasKeyIgnoreCase("Key2"));
 assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
 }

 // Test size method
 @Test
 public void testSizeAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test addAll method
 @Test
 public void testAddAllAttribute() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();attributes3.addAll(attributes1);attributes3.addAll(attributes2);assertEquals(2, attributes3.size());assertEquals("value1", attributes3.get("key1"));assertEquals("value2", attributes3.get("key2"));}

 // Test iterator method
 @Test
 public void testIteratorAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Map.Entry<String, String>> entries = attributes.asList();
 Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
 for (Map.Entry<String, String> entry : entries) {
 Map.Entry<String, String> entryIterator = iterator.next();
 assertEquals(entry.getKey(), entryIterator.getKey());
 assertEquals(entry.getValue(), entryIterator.getValue());
 }
 }

 // Test dataset method
 @Test
 public void testDatasetAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("value1", dataset.get("data-key1"));
 assertEquals("value2", dataset.get("data-key2"));
 }

 // Test html method
 @Test
 public void testHtmlAttribute() throws IOException {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 StringBuilder sb = StringUtil.borrowBuilder();
 attributes.html(sb, null);
 assertEquals("<key1>value1</key1>", sb.toString());
 }

 // Test equals method
 @Test
 public void testEqualsAttribute() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key1", "value1");
 Attributes attributes3 = new Attributes();
 attributes3.put("key2", "value2");
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 }

 // Test clone method
 @Test
 public void testCloneAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.size(), clonedAttributes.size());
 assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
 }

 // Test normalize method
 @Test
 public void testNormalizeAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("KEY1", "value1");
 attributes.normalize();
 assertEquals(1, attributes.size());
 assertEquals("key1", attributes.get("key1"));
 }
}