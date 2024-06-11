import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 // Test constructor
 @Test
 public void testAttributesConstructor() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 // Test get method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 // Test getIgnoreCase method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.getIgnoreCase("key"));
 assertEquals("value", attributes.getIgnoreCase("Key"));
 }

 // Test put method
 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test put method with duplicate key
 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodDuplicateKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 }

 // Test putIgnoreCase method
 @Test
 public void testPutIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key1", "Value1");
 attributes.putIgnoreCase("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test put method with duplicate case-insensitive key
 @Test(expected = IllegalArgumentException.class)
 public void testPutIgnoreCaseMethodDuplicateKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key1", "Value1");
 attributes.putIgnoreCase("Key1", "Value2");
 }

 // Test put method with boolean value
 @Test
 public void testPutMethodBooleanValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", false);
 assertFalse(attributes.get("key").equals(""));
 }

 // Test remove method with existing key
 @Test
 public void testRemoveMethodExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 }

 // Test remove method with non-existing key
 @Test
 public void testRemoveMethodNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.remove("key");
 assertEquals(0, attributes.size());
 assertFalse(attributes.hasKey("key"));
 }

 // Test removeIgnoreCase method with existing key
 @Test
 public void testRemoveIgnoreCaseMethodExistingKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key1", "Value1");
 attributes.putIgnoreCase("Key2", "Value2");
 attributes.removeIgnoreCase("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("Key1"));
 }

 // Test removeIgnoreCase method with non-existing key
 @Test
 public void testRemoveIgnoreCaseMethodNonExistingKey() {
 Attributes attributes = new Attributes();
 attributes.removeIgnoreCase("Key");
 assertEquals(0, attributes.size());
 assertFalse(attributes.hasKey("Key"));
 }

 // Test hasKey method with existing key
 @Test
 public void testHasKeyMethodExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 // Test hasKey method with non-existing key
 @Test
 public void testHasKeyMethodNonExistingKey() {
 Attributes attributes = new Attributes();assertFalse(attributes.hasKey("key"));
 }

 // Test hasKeyIgnoreCase method with existing key
 @Test
 public void testHasKeyIgnoreCaseMethodExistingKey() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 // Test hasKeyIgnoreCase method with non-existing key
 @Test
 public void testHasKeyIgnoreCaseMethodNonExistingKey() {
 Attributes attributes = new Attributes();
 assertFalse(attributes.hasKeyIgnoreCase("Key"));
 }

 // Test size method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 // Test addAll method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 assertEquals(2, attributes1.size());
 assertEquals(2, attributes2.size());
 assertEquals(4, attributes3.size());
 }

 // Test iterator method
 @Test
 public void testIteratorMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> attributesList = attributes.asList();
 Iterator<Attribute> iterator = attributes.iterator();
 int i = 0;
 while (iterator.hasNext()) {
 Attribute attribute = iterator.next();
 assertEquals(attributesList.get(i).getKey(), attribute.getKey());
 assertEquals(attributesList.get(i).getValue(), attribute.getValue());
 i++;
 }
 }

 // Test dataset method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "data-value1");
 attributes.put("data-key2", "data-value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 }

 // Test html method
 @Test
 public void testHtmlMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("<key1>value1</key1><key2>value2</key2>", attributes.html());
 }

 // Test equals method
 @Test
 public void testEqualsMethod() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key1", "value1");
 attributes3.put("key2", "value2");
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 }

 // Test clone method
 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.size(), clonedAttributes.size());
 }

 // Test normalize method
 @Test
 public void testNormalizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("KEY", "VALUE");
 attributes.normalize();
 assertTrue(attributes.hasKey("key"));
 assertEquals("value", attributes.get("key"));
 }
}