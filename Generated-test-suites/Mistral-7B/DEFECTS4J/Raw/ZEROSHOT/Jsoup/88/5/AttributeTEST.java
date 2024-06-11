import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");

 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";

 assertEquals("value", attribute.getValue());
 attribute.setValue(newValue);
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());

 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test(expected = SerializationException.class)
 public void testIOExceptionDuringHtml() throws IOException, SerializationException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 throw new IOException();
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeFalse() {
 Attribute attribute = new Attribute("key", null);
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);

 Attribute attribute = new Attribute("booleanAttribute", null);
 assertTrue(attribute.shouldCollapseAttribute(out));

 attribute.setValue("false");
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("booleanAttribute", null);
 assertFalse(attribute.isBooleanAttribute());

 attribute.setKey("allowfullscreen");
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeFalse() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");

 assertEquals(attribute1, attribute3);
 assertNotEquals(attribute1, attribute2);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");

 assertEquals(attribute1.hashCode(), attribute3.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();

 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}