import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertNull(attribute.getParent());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute("key", "value", parent);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(parent, attribute.getParent());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testToString() {
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
 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "key", "value");
 Assert.assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.shouldCollapseAttribute(out));
 out.syntax(Document.OutputSettings.Syntax.html);
 attribute = new Attribute("key", "");
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 attribute = new Attribute("booleanAttribute", null); // booleanAttributes contain "booleanAttribute"
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isBooleanAttribute());
 attribute = new Attribute("booleanAttribute", null); // booleanAttributes contain "booleanAttribute"
 Assert.assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttributeWithPrefix() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.isDataAttribute("data:"));
 attribute = new Attribute("data:key", "value");
 Assert.assertTrue(attribute.isDataAttribute("data:"));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", "value2");
 Attribute attribute5 = new Attribute("key1", null);
 Attribute attribute6 = new Attribute("key1", "value1");
 Attribute attribute7 = attribute3;
 Assert.assertFalse(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(null));
 Assert.assertFalse(attribute1.equals(this));
 Assert.assertFalse(attribute1.equals(new Object()));
 Assert.assertTrue(attribute1.equals(attribute3));
 Assert.assertTrue(attribute1.equals(attribute6));
 Assert.assertFalse(attribute1.equals(attribute4));
 Assert.assertFalse(attribute1.equals(attribute5));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");
 Attribute attribute4 = new Attribute("key1", "value2");
 Attribute attribute5 = new Attribute("key1", null);
 Attribute attribute6 = new Attribute("key1", "value1");
 Attribute attribute7 = attribute3;
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 int hashCode4 = attribute4.hashCode();
 int hashCode5 = attribute5.hashCode();
 int hashCode6 = attribute6.hashCode();
 int hashCode7 = attribute7.hashCode();
 Assert.assertEquals(hashCode1, hashCode3);
 Assert.assertEquals(hashCode1, hashCode6);
 Assert.assertNotEquals(hashCode1, hashCode2);
 Assert.assertNotEquals(hashCode1, hashCode4);
 Assert.assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clone = attribute.clone();
 Assert.assertNotNull(clone);
 Assert.assertEquals(attribute, clone);
 Assert.assertNotSame(attribute, clone);
 }
}