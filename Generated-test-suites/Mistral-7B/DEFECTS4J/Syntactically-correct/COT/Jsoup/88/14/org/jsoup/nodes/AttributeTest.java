import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Map;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testGetValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = sb.toString();
 assertEquals("<key=\"value\">", html);
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 throw new IOException();
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testToString() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("<key=\"value\">", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", "value");
 assertTrue(dataAttribute.isDataAttribute());
 Attribute notDataAttribute = new Attribute("key", "value");
 assertFalse(notDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 Attribute notBooleanAttribute = new Attribute("key", "value");
 assertFalse(notBooleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute notBooleanAttribute = new Attribute("key", "value");
 assertFalse(notBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Attribute attribute4 = new Attribute("key1", "value1");
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute4));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Attribute attribute4 = new Attribute("key1", "value1");
 int hashcode1 = attribute1.hashCode();
 int hashcode2 = attribute2.hashCode();
 int hashcode3 = attribute3.hashCode();
 int hashcode4 = attribute4.hashCode();
 assertTrue(hashcode1 == hashcode2);
 assertFalse(hashcode1 == hashcode3);
 assertFalse(hashcode1 == hashcode4);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}