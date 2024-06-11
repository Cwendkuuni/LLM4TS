import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 /**
 * Test constructor and basic functionality of Attributes class
 */
 @Test
 public void testAttributesConstructionAndBasicFunctionality() {
 Attributes attributes = new Attributes();
 // Test adding and getting attributes
 attributes.put("key1", "value1");
 assertEquals("value1", attributes.get("key1"));
 // Test adding and getting case-insensitive attributes
 attributes.putIgnoreCase("key1", "value2");
 assertEquals("value2", attributes.getIgnoreCase("key1"));
 // Test getting non-existent attribute
 assertEquals(Attributes.EmptyString, attributes.get("nonExistentKey"));
 // Test size
 assertEquals(2, attributes.size());
 }

 /**
 * Test edge cases and exception handling scenarios
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCapacity() {
 Attributes attributes = new Attributes(-1);
 }

 @Test
 public void testNullAttributeKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(null, "value");
 fail("Should not allow null key");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributeValue() {
 Attributes attributes = new Attributes();
 attributes.put("key", null);
 }

 @Test
 public void testAddingDuplicateAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 @Test
 public void testAddingDuplicateCaseInsensitiveAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("KEY1", "value2");
 assertEquals("value2", attributes.get("key1"));
 }

 @Test
 public void testGettingNonExistentCaseInsensitiveAttribute() {
 Attributes attributes = new Attributes();
 assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("nonExistentKey"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemovingNonExistentAttribute() {
 Attributes attributes = new Attributes();
 attributes.remove("nonExistentKey");
 }

 @Test
 public void testRemovingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.remove("key1");
 assertEquals(Attributes.EmptyString, attributes.get("key1"));
 }

 @Test
 public void testRemovingDuplicateAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key1", "value2");
 attributes.remove("key1");
 assertEquals(Attributes.EmptyString, attributes.get("key1"));
 }

 @Test
 public void testRemoveAttributeWithCaseChange() {
 Attributes attributes = new Attributes();
 attributes.putIgnoreCase("key1", "value1");
 attributes.remove("key1");
 assertEquals(Attributes.EmptyString, attributes.get("key1"));
 }

 @Test
 public void testHasKeyAndHasKeyIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertFalse(attributes.hasKey("nonExistentKey"));
 assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
 }

 @Test
 public void testRemoveAll() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 attributes.removeAll(Collections.singletonMap("key1", "value1"));
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 assertTrue(attributes.hasKey("key2"));
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 attributes1.put("key1", "value1");
 attributes2.put("key2", "value2");
 attributes1.addAll(attributes2);
 assertEquals(2, attributes1.size());
 assertTrue(attributes1.hasKey("key1"));
 assertTrue(attributes1.hasKey("key2"));
 }
}