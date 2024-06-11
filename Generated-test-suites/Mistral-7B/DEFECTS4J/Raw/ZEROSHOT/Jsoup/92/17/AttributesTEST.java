import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class AttributesTest {

 @Test
 public void testAttributes() {
 Attributes attributes = new Attributes();

 // Test setting attributes
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key3", "value3");

 // Test getting attributes
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 Assert.assertEquals("value3", attributes.get("key3"));

 // Test getting attributes ignoring case
 Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
 Assert.assertEquals("value3", attributes.getIgnoreCase("key3"));

 // Test removing attributes
 attributes.remove("key1");
 Assert.assertNull(attributes.get("key1"));

 // Test removing attributes ignoring case
 attributes.removeIgnoreCase("key1");
 Assert.assertNull(attributes.get("key1"));

 // Test checking if attributes contain a key
 Assert.assertTrue(attributes.hasKey("key2"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key2"));
 Assert.assertFalse(attributes.hasKey("key4"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key4"));

 // Test getting size
 Assert.assertEquals(3, attributes.size());

 // Test adding all attributes from another set
 Attributes attributes2 = new Attributes();
 attributes2.put("key4", "value4");
 attributes2.put("key5", "value5");
 attributes.addAll(attributes2);
 Assert.assertEquals(5, attributes.size());
 Assert.assertEquals("value4", attributes.get("key4"));
 Assert.assertEquals("value5", attributes.get("key5"));

 // Test getting attributes as a list
 List<Attributes.Attribute> list = attributes.asList();
 Assert.assertEquals(5, list.size());
 Assert.assertEquals("key1", list.get(0).getKey());
 Assert.assertNull(list.get(0).getValue()); // boolean attribute
 Assert.assertEquals("key2", list.get(1).getKey());
 Assert.assertEquals("value2", list.get(1).getValue());
 Assert.assertEquals("key3", list.get(2).getKey());
 Assert.assertEquals("value3", list.get(2).getValue());
 Assert.assertEquals("key4", list.get(3).getKey());
 Assert.assertEquals("value4", list.get(3).getValue());
 Assert.assertEquals("key5", list.get(4).getKey());
 Assert.assertEquals("value5", list.get(4).getValue());
 }
}