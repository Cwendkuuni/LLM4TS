import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @After
 public void tearDown() {
 attributes = null;
 }

 @Test
 public void testGet() {
 String key = "testKey";
 String value = "testValue";
 attributes.put(key, value);
 assertEquals(value, attributes.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 attributes.put("testKey", "testValue");
 assertNull(attributes.get(null));
 }

 @Test
 public void testGetIgnoreCase() {
 String key = "TestKey";
 String value = "testValue";
 attributes.put(key, value);
 assertEquals(value, attributes.getIgnoreCase(key));
 }

 @Test
 public void testPut() {
 String key = "testKey";
 String oldValue = "oldValue";
 String newValue = "newValue";
 attributes.put(key, oldValue);
 assertEquals(oldValue, attributes.get(key));
 attributes.put(key, newValue);
 assertEquals(newValue, attributes.get(key));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 attributes.put(null, "testValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithEmptyKey() {
 attributes.put("", "testValue");
 }

 @Test
 public void testPutWithExistingKey() {
 String key = "testKey";
 String oldValue = "oldValue";
 String newValue = "newValue";
 attributes.put(key, oldValue);
 attributes.put(key, newValue);
 assertEquals(newValue, attributes.get(key));
 }

 @Test
 public void testPutIgnoreCaseWithExistingKey() {
 String key = "TestKey";
 String oldValue = "oldValue";
 String newValue = "newValue";
 attributes.put(key, oldValue);
 attributes.put("TestKey", newValue);
 assertEquals(newValue, attributes.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 attributes.put("testKey", null);
 }

 @Test
 public void testPutBooleanAttribute() {
 attributes.put("testKey", true);
 assertTrue(attributes.hasKey("testKey"));
 assertNull(attributes.get("testKey"));
 attributes.remove("testKey");
 }

 @Test
 public void testPutAttribute() {
 Attribute attribute = new Attribute("testKey", "testValue");
 attributes.put(attribute);
 assertEquals(attribute.getValue(), attributes.get(attribute.getKey()));
 }

 @Test
 public void testRemove() {
 String key = "testKey";
 String value = "testValue";
 attributes.put(key, value);
 assertTrue(attributes.hasKey(key));
 attributes.remove(key);
 assertFalse(attributes.hasKey(key));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNullKey() {
 attributes.remove(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveWithNonExistentKey() {
 attributes.remove(0);
 }

 @Test
 public void testHasKey() {
 String key = "testKey";
 String value = "testValue";
 attributes.put(key, value);
 assertTrue(attributes.hasKey(key));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 String key = "TestKey";
 String value = "testValue";
 attributes.put(key, value);
 assertTrue(attributes.hasKeyIgnoreCase(key));
 }

 @Test
 public void testHasKeyIgnoreCaseWithNonExistentKey() {
 String key = "TestKey";
 assertFalse(attributes.hasKeyIgnoreCase(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, attributes.size());
 String key = "testKey";
 String value = "testValue";
 attributes.put(key, value);
 assertEquals(1, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes2 = new Attributes();
 attributes2.put("testKey2", "testValue2");
 Attributes expected = new Attributes();
 expected.put("testKey", "testValue");
 expected.put("testKey2", "testValue2");
 attributes.addAll(attributes2);
 assertEquals(expected.size(), attributes.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddAllWithNullAttributes() {
 Attributes attributes2 = null;
 attributes.addAll(attributes2);
 }

 @Test
 public void testIterator() {
 Iterator<Attribute> iterator = attributes.iterator();
 assertEquals(0, iterator.next().getIndex());
 }

 @Test
 public void testAsList() {
 List<Attribute> list = attributes.asList();
 assertEquals(0, list.size());
 String key = "testKey";
 String value = "testValue";
 attributes.put(key, value);
 assertEquals(1, list.size());
 }

 @Test
 public void testDataset() {
 Map<String, String> dataset = attributes.dataset();
 assertEquals(0, dataset.size());
 String key = "testKey";
 String value = "testValue";
 attributes.put(key, value);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testHtml() {
 StringBuilder sb = StringUtil.borrowBuilder();
 try {
 attributes.html(sb, new Document.OutputSettings());
 String expected = " testKey=\"testValue\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testEquals() {
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 Attributes attributes4 = attributes;
 assertEquals(attributes, attributes);
 assertEquals(attributes, attributes4);
 assertNotEquals(attributes, attributes2);
 assertNotEquals(attributes, attributes3);
 }

 @Test
 public void testHashCode() {
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 Attributes attributes4 = attributes;
 int hashCode = attributes.hashCode();
 assertEquals(hashCode, attributes.hashCode());
 assertEquals(hashCode, attributes4.hashCode());
 assertNotEquals(hashCode, attributes2.hashCode());
 assertNotEquals(hashCode, attributes3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attributes cloned = attributes.clone();
 assertEquals(attributes, cloned);
 }

 @Test
 public void testNormalize() {
 Attributes attributes2 = new Attributes();
 attributes2.put("TEST KEY", "testValue");
 attributes2.normalize();
 assertEquals("test key", attributes2.get("test key"));
 }

 @Test
 public void testRemoveDuplicatesWithCaseSensitivity() {
 String key = "testKey";
 String value1 = "testValue1";
 String value2 = "testValue2";
 attributes.put(key, value1);
 attributes.put(key, value2);
 assertEquals(1, attributes.size());
 }

 @Test
 public void testRemoveDuplicatesWithCaseInsensitivity() {
 String key = "testKey";
 String value1 = "testValue1";
 String value2 = "testValue2";
 attributes.put(key, value1);
 attributes.put(key, value2);
 attributes.putIgnoreCase(key, value1);
 assertEquals(1, attributes.size());
 }
}