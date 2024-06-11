import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testEmptyAttributes() {
 Attributes emptyAttributes = new Attributes();
 assertTrue(emptyAttributes.size() == 0);
 }

 @Test
 public void testAddAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testAddIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "value");
 assertEquals("value", attributes.get("key"));
 }

 @Test
 public void testPutIgnoringCaseAttributeWithSameKey() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value1");
 attributes.putIgnoreCase("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 assertTrue(attributes.get("key").equals(""));
 }

 @Test
 public void testPutBooleanAttributeAndRemoveIt() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testAddAttributeWithSameKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 assertEquals("value2", attributes.get("key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyWhenAddingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueWhenAddingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testRemoveIgnoreCaseAttribute() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("Key", "value");
 attributes.removeIgnoreCase("key");
 assertFalse(attributes.hasKey("key"));
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
 attributes.putIgnoreCase("Key", "value");
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
 assertTrue(attributes3.hasKey("key1"));
 assertTrue(attributes3.hasKey("key2"));
 }

 @Test
 public void testAsList() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> list = attributes.asList();
 assertEquals(2, list.size());
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "data-value1");
 attributes.put("data-key2", "data-value2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 }

 @Test
 public void testHtml() {
 Attributes attributes = new Attributes();attributes.put("key", "value");String html = attributes.html();assertEquals("<key>value</key>", html); }

 @Test
 public void testToString() {
 Attributes attributes = new Attributes();attributes.put("key", "value");assertEquals("<key>value</key>", attributes.toString()); }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();attributes1.put("key1", "value1");Attributes attributes2 = new Attributes();attributes2.put("key1", "value1");assertTrue(attributes1.equals(attributes2)); }

 @Test
 public void testHashCode() {
 Attributes attributes1 = new Attributes();attributes1.put("key1", "value1");Attributes attributes2 = new Attributes();attributes2.put("key1", "value1");assertEquals(attributes1.hashCode(), attributes2.hashCode()); }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();attributes.put("key", "value");Attributes clonedAttributes = attributes.clone();assertEquals(attributes.size(), clonedAttributes.size()); }
}