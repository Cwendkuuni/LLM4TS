import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setup() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testConstructor() {
 // Test creating a new attribute with unencoded key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 // Test creating a new attribute with unencoded key, value, and parent
 Attributes parent = new Attributes();
 Attribute attributeWithParent = new Attribute("key", "value", parent);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertEquals(parent, attributeWithParent.getParent());
 }

 @Test
 public void testSetKey() {
 // Test setting the key with a new value
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());

 // Test setting the key with the same value
 attribute.setKey("key");
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 // Test setting the value with a new value
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());

 // Test setting the value with the same value
 attribute.setValue("value");
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testHtml() throws Exception {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testToString() {
 String expected = "key=\"value\"";
 assertEquals(expected, attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "key=value";
 Attribute attribute = Attribute.createFromEncoded("key", encodedKey);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", "value");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws Exception {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 assertTrue(attribute.shouldCollapseAttribute(outputSettings, ""));
 assertTrue(attribute.shouldCollapseAttribute(outputSettings, "value"));
 assertTrue(attribute.shouldCollapseAttribute(outputSettings, "key"));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithValue() {
 Attribute booleanAttributeWithValue = new Attribute("checked", "checked");
 assertTrue(booleanAttributeWithValue.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attributeWithSameKeyAndValue = new Attribute("key", "value");
 Attribute attributeWithDifferentKeyAndValue = new Attribute("differentKey", "value");
 Attribute attributeWithSameKeyAndDifferentValue = new Attribute("key", "differentValue");
 Attribute attributeWithSameParent = new Attribute("key", "value", attribute);

 assertTrue(attribute.equals(attribute));
 assertTrue(attribute.equals(attributeWithSameKeyAndValue));
 assertTrue(attribute.equals(attributeWithSameParent));
 assertFalse(attribute.equals(attributeWithDifferentKeyAndValue));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");

 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();

 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}