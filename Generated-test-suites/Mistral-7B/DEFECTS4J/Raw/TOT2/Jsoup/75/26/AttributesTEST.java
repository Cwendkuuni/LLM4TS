import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 @Test
 public void testGetAndPut() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "Value");
 assertEquals("Value", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.hasKey("key"));
 assertNull(attributes.get("key"));
 }

 @Test
 public void testPutWithExistingKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 }

 @Test
 public void testPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 assertNull(attributes.get("key"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertTrue(attributes.hasKey("key"));
 assertFalse(attributes.hasKey("other"));
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
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 assertEquals(2, attributes3.size());
 assertEquals("value1", attributes3.get("key1"));
 assertEquals("value2", attributes3.get("key2"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 assertEquals("value2", attributes.get("key2"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("Key2", "Value2");
 attributes.removeIgnoreCase("key1");
 assertFalse(attributes.hasKey("key1"));
 assertEquals("Value2", attributes.get("key2"));
 }

 @Test
 public void testRemoveWithNullKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove(null);
 assertEquals("value", attributes.get("key"));
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
 public void testHtml() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 StringBuilder sb = new StringBuilder();
 try {
 attributes.html(sb, null);
 } catch (IOException e) {
 // should not happen
 }
 assertEquals("<key=\"value\">", sb.toString());
 }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("<key=\"value\">", attributes.toString());
 }

 @Test
 public void testClone() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key1", "value1");
 Attributes attributes2 = attributes1.clone();
 assertEquals(attributes1, attributes2);
 }
}