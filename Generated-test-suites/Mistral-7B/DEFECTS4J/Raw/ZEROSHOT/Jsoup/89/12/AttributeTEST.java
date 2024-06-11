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
 public void testKeySetting() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeySetting() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testValueSetting() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueSetting() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = new StringBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals(attribute.toString(), attribute.html());
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "data-my-attribute";
 String encodedValue = "test-value";
 Attribute attribute = Attribute.createFromEncoded("my-attribute", encodedValue);
 assertEquals("my-attribute", attribute.getKey());
 assertEquals("test-value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "my-data-attribute", "value");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("key", "value");
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 assertTrue(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute2));
 }

 @Test
 public void testHashCodeMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 assertEquals(attribute1.hashCode(), attribute3.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 @Test
 public void testCloneMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}