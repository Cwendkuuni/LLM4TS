import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.helper.Entities;
import java.util.Arrays;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Test
 public void testConstructorWithKeyAndValue() {
 attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertNull(attribute.getParent());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(attributes, attribute.getParent());
 }

 @Test
 public void testSetKey() {
 attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithEmptyKey() {
 attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

 @Test
 public void testSetValue() {
 attribute = new Attribute("key", "value");
 String newValue = "newValue";
 String oldValue = attribute.setValue(newValue);
 Assert.assertEquals(newValue, attribute.getValue());
 Assert.assertEquals("value", oldValue);
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueWithNullValue() {
 attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
 }

 @Test(expected = IOException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute = new Attribute("key", "value");
 throw new IOException();
 }

 @Test
 public void testHtmlWithOutputSettings() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute = new Attribute("key", "value");
 attribute.html(sb, out);
 Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
 }

 @Test
 public void testToString() {
 attribute = new Attribute("key", "value");
 Assert.assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attributeFromEncoded = Attribute.createFromEncoded("key", "key=\"value\"");
 Assert.assertEquals("key", attributeFromEncoded.getKey());
 Assert.assertEquals("value", attributeFromEncoded.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 attribute = new Attribute("data-key", null);
 Assert.assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithNonDataAttribute() {
 attribute = new Attribute("key", null);
 Assert.assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeWithFalse() {
 Document.OutputSettings out = new Document.OutputSettings();
 attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 attribute = new Attribute("checked", null);
 Assert.assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithNonBooleanAttribute() {
 attribute = new Attribute("key", null);
 Assert.assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithNullValue() {
 attribute = new Attribute("checked", null);
 Assert.assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1, attribute1);
 Assert.assertEquals(attribute1, attribute2);
 Assert.assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute1.hashCode());
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}