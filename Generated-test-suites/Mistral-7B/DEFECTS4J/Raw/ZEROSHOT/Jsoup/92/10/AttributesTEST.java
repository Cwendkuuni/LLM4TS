import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 @Test
 public void testInitialization() {
 Attributes attributes = new Attributes();
 assertEquals(0, attributes.size());
 }

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 assertNull(attributes.get("otherKey"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 assertNull(attributes.getIgnoreCase("otherKey"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value2", attributes.get("key2"));
 assertEquals("value1", attributes.get("key1"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("key1", "value1");
 attributes.putIgnoreCase("key2", "value2");
 assertEquals("value2", attributes.getIgnoreCase("key2"));
 assertEquals("value1", attributes.getIgnoreCase("key1"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertFalse(attributes.get("key").equals("true"));
 assertNull(attributes.get("key")).isDisplayName("Expected empty string for boolean attribute");
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 attributes.remove("key1");
 assertEquals("value2", attributes.get("key2"));
 assertEquals(1, attributes.size());
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");
 assertEquals(2, attributes.size());
 attributes.removeIgnoreCase("key1");
 assertEquals("Value2", attributes.get("Key2"));
 assertEquals(1, attributes.size());
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 assertFalse(attributes.hasKey("otherKey"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
 }

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals(2, attributes.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnmodifiableList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 list.add(new Attribute("key3", "value3"));
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("value1", dataset.get("data-key1"));
 assertEquals("value2", dataset.get("data-key2"));
 }

 @Test
 public void testHTML() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 StringBuilder stringBuilder = new StringBuilder();
 try {
 attributes.html(stringBuilder);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 assertEquals(" key1=\"value1\" key2=\"value2\"", stringBuilder.toString());
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();attributes1.put("key1", "value1");
 Attributes attributes2 = new Attributes();attributes2.put("key1", "value1");
 Attributes attributes3 = new Attributes();attributes3.put("key1", "value2");
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();attributes.put("key1", "value1");
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.get("key1"), clonedAttributes.get("key1"));
 }

}