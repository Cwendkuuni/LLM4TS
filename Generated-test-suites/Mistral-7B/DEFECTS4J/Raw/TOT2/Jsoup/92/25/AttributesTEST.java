import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.InitialCapacity;
import static org.jsoup.nodes.Attributes.GrowthFactor;

public class AttributesTest {

 private Attributes createTestAttributes() {
 return new Attributes();
 }

 @Test
 public void testGetEmptyAttributes() {
 Attributes attributes = createTestAttributes();
 Assert.assertEquals(InitialCapacity, attributes.size());
 Assert.assertEquals(EmptyString, attributes.get(EmptyString));
 Assert.assertEquals(EmptyString, attributes.getIgnoreCase(EmptyString));
 }

 @Test
 public void testGetSingleAttribute() {
 Attributes attributes = createTestAttributes();
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.get("key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 Assert.assertEquals(EmptyString, attributes.get("otherKey"));
 Assert.assertEquals(EmptyString, attributes.getIgnoreCase("otherKey"));
 }

 @Test
 public void testGetSingleAttributeIgnoreCase() {
 Attributes attributes = createTestAttributes();
 attributes.put("Key", "Value");
 Assert.assertEquals("Value", attributes.getIgnoreCase("key"));
 Assert.assertEquals("Value", attributes.getIgnoreCase("Key"));
 Assert.assertEquals(EmptyString, attributes.get("otherKey"));
 Assert.assertEquals(EmptyString, attributes.getIgnoreCase("otherKey"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 Attributes attributes = createTestAttributes();
 attributes.put(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 Attributes attributes = createTestAttributes();
 attributes.put("key", null);
 }

 @Test
 public void testPutSameKeyTwice() {
 Attributes attributes = createTestAttributes();
 attributes.put("key", "value1");
 attributes.put("key", "value2");
 Assert.assertEquals("value2", attributes.get("key"));
 Assert.assertEquals("value2", attributes.getIgnoreCase("key"));
 }

 @Test
 public void testPutDuplicateKeyIgnoreCase() {
 Attributes attributes = createTestAttributes();
 attributes.put("Key", "value1");
 attributes.put("key", "value2");
 Assert.assertEquals("value2", attributes.get("key"));
 Assert.assertEquals("value2", attributes.getIgnoreCase("Key"));
 }

 @Test
 public void testPutMultipleAttributes() {
 Attributes attributes = createTestAttributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key3", "value3");
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
 Assert.assertEquals("value2", attributes.get("key2"));
 Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
 Assert.assertEquals("value3", attributes.get("key3"));
 Assert.assertEquals("value3", attributes.getIgnoreCase("key3"));
 }

 @Test
 public void testPutBooleanAttribute() {
 Attributes attributes = createTestAttributes();
 attributes.put("key", true);
 Assert.assertEquals(EmptyString, attributes.get("key"));
 Assert.assertTrue(attributes.hasKey("key"));
 }

 @Test
 public void testPutBooleanAttributeRemove() {
 Attributes attributes = createTestAttributes();
 attributes.put("key", true);
 attributes.remove("key");
 Assert.assertFalse(attributes.hasKey("key"));
 }

 @Test
 public void testPutAndRemoveSingleAttribute() {
 Attributes attributes = createTestAttributes();
 attributes.put("key", "value");
 Assert.assertEquals("value", attributes.get("key"));
 Assert.assertEquals("value", attributes.getIgnoreCase("key"));
 attributes.remove("key");
 Assert.assertFalse(attributes.hasKey("key"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
 }

 @Test
 public void testRemoveNonExistingAttribute() {
 Attributes attributes = createTestAttributes();
 attributes.remove("nonExistingKey");
 Assert.assertFalse(attributes.hasKey("nonExistingKey"));
 }

 @Test
 public void testRemoveIgnoreCaseNonExistingAttribute() {
 Attributes attributes = createTestAttributes();
 attributes.removeIgnoreCase("nonExistingKey");
 Assert.assertFalse(attributes.hasKeyIgnoreCase("nonExistingKey"));
 }

 @Test
 public void testSizeAndCapacity() {
 Attributes attributes = createTestAttributes();
 for (int i = 0; i < GrowthFactor * InitialCapacity; i++) {
 attributes.put("key" + i, "value" + i);
 }
 Assert.assertEquals(GrowthFactor * InitialCapacity, attributes.size());
 }

 @Test
 public void testHasKey() {
 Attributes attributes = createTestAttributes();
 attributes.put("key1", "value1");
 Assert.assertTrue(attributes.hasKey("key1"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
 Assert.assertFalse(attributes.hasKey("key2"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = createTestAttributes();
 attributes.put("Key1", "value1");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("Key1"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("Key2"));
 }

 @Test
 public void testSizeAndRemove() {
 Attributes attributes = createTestAttributes();
 attributes.put("key1", "value1");
 Assert.assertEquals(1, attributes.size());
 attributes.remove("key1");
 Assert.assertEquals(0, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = createTestAttributes();
 Attributes attributes2 = createTestAttributes();
 attributes1.put("key1", "value1");
 attributes1.put("key2", "value2");
 attributes2.put("key2", "value3");
 attributes2.put("key3", "value3");
 attributes1.addAll(attributes2);
 Assert.assertEquals(3, attributes1.size());
 Assert.assertEquals("value2", attributes1.get("key2"));
 Assert.assertEquals("value3", attributes1.get("key3"));
 Assert.assertTrue(attributes1.hasKey("key1"));
 Assert.assertTrue(attributes1.hasKey("key2"));
 Assert.assertTrue(attributes1.hasKey("key3"));
 }

 @Test
 public void testRemoveByIndex() {
 Attributes attributes = createTestAttributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.put("key3", "value3");
 attributes.remove(1);
 Assert.assertEquals(2, attributes.size());
 Assert.assertEquals("value1", attributes.get("key1"));
 Assert.assertEquals("value3", attributes.get("key3"));
 Assert.assertFalse(attributes.hasKey("key2"));
 }
}