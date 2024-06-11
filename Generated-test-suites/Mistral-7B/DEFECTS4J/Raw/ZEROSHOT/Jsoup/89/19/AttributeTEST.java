import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute("key", "value", parent);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(attribute, parent.get("key"));
 }

 @Test
 public void testAttributeKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeKeySetterNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testAttributeValueSetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeValueSetterNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testAttributeHtml() throws Exception {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("key=\"value\"", html);
 }

 @Test
 public void testAttributeToString() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testAttributeCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 Assert.assertTrue(attribute.isDataAttribute());
 Attribute attribute2 = new Attribute("key", null);
 Assert.assertFalse(attribute2.isDataAttribute());
 }

 @Test
 public void testAttributeShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = new Attribute("checked", null);
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 Attribute attribute2 = new Attribute("key", "value");
 Assert.assertFalse(attribute2.shouldCollapseAttribute(out));
 }

 @Test
 public void testAttributeIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 Assert.assertTrue(attribute.isBooleanAttribute());
 Attribute attribute2 = new Attribute("key", "value");
 Assert.assertFalse(attribute2.isBooleanAttribute());
 }

 @Test
 public void testAttributeEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Assert.assertFalse(attribute1.equals(attribute2));
 Assert.assertTrue(attribute1.equals(attribute3));
 }

 @Test
 public void testAttributeClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clone = attribute.clone();
 Assert.assertEquals(attribute, clone);
 }

}