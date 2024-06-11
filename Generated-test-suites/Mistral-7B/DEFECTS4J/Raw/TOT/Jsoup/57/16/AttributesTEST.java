import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

/**
 * Test class for Attributes class.
 */
public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 /**
 * Test get method with existing attribute.
 */
 @Test
 public void testGet() {
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 /**
 * Test get method with non-existing attribute.
 */
 @Test
 public void testGetNonExisting() {
 assertEquals("", attributes.get("nonExistingKey"));
 }

 /**
 * Test getIgnoreCase method with existing attribute.
 */
 @Test
 public void testGetIgnoreCase() {
 attributes.put("key", "value");
 assertEquals("value", attributes.getIgnoreCase("key"));
 assertEquals("value", attributes.getIgnoreCase("Key"));
 }

 /**
 * Test getIgnoreCase method with non-existing attribute.
 */
 @Test
 public void testGetIgnoreCaseNonExisting() {
 assertEquals("", attributes.getIgnoreCase("nonExistingKey"));
 }

 /**
 * Test put method with existing key.
 */
 @Test
 public void testPutExisting() {
 attributes.put("key", "oldValue");
 attributes.put("key", "newValue");
 assertEquals("newValue", attributes.get("key"));
 }

 /**
 * Test put method with non-existing key.
 */
 @Test
 public void testPutNonExisting() {
 attributes.put("newKey", "newValue");
 assertEquals("newValue", attributes.get("newKey"));
 }

 /**
 * Test put method with the same key but different types.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testPutSameKeyDifferentTypes() {
 attributes.put("key", "oldValue");
 attributes.put("key", new Boolean(true));
 }

 /**
 * Test remove method with existing key.
 */
 @Test
 public void testRemove() {
 attributes.put("key", "value");
 attributes.remove("key");
 assertEquals("", attributes.get("key"));
 }

 /**
 * Test remove method with non-existing key.
 */
 @Test
 public void testRemoveNonExisting() {
 attributes.remove("nonExistingKey");
 assertEquals("", attributes.get("nonExistingKey"));
 }

 /**
 * Test removeIgnoreCase method with existing key.
 */
 @Test
 public void testRemoveIgnoreCase() {
 attributes.put("Key", "value");
 attributes.removeIgnoreCase("key");
 assertEquals("", attributes.get("Key"));
 }

 /**
 * Test removeIgnoreCase method with non-existing key.
 */
 @Test
 public void testRemoveIgnoreCaseNonExisting() {
 attributes.removeIgnoreCase("nonExistingKey");
 assertEquals("", attributes.get("nonExistingKey"));
 }

 /**
 * Test hasKey method with existing key.
 */
 @Test
 public void testHasKey() {
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 /**
 * Test hasKey method with non-existing key.
 */
 @Test
 public void testHasKeyNonExisting() {
 assertTrue(attributes.hasKey("nonExistingKey") == false);
 }

 /**
 * Test hasKeyIgnoreCase method with existing key.
 */
 @Test
 public void testHasKeyIgnoreCase() {
 attributes.put("key", "value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 assertTrue(attributes.hasKeyIgnoreCase("Key"));
 }

 /**
 * Test hasKeyIgnoreCase method with non-existing key.
 */
 @Test
 public void testHasKeyIgnoreCaseNonExisting() {
 assertTrue(attributes.hasKeyIgnoreCase("nonExistingKey") == false);
 }

 /**
 * Test size method.
 */
 @Test
 public void testSize() {
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 /**
 * Test addAll method.
 */
 @Test
 public void testAddAll() {
 Attributes incoming = new Attributes();
 incoming.put("key3", "value3");
 attributes.addAll(incoming);
 assertEquals(3, attributes.size());
 assertTrue(attributes.hasKey("key3"));
 }

 /**
 * Test iterator method.
 */
 @Test
 public void testIterator() {
 List<Attribute> list = attributes.asList();
 for (Attribute attribute : list) {
 assertEquals(attribute.getKey(), "key" + Integer.toString(list.indexOf(attribute) + 1));
 }
 }

 /**
 * Test dataset method.
 */
 @Test
 public void testDataset() {
 attributes.put("dataKey1", "dataValue1");
 attributes.put("dataKey2", "dataValue2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("dataValue1", dataset.get("dataKey1"));
 assertEquals("dataValue2", dataset.get("dataKey2"));
 }

 /**
 * Test html method.
 */
 @Test
 public void testHtml() {
 StringBuilder expectedHtml = new StringBuilder();
 expectedHtml.append(" ");
 expectedHtml.append("key=\"value\" ");
 attributes.put("key", "value");
 assertEquals(expectedHtml.toString(), attributes.html());
 }

 /**
 * Test equals method.
 */
 @Test
 public void testEquals() {
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

 /**
 * Test clone method.
 */
 @Test
 public void testClone() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = attributes1.clone();
 assertTrue(attributes1.equals(attributes2));
 }
}