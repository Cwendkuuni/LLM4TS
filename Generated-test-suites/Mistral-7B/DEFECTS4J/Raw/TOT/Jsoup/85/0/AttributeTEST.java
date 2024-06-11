import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attributes attributes;

 @Before
 public void setup() {
 attributes = new Attributes();
 }

 @Test
 public void testAttributeCreation() {
 // Test creating a new Attribute with unencoded key and value
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());

 // Test creating a new Attribute with unencoded key, value, and parent
 Attribute attributeWithParent = new Attribute("key", "value", attributes);
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertEquals(attributes, attributeWithParent.parent);
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSettingEmptyKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String result = sb.toString();
 assertEquals("<key value=\"value\"></key>", result);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(Attribute.isDataAttribute("data-key"));
 assertFalse(Attribute.isDataAttribute("key"));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "");
 assertTrue(attribute.shouldCollapseAttribute(out));
 Attribute attributeWithValue = new Attribute("key", "value");
 assertFalse(attributeWithValue.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(Attribute.isBooleanAttribute("checked"));
 assertFalse(Attribute.isBooleanAttribute("key"));
 }

 @Test
 public void testEqualsMethod() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}