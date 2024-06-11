import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Arrays;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testAttributeCreation() {
 // Test creating a new attribute with raw key and value
 Attribute attribute = createAttribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());

 // Test creating a new attribute with raw key, value and parent
 Attribute attributeParent = new Attributes();
 Attribute attributeWithParent = createAttribute("key", "value", attributeParent);
 Assert.assertEquals("key", attributeWithParent.getKey());
 Assert.assertEquals("value", attributeWithParent.getValue());
 Assert.assertEquals(attributeWithParent, attributeParent.put("key", "value"));
 }

 @Test
 public void testAttributeSetKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = AssertionError.class)
 public void testAttributeSetKeyNull() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testAttributeSetValue() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = AssertionError.class)
 public void testAttributeSetValueNull() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testAttributeHTML() throws IOException {
 String key = "testKey";
 String value = "testValue";
 Attribute attribute = createAttribute(key, value);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("<" + key + "=\"" + value + "\"/>", html);
 }

 @Test(expected = SerializationException.class)
 public void testAttributeHTMLIOException() throws IOException {
 Attribute attribute = createAttribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testAttributeIsDataAttribute() {
 Attribute attribute = createAttribute("testData", null);
 Assert.assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testAttributeIsDataAttributeFalse() {
 Attribute attribute = createAttribute("test", null);
 Assert.assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testAttributeShouldCollapseAttribute() {
 Attribute attribute = createAttribute("test", null);
 Assert.assertFalse(attribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testAttributeShouldCollapseAttributeTrue() {
 String key = "test";
 Attribute attribute = createAttribute(key, key);
 Assert.assertTrue(attribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testAttributeIsBooleanAttribute() {
 Attribute attribute = createAttribute("testBoolean", null);
 Assert.assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testAttributeIsBooleanAttributeTrue() {
 String key = "allowfullscreen";
 Attribute attribute = createAttribute(key, null);
 Assert.assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testAttributeEquals() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key1", "value1");
 Attribute attribute3 = createAttribute("key2", "value2");
 Assert.assertEquals(attribute1, attribute1);
 Assert.assertEquals(attribute1, attribute2);
 Assert.assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testAttributeClone() throws CloneNotSupportedException {
 Attribute attribute = createAttribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}