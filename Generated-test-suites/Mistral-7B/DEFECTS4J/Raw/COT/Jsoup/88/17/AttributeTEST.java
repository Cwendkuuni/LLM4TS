import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

 private Attribute createAttribute(String key, String val) {
 return new Attribute(key, val, null);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = createAttribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyAndValueAndParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyConstructor() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueConstructor() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyConstructor() {
 Attribute attribute = new Attribute("", "value");
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testGetValue() {
 Attribute attribute = createAttribute("key", "value");
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value");
 String oldValue = attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 Assert.assertNull(oldValue);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb).toString());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlMethodWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings()); // IOException intentionally not caught
 }

 @Test(expected = SerializationException.class)
 public void testHtmlMethodWithSerializationException() {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 throw new SerializationException(new IOException());
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = createAttribute("key", "value");
 Assert.assertEquals(attribute.html(), attribute.toString());
 }

 @Test
 public void testCreateFromEncodedMethod() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullEncodedKeyCreateFromEncodedMethod() {
 Attribute.createFromEncoded(null, "");
 }

 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("data-key", null);
 Assert.assertTrue(attribute.isDataAttribute());
 Attribute attribute1 = new Attribute("key", null);
 Assert.assertFalse(attribute1.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Assert.assertTrue(Attribute.shouldCollapseAttribute("booleanAttribute", null, outputSettings));
 Assert.assertTrue(Attribute.shouldCollapseAttribute("booleanAttribute", "", outputSettings));
 Assert.assertTrue(Attribute.shouldCollapseAttribute("booleanAttribute", "booleanAttribute", outputSettings));
 Assert.assertFalse(Attribute.shouldCollapseAttribute("nonBooleanAttribute", null, outputSettings));
 Assert.assertFalse(Attribute.shouldCollapseAttribute("nonBooleanAttribute", "", outputSettings));
 Assert.assertFalse(Attribute.shouldCollapseAttribute("nonBooleanAttribute", "nonBooleanAttribute", outputSettings));
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
 Assert.assertTrue(Attribute.isBooleanAttribute("disabled"));
 Assert.assertFalse(Attribute.isBooleanAttribute("key"));
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1, attribute1);
 Assert.assertEquals(attribute1, attribute2);
 Assert.assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute1.hashCode());
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}