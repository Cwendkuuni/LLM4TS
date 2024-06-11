import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for constructor and initial state
 @Test
 public void testEmptyAttributes() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 }

 // Test case for adding attributes
 @Test
 public void testAddAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Assert.assertEquals(2, attributes.size());
 }

 // Test case for adding duplicate attributes
 @Test
 public void testAddDuplicateAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 Assert.assertEquals("value2", attributes.get("key"));
 }

 // Test case for getting attributes by key (case sensitive)
 @Test
 public void testGetAttributeCaseSensitive() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.get("key"));
 Assert.assertEquals(EmptyString, attributes.get("otherKey"));
 }

 // Test case for getting attributes by key (case insensitive)
 @Test
 public void testGetAttributeIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("KEY"));
 Assert.assertEquals(EmptyString, attributes.getIgnoreCase("otherKey"));
 }

 // Test case for removing attributes
 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 Assert.assertEquals(EmptyString, attributes.get("key"));
 Assert.assertEquals(0, attributes.size());
 }

 // Test case for checking if attributes contain a key (case sensitive)
 @Test
 public void testHasKey() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertTrue(attributes.hasKey("key"));
 Assert.assertFalse(attributes.hasKey("otherKey"));
 }

 // Test case for checking if attributes contain a key (case insensitive)
 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("Key"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
 }

 // Test case for cloning attributes
 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Attributes clonedAttributes = attributes.clone();
 Assert.assertEquals(2, clonedAttributes.size());
 Assert.assertEquals("value1", clonedAttributes.get("key1"));
 Assert.assertEquals("value2", clonedAttributes.get("key2"));
 }

 // Test case for normalizing attributes
 @Test
 public void testNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("KEY2", "value2");
 attributes.normalize();
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 }

 // Test case for handling empty attributes
 @Test
 public void testEmptyAttributesMethods() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 Assert.assertEquals(EmptyString, attributes.get("key"));
 Assert.assertFalse(attributes.hasKey("key"));
 }

 // Test case for handling null values
 @Test
 public void testNullAttributeValues() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 Assert.assertEquals(EmptyString, attributes.get("key"));
 Assert.assertFalse(attributes.hasKey("key"));
 }

 // Test case for adding an attribute with a null key
 @Test(expected = NullPointerException.class)
 public void testNullAttributeKey() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 }

 // Test case for adding an attribute with a null value
 @Test
 public void testNullAttributeValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 // Test case for adding an attribute with a duplicate key and null value
 @Test
 public void testAddDuplicateNullAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 attributes.put("key", null);
 Assert.assertEquals(null, attributes.get("key"));
 }

 // Test case for adding an attribute with a non-String value
 @Test(expected = IllegalArgumentException.class)
 public void testAddAttributeWithNonStringValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", new Object());
 }

 // Test case for adding an attribute with a key starting with "data-"
 @Test
 public void testAddDataAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "value");
 Assert.assertEquals("value", attributes.get("data-key"));
 }

 // Test case for adding multiple data attributes with the same key
 @Test
 public void testAddMultipleDataAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "value1");
 attributes.put("data-key", "value2");
 Assert.assertEquals("value2", attributes.get("data-key"));
 }

 // Test case for removing a data attribute
 @Test
 public void testRemoveDataAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "value");
 attributes.remove("data-key");
 Assert.assertEquals(EmptyString, attributes.get("data-key"));
 }

 // Test case for checking if attributes contain a data attribute (case sensitive)
 @Test
 public void testHasDataAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "value");
 Assert.assertTrue(attributes.hasKey("data-key"));
 Assert.assertFalse(attributes.hasKey("otherKey"));
 }

 // Test case for checking if attributes contain a data attribute (case insensitive)
 @Test
 public void testHasDataAttributeIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("data-key", "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("data-key"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("DATA-KEY"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
 }

 // Test case for getting all data attributes as a map
 @Test
 public void testGetAllDataAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("data-key1", "value1");
 attributes.put("data-key2", "value2");
 Map<String, String> dataAttributes = attributes.dataset();
 Assert.assertEquals(2, dataAttributes.size());
 Assert.assertEquals("value1", dataAttributes.get("data-key1"));
 Assert.assertEquals("value2", dataAttributes.get("data-key2"));
 }
}