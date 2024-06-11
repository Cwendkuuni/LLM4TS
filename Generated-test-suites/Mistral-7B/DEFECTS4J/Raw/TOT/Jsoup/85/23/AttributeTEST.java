import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes.OutputSettings;

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
 Assert.assertNotNull(attribute.parent);
 }

 @Test
 public void testAttributeKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
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

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeValueSetterNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testAttributeHtml() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals("key=\"value\"", sb.toString());
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
 public void testAttributeIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", "");
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testAttributeShouldCollapseAttribute() {
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 Attribute booleanAttribute = new Attribute("checked", "");
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testAttributeEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testAttributeClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }

 @Test
 public void testAttributeIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", "");
 Attribute nonDataAttribute = new Attribute("key", "value");
 Assert.assertTrue(dataAttribute.isDataAttribute());
 Assert.assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testElementWithAttribute() throws IOException {
 Element element = new Element("div");
 element.attr("key", "value");
 Attribute attribute = element.attribute("key");
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testElementWithDataAttribute() throws IOException {
 Element element = new Element("div");
 element.data("key", "value");
 Attribute attribute = element.attribute("data-key");
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testElementWithDuplicateAttribute() throws IOException {
 Element element = new Element("div");element.attr("key", "value1");
 element.attr("key", "value2");
 Attribute attribute = element.attribute("key");
 Assert.assertEquals("value2", attribute.getValue());
 }

 @Test
 public void testElementWithEmptyAttribute() throws IOException {
 Element element = new Element("div");
 element.attr("key", "");
 Attribute attribute = element.attribute("key");
 Assert.assertNull(attribute);
 }

 @Test
 public void testElementWithNullAttribute() throws IOException {
 Element element = new Element("div");
 element.attr("key", null);
 Attribute attribute = element.attribute("key");
 Assert.assertNull(attribute);
 }
}