import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 @Test
 public void testCreation() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 }

 @Test
 public void testCreationWithParent() {
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute("key", "value", parentAttributes);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertSame(parentAttributes, attribute.getParent());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws Exception {
 Appendable appendable = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(appendable, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, appendable.toString());
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals(attribute.html(), attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "%6B%65%78%65%72%6F%63%68%69%70%75%74%65%72");
 assertEquals("key", attribute.getKey());
 assertEquals("boolean", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", null);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", null);
 assertFalse(attribute1.equals(null));
 assertFalse(attribute1.equals(attribute2));
 assertTrue(attribute1.equals(attribute3));
 assertFalse(attribute1.equals(attribute4));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", null);
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 int hashCode4 = attribute4.hashCode();
 assertNotSame(hashCode1, hashCode2);
 assertSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode4);
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals("key", clonedAttribute.getKey());
 assertEquals("value", clonedAttribute.getValue());
 assertSame(attribute.getParent(), clonedAttribute.getParent());
 }
}