import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 // Test case for the get() method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 String actualValue = attributes.get("key");
 String expectedValue = "value";
 assertEquals(expectedValue, actualValue);
 }

 // Test case for the getIgnoreCase() method
 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 String actualValue = attributes.getIgnoreCase("key");
 String expectedValue = "value";
 assertEquals(expectedValue, actualValue);
 }

 // Test case for the put() method with a new attribute
 @Test
 public void testPutMethodNewAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.put("newKey", "newValue");
 String actualValue = attributes.get("newKey");
 String expectedValue = "newValue";
 assertEquals(expectedValue, actualValue);
 }

 // Test case for the put() method with an existing attribute
 @Test
 public void testPutMethodExistingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.put("key", "newValue");
 String actualValue = attributes.get("key");
 String expectedValue = "newValue";
 assertEquals(expectedValue, actualValue);
 }

 // Test case for the put() method with a boolean attribute
 @Test
 public void testPutMethodBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 boolean actualValue = attributes.get("key").equals("");
 assertTrue(actualValue);
 }

 // Test case for the put() method with an existing boolean attribute
 @Test
 public void testPutMethodExistingBooleanAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", true);
 attributes.put("key", false);
 boolean actualValue = attributes.get("key").equals("");
 assertFalse(actualValue);
 }

 // Test case for the remove() method with an existing attribute
 @Test
 public void testRemoveMethodExistingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 attributes.remove("key");
 String actualValue = attributes.get("key");
 assertEquals(EmptyString, actualValue);
 }

 // Test case for the remove() method with a non-existing attribute
 @Test
 public void testRemoveMethodNonExistingAttribute() {
 Attributes attributes = new Attributes();
 attributes.remove("nonExistingKey");
 String actualValue = attributes.get("nonExistingKey");
 assertEquals(EmptyString, actualValue);
 }

 // Test case for the hasKey() method with an existing attribute
 @Test
 public void testHasKeyMethodExistingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("key", "value");
 boolean actualValue = attributes.hasKey("key");
 assertTrue(actualValue);
 }

 // Test case for the hasKey() method with a non-existing attribute
 @Test
 public void testHasKeyMethodNonExistingAttribute() {
 Attributes attributes = new Attributes();
 boolean actualValue = attributes.hasKey("nonExistingKey");
 assertFalse(actualValue);
 }

 // Test case for the hasKeyIgnoreCase() method with an existing attribute
 @Test
 public void testHasKeyIgnoreCaseMethodExistingAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("Key", "value");
 boolean actualValue = attributes.hasKeyIgnoreCase("key");
 assertTrue(actualValue);
 }

 // Test case for the hasKeyIgnoreCase() method with a non-existing attribute
 @Test
 public void testHasKeyIgnoreCaseMethodNonExistingAttribute() {
 Attributes attributes = new Attributes();
 boolean actualValue = attributes.hasKeyIgnoreCase("nonExistingKey");
 assertFalse(actualValue);
 }

 // Test case for the size() method with empty Attributes
 @Test
 public void testSizeMethodEmptyAttributes() {
 Attributes attributes = new Attributes();
 int actualSize = attributes.size();
 int expectedSize = 0;
 assertEquals(expectedSize, actualSize);
 }

 // Test case for the size() method with attributes
 @Test
 public void testSizeMethodAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 int actualSize = attributes.size();
 int expectedSize = 2;
 assertEquals(expectedSize, actualSize);
 }

 // Test case for the addAll() method with empty Attributes
 @Test
 public void testAddAllMethodEmptyAttributes() {
 Attributes attributes = new Attributes();
 Attributes otherAttributes = new Attributes();
 otherAttributes.put("key1", "value1");
 otherAttributes.put("key2", "value2");
 attributes.addAll(otherAttributes);

 String actualValue1 = attributes.get("key1");
 String expectedValue1 = "value1";
 assertEquals(expectedValue1, actualValue1);

 String actualValue2 = attributes.get("key2");
 String expectedValue2 = "value2";
 assertEquals(expectedValue2, actualValue2);
 }

 // Test case for the addAll() method with existing Attributes
 @Test
 public void testAddAllMethodExistingAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 Attributes otherAttributes = new Attributes();
 otherAttributes.put("key2", "value2");
 attributes.addAll(otherAttributes);

 String actualValue1 = attributes.get("key1");
 String expectedValue1 = "value1";
 assertEquals(expectedValue1, actualValue1);

 String actualValue2 = attributes.get("key2");
 String expectedValue2 = "value2";
 assertEquals(expectedValue2, actualValue2);
 }

 // Test case for the asList() method with empty Attributes
 @Test
 public void testAsListMethodEmptyAttributes() {
 Attributes attributes = new Attributes();
 List<Attribute> actualList = attributes.asList();
 assertTrue(actualList.isEmpty());
 }

 // Test case for the asList() method with attributes
 @Test
 public void testAsListMethodAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 List<Attribute> actualList = attributes.asList();
 List<Attribute> expectedList = Arrays.asList(new Attribute("key1", "value1"), new Attribute("key2", "value2"));
 assertEquals(expectedList, actualList);
 }

 // Test case for the dataset() method with empty Attributes
 @Test
 public void testDatasetMethodEmptyAttributes() {
 Attributes attributes = new Attributes();
 Map<String, String> actualDataset = attributes.dataset();
 assertTrue(actualDataset.isEmpty());
 }

 // Test case for the dataset() method with attributes
 @Test
 public void testDatasetMethodAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 Map<String, String> actualDataset = attributes.dataset();
 Map<String, String> expectedDataset = new HashMap<>();
 expectedDataset.put("key1", "value1");
 expectedDataset.put("key2", "value2");
 assertEquals(expectedDataset, actualDataset);
 }
}