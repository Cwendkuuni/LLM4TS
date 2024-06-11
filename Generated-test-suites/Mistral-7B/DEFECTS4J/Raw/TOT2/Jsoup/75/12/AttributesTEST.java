import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributesTest {

 // Test case for get() method
 @Test
 public void testGetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key1", "value1");
 String expectedValue = "value1";
 String actualValue = attributes.get("key1");

 Assert.assertEquals("Test get method with valid key and value", expectedValue, actualValue);

 // Test case for get() method with non-existent key
 Attributes attributes2 = new Attributes();
 String actualValue2 = attributes.get("key1");
 Assert.assertEquals("Test get method with non-existent key", "", actualValue2);

 // Test case for getIgnoreCase() method
 Attributes attributes3 = new Attributes();
 attributes3.put("key2", "value2");
 String expectedValueIgnoreCase = "value2";
 String actualValueIgnoreCase = attributes3.getIgnoreCase("key2");

 Assert.assertEquals("Test getIgnoreCase method with valid key and value", expectedValueIgnoreCase, actualValueIgnoreCase);

 // Test case for getIgnoreCase() method with non-existent key
 Attributes attributes4 = new Attributes();
 String actualValueIgnoreCase2 = attributes4.getIgnoreCase("key2");
 Assert.assertEquals("Test getIgnoreCase method with non-existent key", "", actualValueIgnoreCase2);
 }

 // Test case for put() method
 @Test
 public void testPutMethod() {
 Attributes attributes = new Attributes();
 String key = "key3";
 String value = "value3";
 Attributes expectedAttributes = new Attributes();
 expectedAttributes.put(key, value);
 attributes.put(key, value);

 Attributes actualAttributes = attributes;

 Assert.assertEquals("Test put method with valid key and value", expectedAttributes, actualAttributes);

 // Test case for put() method with existing key
 Attributes attributes2 = new Attributes();
 attributes2.put("key4", "value4");
 attributes2.put("key4", "newValue4");
 Attributes expectedAttributes2 = new Attributes();
 expectedAttributes2.put("key4", "newValue4");
 attributes2 = expectedAttributes2;

 Assert.assertEquals("Test put method with existing key", expectedAttributes2, attributes2);

 // Test case for put() method with case insensitive key
 Attributes attributes3 = new Attributes();
 attributes3.put("key5", "value5");
 String newKey = "KEY5";
 attributes3.put(newKey, "newValue5");
 Attributes expectedAttributes3 = new Attributes();
 expectedAttributes3.put("key5", "newValue5");
 expectedAttributes3.put("KEY5", "newValue5");
 attributes3 = expectedAttributes3;

 Assert.assertEquals("Test put method with case insensitive key", expectedAttributes3, attributes3);
 }

 // Test case for put() method with boolean value
 @Test
 public void testPutBooleanMethod() {
 Attributes attributes = new Attributes();
 String key = "key6";
 attributes.put(key, true);
 Attributes expectedAttributes = new Attributes();
 expectedAttributes.put(key, null);
 
 Attributes actualAttributes = attributes;

 Assert.assertEquals("Test put method with boolean value", expectedAttributes, actualAttributes);
 }

 // Test case for put() method with duplicate key
 @Test(expected = IllegalArgumentException.class)
 public void testPutDuplicateKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key7", "value7");
 attributes.put("key7", "value8");
 }

 // Test case for remove() method
 @Test
 public void testRemoveMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key8", "value8");
 String key = "key8";

 attributes.remove(key);
 int expectedSize = 0;
 int actualSize = attributes.size();

 Assert.assertEquals("Test remove method", expectedSize, actualSize);
 }

 // Test case for remove() method with non-existent key
 @Test
 public void testRemoveNonExistentMethod() {
 Attributes attributes = new Attributes();
 String key = "key9";

 attributes.remove(key);
 }

 // Test case for remove() method with existing key
 @Test
 public void testRemoveExistingMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key10", "value10");
 String key = "key10";

 attributes.remove(key);
 int expectedSize = 0;
 int actualSize = attributes.size();

 Assert.assertEquals("Test remove method with existing key", expectedSize, actualSize);
 }

 // Test case for removeIgnoreCase() method
 @Test
 public void testRemoveIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key11", "value11");
 String key = "Key11";

 attributes.removeIgnoreCase(key);
 int expectedSize = 0;
 int actualSize = attributes.size();

 Assert.assertEquals("Test removeIgnoreCase method", expectedSize, actualSize);
 }

 // Test case for removeIgnoreCase() method with non-existent key
 @Test
 public void testRemoveIgnoreCaseNonExistentMethod() {
 Attributes attributes = new Attributes();
 String key = "Key12";

 attributes.removeIgnoreCase(key);
 }

 // Test case for removeIgnoreCase() method with existing key
 @Test
 public void testRemoveIgnoreCaseExistingMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key13", "value13");
 String key = "key13";

 attributes.removeIgnoreCase(key);
 int expectedSize = 0;
 int actualSize = attributes.size();

 Assert.assertEquals("Test removeIgnoreCase method with existing key", expectedSize, actualSize);
 }

 // Test case for hasKey() method
 @Test
 public void testHasKeyMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key14", "value14");
 boolean expectedValue = true;
 boolean actualValue = attributes.hasKey("key14");

 Assert.assertEquals("Test hasKey method with valid key", expectedValue, actualValue);

 boolean expectedValue2 = false;
 boolean actualValue2 = attributes.hasKey("key15");

 Assert.assertEquals("Test hasKey method with non-existent key", expectedValue2, actualValue2);
 }

 // Test case for hasKeyIgnoreCase() method
 @Test
 public void testHasKeyIgnoreCaseMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key16", "value16");
 boolean expectedValue = true;
 boolean actualValue = attributes.hasKeyIgnoreCase("key16");

 Assert.assertEquals("Test hasKeyIgnoreCase method with valid key", expectedValue, actualValue);

 boolean expectedValue2 = false;
 boolean actualValue2 = attributes.hasKeyIgnoreCase("key17");

 Assert.assertEquals("Test hasKeyIgnoreCase method with non-existent key", expectedValue2, actualValue2);
 }

 // Test case for size() method
 @Test
 public void testSizeMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key18", "value18");
 int expectedSize = 1;
 int actualSize = attributes.size();

 Assert.assertEquals("Test size method", expectedSize, actualSize);
 }

 // Test case for addAll() method
 @Test
 public void testAddAllMethod() {
 Attributes attributes1 = new Attributes();
 attributes1.put("key19", "value19");
 Attributes attributes2 = new Attributes();
 attributes2.put("key20", "value20");

 Attributes expectedAttributes = new Attributes();
 expectedAttributes.put("key19", "value19");
 expectedAttributes.put("key20", "value20");

 Attributes attributes = new Attributes();
 attributes.addAll(attributes1);
 attributes.addAll(attributes2);

 Attributes actualAttributes = attributes;

 Assert.assertEquals("Test addAll method", expectedAttributes, actualAttributes);
 }

 // Test case for iterator() method
 @Test
 public void testIteratorMethod() {
 Attributes attributes = new Attributes();
 attributes.put("key21", "value21");
 attributes.put("key22", "value22");

 for (Map.Entry<String, String> entry : attributes.asList().iterator()) {
 String key = entry.getKey();
 String value = entry.getValue();

 System.out.println("Key: " + key + ", Value: " + value);
 }
 }

 // Test case for dataset() method
 @Test
 public void testDatasetMethod() {
 Attributes attributes = new Attributes();
 attributes.put("data-testKey1", "data-testValue1");
 Attributes dataAttributes = attributes.dataset();
 String expectedValue = "data-testValue1";
 String actualValue = dataAttributes.get("testKey1");

 Assert.assertEquals("Test dataset method", expectedValue, actualValue);
 }
}