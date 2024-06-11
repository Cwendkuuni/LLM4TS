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
String actual = attributes.get("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for getIgnoreCase method
@Test
public void testGetIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "Value");
String actual = attributes.getIgnoreCase("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for put method
@Test
public void testPutMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String actual = attributes.get("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for putIgnoreCase method
@Test
public void testPutIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.putIgnoreCase("Key", "value");
String actual = attributes.get("key");
String expected = "value";
assertEquals(expected, actual);
}

// Test case for put method with boolean value
@Test
public void testPutBooleanMethod() {
Attributes attributes = new Attributes();
attributes.put("key", true);
assertTrue(attributes.hasKey("key"));
assertFalse(attributes.get("key").equals("true"));
}

// Test case for put method with null value
@Test
public void testPutNullMethod() {
Attributes attributes = new Attributes();
attributes.put("key", null);
assertTrue(attributes.hasKey("key"));
assertNull(attributes.get("key"));
}

// Test case for remove method
@Test
public void testRemoveMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
attributes.remove("key");
assertFalse(attributes.hasKey("key"));
}

// Test case for removeIgnoreCase method
@Test
public void testRemoveIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "value");
attributes.removeIgnoreCase("key");
assertFalse(attributes.hasKey("key"));
}

// Test case for hasKey method
@Test
public void testHasKeyMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
assertTrue(attributes.hasKey("key"));
}

// Test case for hasKeyIgnoreCase method
@Test
public void testHasKeyIgnoreCaseMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "value");
assertTrue(attributes.hasKeyIgnoreCase("key"));
}

// Test case for size method
@Test
public void testSizeMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
assertEquals(2, attributes.size());
}

// Test case for addAll method
@Test
public void testAddAllMethod() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
Attributes attributes2 = new Attributes();
attributes2.put("key2", "value2");
Attributes result = new Attributes();
result.addAll(attributes1);
result.addAll(attributes2);
assertTrue(result.hasKey("key1"));
assertTrue(result.hasKey("key2"));
}

// Test case for iterator method
@Test
public void testIteratorMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");
List<Attribute> expectedList = Arrays.asList(
new Attribute("key1", "value1"),
new Attribute("key2", "value2")
);
assertEquals(expectedList, attributes.asList());
}

// Test case for dataset method
@Test
public void testDatasetMethod() {
Attributes attributes = new Attributes();
attributes.put("data-key1", "data-value1");
attributes.put("data-key2", "data-value2");
Map<String, String> expectedMap = new HashMap<String, String>() {{
put("data-key1", "data-value1");
put("data-key2", "data-value2");
}};
assertEquals(expectedMap, attributes.dataset());
}

// Test case for html method
@Test
public void testHtmlMethod() {
Attributes attributes = new Attributes();
attributes.put("key", "value");
String expectedHtml = " key=\"value\"";
assertEquals(expectedHtml, attributes.html());
}

// Test case for equals method
@Test
public void testEqualsMethod() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
attributes1.put("key1", "value1");
attributes2.put("key1", "value1");
assertTrue(attributes1.equals(attributes2));
}

// Test case for hashCode method
@Test
public void testHashCodeMethod() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
attributes1.put("key1", "value1");
attributes2.put("key1", "value1");
assertEquals(attributes1.hashCode(), attributes2.hashCode());
}

// Test case for clone method
@Test
public void testCloneMethod() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
Attributes clonedAttributes = attributes.clone();
assertTrue(attributes.equals(clonedAttributes));
}

// Test case for normalize method
@Test
public void testNormalizeMethod() {
Attributes attributes = new Attributes();
attributes.put("Key", "Value");
attributes.normalize();
assertTrue(attributes.hasKey("key"));
}
}