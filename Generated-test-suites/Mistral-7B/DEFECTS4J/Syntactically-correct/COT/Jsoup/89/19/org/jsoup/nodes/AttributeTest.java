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
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyAndValueAndParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testGetKey() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
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

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("<key>value</key>", html);
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 throw new IOException("IOException");
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = createAttribute("key", "value");
 assertFalse(attribute.isDataAttribute());
 attribute.setKey("data-test");
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = createAttribute("key", "value");
 assertFalse(attribute.isBooleanAttribute());
 attribute.setKey("checked");
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = createAttribute("key", "");
 assertTrue(attribute.shouldCollapseAttribute(out));
 attribute.setKey("key");
 assertTrue(attribute.shouldCollapseAttribute(out));
 out.setSyntax(Document.OutputSettings.Syntax.xml);
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key1", "value1");
 Attribute attribute3 = createAttribute("key2", "value2");
 Attribute attribute4 = attribute1;
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute4));
 }

 @Test
 public void testClone() {
 Attribute attribute = createAttribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }

}