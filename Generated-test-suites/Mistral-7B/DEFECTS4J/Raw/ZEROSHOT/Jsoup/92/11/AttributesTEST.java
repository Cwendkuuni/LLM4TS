import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

 @Test
 public void testAttributes() {
 Attributes attributes = new Attributes();

 // Test get method
 attributes.put("key1", "value1");
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals(EmptyString, attributes.get("key2"));

 // Test getIgnoreCase method
 attributes.put("Key1", "Value1");
 Assert.assertEquals("Value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals("Value1", attributes.getIgnoreCase("Key1"));

 // Test put method
 attributes.put("key2", "value2");
 Assert.assertEquals("value2", attributes.get("key2"));

 // Test putIgnoreCase method
 attributes.putIgnoreCase("KEY2", "value3");
 Assert.assertEquals("value3", attributes.getIgnoreCase("key2"));
 Assert.assertEquals("value3", attributes.getIgnoreCase("KEY2"));

 // Test put boolean attribute
 attributes.put("booleanKey", true);
 Assert.assertTrue(attributes.hasKey("booleanKey"));
 Assert.assertEquals(EmptyString, attributes.get("booleanKey"));

 // Test put method with existing key
 attributes.put("key1", "value4");
 Assert.assertEquals("value4", attributes.get("key1"));

 // Test remove method
 attributes.remove("key1");
 Assert.assertFalse(attributes.hasKey("key1"));

 // Test addAll method
 Attributes attributes2 = new Attributes();
 attributes2.put("key3", "value3");
 attributes.addAll(attributes2);
 Assert.assertEquals(3, attributes.size());
 Assert.assertEquals("value3", attributes.get("key3"));

 // Test dataset method
 Map<String, String> dataset = attributes.dataset();
 dataset.put("newKey", "newValue");
 attributes.normalize();
 Assert.assertEquals(4, attributes.size());
 Assert.assertEquals("newValue", attributes.getIgnoreCase("newkey"));

 // Test html method
 Assert.assertEquals("<key1=\"value1\" key2=\"\">", attributes.html());
 }
}