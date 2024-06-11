import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit. Assert;
import org.junit.Test;

public class AttributeTest {

 @Test
 public void testAttributeConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", null);
 Attribute parent = new Attributes(); // Assuming Attributes is a class that can be instantiated
 attribute.setParent(parent);
 parent.put(attribute);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(attribute, parent.get("key"));
 }

 @Test
 public void testSetKeyMethod() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSetValueMethod() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String htmlOutput = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("<key=\"value\">", htmlOutput);
 }

 @Test
 public void testCreateFromEncodedMethod() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlMethodWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, null);
 }

 @Test
 public void testIsDataAttributeMethod() {
 Attribute attribute = new Attribute("data-key", "value");
 Assert.assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeMethodWithNonDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethodWithBooleanAttributeAndEmptyValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Assert.assertTrue(new Attribute("checked", "").shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeMethodWithBooleanAttributeAndValueSameAsKey() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Assert.assertTrue(new Attribute("checked", "checked").shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeMethodWithBooleanAttributeAndValueDifferentFromKey() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Assert.assertFalse(new Attribute("checked", "notChecked").shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttributeDeprecatedMethod() {
 Assert.assertFalse(new Attribute("key", "value").isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeStaticMethod() {
 Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertTrue(attribute1.equals(attribute1));
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute1.hashCode());
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testCloneMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
 Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}