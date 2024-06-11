import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 private Attributes attributes; // for testing the parent Attribute reference

 @Before
 public void setUp() {
 this.attributes = new Attributes();
 }

 /**
 * Test constructor with unencoded key and value
 */
 @Test
 public void testAttributeConstructorWithUnencodedKeyAndValue() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 }

 /**
 * Test constructor with unencoded key, value and parent
 */
 @Test
 public void testAttributeConstructorWithUnencodedKeyValueAndParent() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value, this.attributes);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 Assert.assertNull(attribute.getParent());
 }

 /**
 * Test getKey method
 */
 @Test
 public void testGetKeyMethod() {
 String key = "testKey";
 Attribute attribute = new Attribute(key, null, null);
 Assert.assertEquals(key, attribute.getKey());
 }

 /**
 * Test setKey method
 */
 @Test
 public void testSetKeyMethod() {
 String originalKey = "testKey";
 String newKey = "newTestKey";
 Attribute attribute = new Attribute(originalKey, null, null);
 attribute.setKey(newKey);
 Assert.assertEquals(newKey, attribute.getKey());
 Assert.assertNotEquals(originalKey, attribute.getKey());
 }

 /**
 * Test getValue method
 */
 @Test
 public void testGetValueMethod() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value, null);
 Assert.assertEquals(value, attribute.getValue());
 }

 /**
 * Test setValue method
 */
 @Test
 public void testSetValueMethod() {
 String originalValue = "testValue";
 String newValue = "newTestValue";
 Attribute attribute = new Attribute("testKey", originalValue, null);
 String oldValue = attribute.setValue(newValue);
 Assert.assertEquals(newValue, attribute.getValue());
 Assert.assertNotEquals(originalValue, attribute.getValue());
 Assert.assertNull(oldValue);
 }

 /**
 * Test html method with null parent
 */
 @Test(expected = SerializationException.class)
 public void testHtmlMethodWithNullParent() {
 Attribute attribute = new Attribute("testKey", "testValue", null);
 try {
 attribute.html();
 } catch (SerializationException e) {
 // expected exception
 }
 }

 /**
 * Test html method with non-null parent
 */
 @Test
 public void testHtmlMethodWithNonNullParent() throws IOException {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value, this.attributes);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 Assert.assertNotNull(html);
 Assert.assertTrue(html.contains(key + "=\"" + value + "\""));
 }

 /**
 * Test createFromEncoded method
 */
 @Test
 public void testCreateFromEncodedMethod() {
 String unencodedKey = "testKey";
 String encodedValue = "testValue=\"testValue\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 Assert.assertEquals(unencodedKey, attribute.getKey());
 Assert.assertEquals("testValue", attribute.getValue());
 }

 /**
 * Test isDataAttribute method
 */
 @Test
 public void testIsDataAttributeMethod() {
 String key1 = "testKey";
 String key2 = "data-testKey";
 Attribute attribute1 = new Attribute(key1, null, null);
 Attribute attribute2 = new Attribute(key2, null, null);
 Assert.assertFalse(attribute1.isDataAttribute());
 Assert.assertTrue(attribute2.isDataAttribute());
 }

 /**
 * Test shouldCollapseAttribute method
 */
 @Test
 public void testShouldCollapseAttributeMethod() throws IOException {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value, null);
 Document.OutputSettings out = new Document.OutputSettings();
 Assert.assertFalse(attribute.shouldCollapseAttribute(out));
 }

 /**
 * Test equals method
 */
 @Test
 public void testEqualsMethod() {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute1 = new Attribute(key, value, null);
 Attribute attribute2 = new Attribute(key, value, null);
 Attribute attribute3 = new Attribute("otherKey", value, null);
 Attribute attribute4 = new Attribute(key, null, null);
 Assert.assertTrue(attribute1.equals(attribute1));
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 Assert.assertFalse(attribute1.equals(attribute4));
 }

 /**
 * Test hashCode method
 */
 @Test
 public void testHashCodeMethod() {
 String key1 = "testKey1";
 String key2 = "testKey2";
 String value = "testValue";
 Attribute attribute1 = new Attribute(key1, value, null);
 Attribute attribute2 = new Attribute(key2, value, null);
 Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 /**
 * Test clone method
 */
 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = new Attribute(key, value, null);
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(key, clonedAttribute.getKey());
 Assert.assertEquals(value, clonedAttribute.getValue());
 }

 /**
 * Test isBooleanAttribute method
 */
 @Test
 public void testIsBooleanAttributeMethod() {
 String key1 = "testKey";
 String key2 = "booleanAttribute";
 Attribute attribute1 = new Attribute(key1, null, null);
 Attribute attribute2 = new Attribute(key2, null, null);
 Assert.assertFalse(attribute1.isBooleanAttribute());
 Assert.assertTrue(attribute2.isBooleanAttribute());
 }
}