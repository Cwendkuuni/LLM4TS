import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

 @Test
 public void testSize() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());

 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 Assert.assertEquals(2, attributes.size());
 }

 @Test
 public void testGet() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals( "", attributes.get("key2"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals( "", attributes.getIgnoreCase("key2"));
 }

 @Test
 public void testPut() {
 Attributes attributes = new Attributes();

 attributes.put("key1", "value1");
 attributes.put("key1", "value2");

 Assert.assertEquals("value2", attributes.get("key1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 Attributes attributes = new Attributes();

 attributes.put(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 Attributes attributes = new Attributes();

 attributes.put("key", null);
 }

 @Test
 public void testPutIgnoreCase() {
 Attributes attributes = new Attributes();

 attributes.put("Key1", "Value1");
 attributes.putIgnoreCase("key1", "Value2");

 Assert.assertEquals("Value2", attributes.getIgnoreCase("Key1"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = new Attributes();

 attributes.put("key", true);
 Assert.assertEquals("", attributes.get("key"));
 }

 @Test
 public void testPutBooleanIgnoreCase() {
 Attributes attributes = new Attributes();

 attributes.put("Key", true);
 attributes.putIgnoreCase("key", false);
 Assert.assertEquals("", attributes.get("Key"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 Assert.assertTrue(attributes.hasKey("key1"));
 Assert.assertFalse(attributes.hasKey("key2"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY2"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");

 Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 }

 @Test
 public void testRemove() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 attributes.remove("key1");
 Assert.assertEquals("value2", attributes.get("key2"));
 Assert.assertFalse(attributes.hasKey("key1"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("Key1", "Value1");
 attributes.put("key2", "Value2");

 attributes.removeIgnoreCase("key1");
 Assert.assertEquals("Value2", attributes.getIgnoreCase("Key1"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key1"));
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

 Assert.assertEquals(2, attributes3.size());
 Assert.assertEquals("value1", attributes3.get("key1"));
 Assert.assertEquals("value2", attributes3.get("key2"));
 }

 @Test
 public void testAddAllIgnoreCase() {
 Attributes attributes1 = new Attributes();
 attributes1.put("Key1", "Value1");

 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "Value2");

 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);

 Assert.assertEquals(2, attributes3.size());
 Assert.assertEquals("Value1", attributes3.getIgnoreCase("Key1"));
 Assert.assertEquals("Value2", attributes3.getIgnoreCase("key2"));
 }

 @Test
 public void testIterator() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");

 List<Map.Entry<String, String>> list = attributes.asList();

 for (Map.Entry<String, String> entry : list) {
 System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
 }
 }

 @Test
 public void testDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");

 Map<String, String> dataset = attributes.dataset();

 Assert.assertEquals(2, dataset.size());
 Assert.assertEquals("value1", dataset.get("data-key1"));
 Assert.assertEquals("value2", dataset.get("data-key2"));
 }
}