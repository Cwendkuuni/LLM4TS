import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String value = attributes.get("key");
 assertEquals("value", value);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 String value = attributes.getIgnoreCase("key");
 assertEquals("Value", value);
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value1");
 attributes.put("key", "Value2");
 assertEquals("Value2", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.get("key").equals(""));
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testPutIgnoreCaseBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("Key", true);
 assertTrue(attributes.getIgnoreCase("key").equals(""));
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 attributes.removeIgnoreCase("key");
 assertFalse(attributes.hasKey("Key"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertTrue(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 Attributes result = new Attributes();
 attributes1.addAll(attributes2);
 assertEquals(2, result.size());
 assertTrue(result.hasKey("key1"));
 assertTrue(result.hasKey("key2"));
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> attributesList = attributes.asList();
 Iterator<Attribute> iterator = attributes.iterator();
 Attribute attribute = null;
 while (iterator.hasNext()) {
 attribute = iterator.next();
 assertEquals(attribute.getKey(), "key1" || "key2");
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertTrue(dataset.containsKey("data-key1"));
 assertTrue(dataset.containsKey("data-key2"));
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("<key>value</key>", attributes.html());
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();attributes1.put("key1", "value1");attributes1.put("key2", "value2");
 Attributes attributes2 = new Attributes();attributes2.put("key1", "value1");attributes2.put("key2", "value2");
 Attributes attributes3 = new Attributes();attributes3.put("key1", "value1");attributes3.put("key3", "value3");assertTrue(attributes1.equals(attributes2));assertTrue(attributes1.equals(attributes3));

 Attributes attributes4 = new Attributes();assertFalse(attributes1.equals(attributes4));

 Attributes attributes5 = new Attributes();attributes5.put("Key1", "Value1");assertFalse(attributes1.equals(attributes5));
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();attributes.put("key", "value");Attributes clonedAttributes = attributes.clone();assertEquals(attributes.size(), clonedAttributes.size());assertTrue(attributes.equals(clonedAttributes)); }
}