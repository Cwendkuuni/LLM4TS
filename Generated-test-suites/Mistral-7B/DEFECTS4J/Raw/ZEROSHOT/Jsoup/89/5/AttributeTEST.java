import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attribute;
import java.util.Arrays;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute("key", "value", parentAttributes);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(attribute, parentAttributes.put("key", "value"));
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws Exception {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 Assert.assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeFalse() {
 Attribute attribute = new Attribute("key", null);
 Assert.assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Attribute attribute = new Attribute("checked", "");
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeFalse() {
 Attribute attribute = new Attribute("key", "value");
 Document.OutputSettings out = new Document.OutputSettings();
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 Assert.assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeFalse() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute a = new Attribute("key1", "value1");
 Attribute b = new Attribute("key1", "value1");
 Attribute c = new Attribute("key2", "value2");
 Assert.assertEquals(a, a);
 Assert.assertEquals(a, a);
 Assert.assertEquals(a, b);
 Assert.assertNotEquals(a, c);
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}