import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute("key", "value", parentAttributes);
 Assertions.assertEquals("key", attribute.getKey());
 Assertions.assertEquals("value", attribute.getValue());
 Assertions.assertEquals(attribute, parentAttributes.get("key"));
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
 String newValue = "newValue";
 attribute.setValue(newValue);
 Assertions.assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String htmlOutput = StringUtil.releaseBuilder(sb);
 Assertions.assertEquals("<key>value</key>", htmlOutput);
 }

 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("data-key", null);
 Assertions.assertTrue(attribute.isDataAttribute());
 Attribute attribute1 = new Attribute("key", null);
 Assertions.assertFalse(attribute1.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 Assertions.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 Attribute booleanAttribute = new Attribute("checked", null);
 Assertions.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 Attribute attribute = new Attribute("checked", null);
 Assertions.assertTrue(attribute.isBooleanAttribute());
 Attribute attribute1 = new Attribute("key", "value");
 Assertions.assertFalse(attribute1.isBooleanAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assertions.assertTrue(attribute1.equals(attribute2));
 Assertions.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assertions.assertEquals(attribute, clonedAttribute);
 }
}