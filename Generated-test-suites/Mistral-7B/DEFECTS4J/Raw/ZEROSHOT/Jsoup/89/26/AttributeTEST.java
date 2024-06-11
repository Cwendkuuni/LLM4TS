import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertNotNull(attribute);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute("key", "value", parent);
 assertNotNull(attribute);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertEquals(attribute, parent.get("key"));
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testGetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("booleanAttribute", null);
 assertTrue(attribute.isBooleanAttribute());
 attribute = new Attribute("key", "value");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 attribute = new Attribute("key", "value");
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setKey("newKey");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("");
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("value");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "data-key=\"newKey\"";
 Attribute attribute = Attribute.createFromEncoded("key", encodedKey);
 assertEquals("newKey", attribute.getKey());
 assertEquals(null, attribute.getValue());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 assertTrue(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute2));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 assertEquals(attribute1.hashCode(), attribute3.hashCode());
 assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}