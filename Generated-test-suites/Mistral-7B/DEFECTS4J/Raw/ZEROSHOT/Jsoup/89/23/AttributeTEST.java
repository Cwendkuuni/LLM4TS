import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

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
 Assert.assertNotNull(attribute.parent);
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyThrowsException() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValueThrowsException() {
 Attribute attribute = new Attribute("key", null);
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
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isDataAttribute());

 Attribute dataAttribute = new Attribute("data-key", "value");
 Assert.assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws Exception {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("boolean", "");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));

 Attribute stringAttribute = new Attribute("string", "value");
 Assert.assertFalse(stringAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("boolean", "");
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());

 Attribute stringAttribute = new Attribute("string", null);
 Assert.assertFalse(stringAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeHtml5() {
 Attribute booleanAttribute = new Attribute("allowfullscreen", null);
 Assert.assertTrue(Attribute.isBooleanAttribute(booleanAttribute.getKey()));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertTrue(attribute1.equals(attribute1));
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertNotSame(attribute, clonedAttribute);
 Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
 Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}