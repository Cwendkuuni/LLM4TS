import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 @Test
 public void testCreateAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("key", "value", attributes);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(attribute, attributes.put("key", "value"));
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("key=\"value\"", html);
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 Attribute notDataAttribute = new Attribute("key", null);
 Assert.assertTrue(attribute.isDataAttribute());
 Assert.assertFalse(notDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(out));
 Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1, attribute2);
 Assert.assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneException() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = (Attribute) attribute.clone();
 }
}