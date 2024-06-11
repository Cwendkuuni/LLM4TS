import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Arrays;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testCreateAttribute() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attributes parent = new Attributes();
 Attribute attribute = createAttribute("key", "value", parent);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attribute, parent.get(attribute.getKey()));
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value");
 String oldValue = attribute.getValue();
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 assertEquals(oldValue, attribute.parent.get(attribute.getKey()));
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertTrue(html.contains("key=\"" + "value" + "\""));
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException, SerializationException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, null);
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = createAttribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 Attribute attribute2 = createAttribute("key", null);
 assertFalse(attribute2.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = createAttribute("key", "key");
 assertTrue(attribute.shouldCollapseAttribute(out));
 Attribute attribute2 = createAttribute("key", "value");
 assertFalse(attribute2.shouldCollapseAttribute(out));
 Attribute booleanAttribute = createAttribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = createAttribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = createAttribute("key", null);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key2", "value2");
 Attribute attribute3 = createAttribute("key1", "value1");
 Attribute attribute4 = createAttribute("key1", null);
 Attribute attribute5 = attribute1;
 assertTrue(attribute1.equals(attribute5));
 assertFalse(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute4));
 }

 @Test
 public void testClone() {
 Attribute attribute = createAttribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneException() {
 Attribute attribute = createAttribute("key", "value");
 try {
 Attribute clonedAttribute = (Attribute) attribute.clone();
 } catch (CloneNotSupportedException e) {
 }
 }

}