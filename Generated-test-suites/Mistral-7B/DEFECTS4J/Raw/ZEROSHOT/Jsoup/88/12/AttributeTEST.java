import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import java.util.Arrays;

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

 @Test
 public void testKeySetter() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testValueSetter() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 Assert.assertEquals(newValue, attribute.getValue());
 Assert.assertEquals(oldValue, attribute.parent.get("key")); // check if parent was updated
 }

 @Test
 public void testHtmlMethod() {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 try {
 attribute.html(sb, new Document("").outputSettings());
 } catch (IOException e) {
 e.printStackTrace();
 }
 String html = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("<key value=\"value\"></key>", html);
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("<key value=\"value\"></key>", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedValue = "&amp;key=value";
 Attribute attribute = Attribute.createFromEncoded("key", encodedValue);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", null);
 Attribute nonDataAttribute = new Attribute("key", null);
 Assert.assertTrue(dataAttribute.isDataAttribute());
 Assert.assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
 Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute nonBooleanAttribute = new Attribute("key", null);
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithValue() {
 Attribute booleanAttribute = new Attribute("checked", "");
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", "value1");
 Attribute attribute5 = attribute1;
 Assert.assertNotEquals(attribute1, attribute2);
 Assert.assertEquals(attribute3, attribute4);
 Assert.assertEquals(attribute1, attribute5);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", "value1");
 Attribute attribute5 = attribute1;
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 int hashCode4 = attribute4.hashCode();
 int hashCode5 = attribute5.hashCode();
 Assert.assertNotEquals(hashCode1, hashCode2);
 Assert.assertEquals(hashCode3, hashCode4);
 Assert.assertEquals(hashCode1, hashCode5);
 }

 @Test
 public void testClone() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}