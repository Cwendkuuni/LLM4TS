import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

// Test constructor
@Test
public void testAttributesConstruction() {
Attributes attributes = new Attributes();
assertEquals(0, attributes.size());
}

// Test get method
@Test
public void testGetMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
assertEquals("value", attributes.get("key"));
assertEquals("", attributes.get("nonExistentKey"));
}

// Test getIgnoreCase method
@Test
public void testGetIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "value");
assertEquals("value", attributes.getIgnoreCase("key"));
assertEquals("value", attributes.getIgnoreCase("Key"));
assertEquals("", attributes.getIgnoreCase("nonExistentKey"));
}

// Test put method
@Test
public void testPutMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
assertEquals(2, attributes.size());
assertEquals("value1", attributes.get("key1"));
assertEquals("value2", attributes.get("key2"));
}

// Test putIgnoreCase method
@Test
public void testPutIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.putIgnoreCase("Key1", "value1");
attributes.putIgnoreCase("key2", "value2");
assertEquals(2, attributes.size());
assertEquals("value1", attributes.getIgnoreCase("Key1"));
assertEquals("value2", attributes.getIgnoreCase("key2"));
}

// Test put method with existing key
@Test
public void testPutWithExistingKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value1");
attributes.put("key", "value2");
assertEquals("value2", attributes.get("key"));
assertEquals(1, attributes.size());
}

// Test put method with null value
@Test(expected = NullPointerException.class)
public void testPutWithNullValue() {
Attributes attributes = new Attributes();
attributes.put("key", null);
}

// Test remove method
@Test
public void testRemoveMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
attributes.remove("key1");
assertEquals("value2", attributes.get("key2"));
assertEquals(1, attributes.size());
}

// Test hasKey method
@Test
public void testHasKeyMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
assertTrue(attributes.hasKey("key1"));
assertFalse(attributes.hasKey("nonExistentKey"));
}

// Test hasKeyIgnoreCase method
@Test
public void testHasKeyIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key1", "value1");
assertTrue(attributes.hasKeyIgnoreCase("key1"));
assertTrue(attributes.hasKeyIgnoreCase("Key1"));
assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
}

// Test size method
@Test
public void testSizeMethod() {
Attributes attributes = new Attributes();
assertEquals(0, attributes.size());
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
Attributes combined = new Attributes();
combined.addAll(attributes1);
combined.addAll(attributes2);
assertEquals(2, combined.size());
assertEquals("value1", combined.get("key1"));
assertEquals("value2", combined.get("key2"));
}

// Test iterator method
@Test
public void testIteratorMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
List<Map.Entry<String, String>> list = attributes.asList();
for (Map.Entry<String, String> entry : list) {
System.out.println(entry.getKey() + "=" + entry.getValue());
}
}

// Test dataset method
@Test
public void testDatasetMethod() {
Attributes attributes = new Attributes();
attributes.put("data-key1", "value1");
attributes.put("data-key2", "value2");
Map<String, String> dataset = attributes.dataset();
for (Map.Entry<String, String> entry : dataset.entrySet()) {
System.out.println(entry.getKey() + "=" + entry.getValue());
}
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
attributes3.put("key1", "value2");
assertTrue(attributes1.equals(attributes1));
assertTrue(attributes1.equals(attributes2));
assertFalse(attributes1.equals(attributes3));
}

// Test hashCode method
@Test
public void testHashCodeMethod() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
Attributes attributes3 = new Attributes();
attributes1.put("key1", "value1");
attributes2.put("key1", "value1");
attributes3.put("key1", "value2");
assertEquals(attributes1.hashCode(), attributes1.hashCode());
assertEquals(attributes1.hashCode(), attributes2.hashCode());
assertNotEquals(attributes1.hashCode(), attributes3.hashCode());
}

// Test clone method
@Test
public void testCloneMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
Attributes cloned = attributes.clone();
assertEquals(attributes, cloned);
}

// Test normalize method
@Test
public void testNormalizeMethod() {
Attributes attributes = new Attributes();
attributes.put("Key1", "value1");
attributes.put("KEY2", "value2");
attributes.normalize();
assertEquals("key1", attributes.get("Key1"));
assertEquals("key2", attributes.get("KEY2"));
}

// Test add method with existing key
@Test
public void testAddWithExistingKey() {
Attributes attributes = new Attributes();
attributes.put("key", "value1");
attributes.put("key", "value2");
assertEquals("value2", attributes.get("key"));
}

// Test add method with null value
@Test
public void testAddWithNullValue() {
Attributes attributes = new Attributes();
attributes.put("key", null);
assertEquals(0, attributes.size());
}

// Test remove method with non-existent key
@Test
public void testRemoveWithNonExistentKey() {
Attributes attributes = new Attributes();
attributes.remove("nonExistentKey");
assertEquals(0, attributes.size());
}

// Test hasKey method with non-existent key
@Test
public void testHasKeyWithNonExistentKey() {
Attributes attributes = new Attributes();
assertFalse(attributes.hasKey("nonExistentKey"));
}

// Test get method with non-existent key
@Test
public void testGetWithNonExistentKey() {
Attributes attributes = new Attributes();
assertEquals("", attributes.get("nonExistentKey"));
}

// Test put method with null key
@Test(expected = NullPointerException.class)
public void testPutWithNullKey() {
Attributes attributes = new Attributes();
attributes.put(null, "value");
}
}