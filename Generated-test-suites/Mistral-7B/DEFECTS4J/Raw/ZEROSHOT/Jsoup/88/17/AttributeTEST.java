import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttributeTest {

 @Test
 public void testCreationWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testCreationWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", new Attributes());
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 Assertions.assertNull(attribute.getParent());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assertions.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 String oldValue = attribute.setValue("newValue");
 Assertions.assertEquals("newValue", attribute.getValue());
 Assertions.assertEquals("value", oldValue);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 Assertions.assertEquals(expected, sb.toString());
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 String expected = "key=\"value\"";
 Assertions.assertEquals(expected, attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Assertions.assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", "value");
 Assertions.assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 Assertions.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 Attribute booleanAttribute = new Attribute("checked", "");
 Assertions.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEqualsAndHashcode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assertions.assertTrue(attribute1.equals(attribute2));
 Assertions.assertTrue(attribute1.hashCode() == attribute2.hashCode());
 Assertions.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assertions.assertEquals(attribute, clonedAttribute);
 }
}