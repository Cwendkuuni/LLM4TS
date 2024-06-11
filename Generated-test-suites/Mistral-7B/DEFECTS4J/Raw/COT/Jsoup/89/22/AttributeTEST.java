import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testCreateAttributeWithKeyAndValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithKeyAndValueAndParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateAttributeWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 fail("Should not create an Attribute with a null key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateAttributeWithEmptyKey() {
 Attribute attribute = new Attribute("", "value");
 fail("Should not create an Attribute with an empty key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateAttributeWithNullValue() {
 Attribute attribute = new Attribute("key", null);
 fail("Should not create an Attribute with a null value");
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key1", "value1");
 attribute.setKey("key2");
 assertEquals("key2", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = createAttribute("key1", "value1");
 attribute.setKey(null);
 fail("Should not set key to null");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithEmptyKey() {
 Attribute attribute = createAttribute("key1", "value1");
 attribute.setKey("");
 fail("Should not set key to an empty string");
 }

 @Test
 public void testGetKey() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testGetValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value1");
 attribute.setValue("value2");
 assertEquals("value2", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = createAttribute("key", "value1");
 attribute.setValue(null);
 fail("Should not set value to null");
 }

 @Test
 public void testHtml() throws IOException {
 Attribute attribute = createAttribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).trim());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 Attribute attribute = createAttribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 throw new IOException();
 attribute.html(sb, new Document("").outputSettings());
 fail("Should throw SerializationException when IOException is thrown");
 }

 @Test
 public void testToString() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFromEncodedWithNullUnencodedKey() {
 Attribute.createFromEncoded(null, "key=\"value\"");
 fail("Should not create Attribute from encoded value with null unencoded key");
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", "value");
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeFalse() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(new Attribute("checked", "").shouldCollapseAttribute(out));
 assertTrue(new Attribute("checked", "checked").shouldCollapseAttribute(out));
 assertTrue(new Attribute("checked", "").shouldCollapseAttribute(out, Document.OutputSettings.Syntax.xml));
 assertFalse(new Attribute("checked", "checked").shouldCollapseAttribute(out, Document.OutputSettings.Syntax.xml));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 assertTrue(attribute.isBooleanAttribute());
 attribute.setKey("disabled");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testClone() {
 Attribute original = new Attribute("key", "value");
 Attribute clone = original.clone();
 assertEquals("key", clone.getKey());
 assertEquals("value", clone.getValue());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value1");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value1");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}