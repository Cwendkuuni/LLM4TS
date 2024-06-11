import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = createAttribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setKey("newKey");
 Attribute newAttribute = createAttribute("newKey", null);
 Assert.assertEquals(newAttribute, attribute);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value");
 String oldValue = attribute.getValue();
 attribute.setValue("newValue");
 Assert.assertNotEquals(oldValue, attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = createAttribute("key", "value");
 attribute.setValue(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetValueWithParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 attribute.setValue("newValue");
 }

 @Test
 public void testGetHtml() throws Exception {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testToString() {
 Attribute attribute = createAttribute("key", "value");
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
 Attribute booleanAttribute = createAttribute("booleanAttribute", null);
 Attribute dataAttribute = createAttribute("data:test", null);
 Assert.assertFalse(booleanAttribute.isDataAttribute());
 Assert.assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws Exception {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute1 = createAttribute("booleanAttribute", "");
 Assert.assertTrue(attribute1.shouldCollapseAttribute(out));
 Attribute attribute2 = createAttribute("booleanAttribute", "booleanAttribute");
 Assert.assertTrue(attribute2.shouldCollapseAttribute(out));
 Attribute attribute3 = createAttribute("booleanAttribute", "false");
 Assert.assertFalse(attribute3.shouldCollapseAttribute(out));
 Attribute attribute4 = createAttribute("booleanAttribute", "true");
 Assert.assertFalse(attribute4.shouldCollapseAttribute(out));
 Attribute attribute5 = createAttribute("data:test", null);
 Assert.assertTrue(attribute5.shouldCollapseAttribute(out));
 Attribute attribute6 = createAttribute("data:test", "test");
 Assert.assertFalse(attribute6.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = createAttribute("booleanAttribute", null);
 Assert.assertFalse(booleanAttribute.isBooleanAttribute());
 Attribute checkBoxAttribute = createAttribute("checked", null);
 Assert.assertTrue(checkBoxAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key2", "value2");
 Attribute attribute3 = createAttribute("key1", "value1");
 Attribute attribute4 = createAttribute("key1", "value2");
 Attribute attribute5 = createAttribute("key1", null);
 Attribute attribute6 = attribute1;
 Attribute attribute7 = new Attribute("key1", "value1");
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put("key1", attribute1);
 Attribute attribute8 = attributes.get("key1");
 Assert.assertFalse(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 Assert.assertFalse(attribute1.equals(attribute4));
 Assert.assertFalse(attribute1.equals(attribute5));
 Assert.assertTrue(attribute1.equals(attribute6));
 Assert.assertTrue(attribute1.equals(attribute7));
 Assert.assertTrue(attribute1.equals(attribute8));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key2", "value2");
 Attribute attribute3 = createAttribute("key1", "value1");
 Attribute attribute4 = createAttribute("key1", "value2");
 Attribute attribute5 = createAttribute("key1", null);
 Attribute attribute6 = attribute1;
 Attribute attribute7 = new Attribute("key1", "value1");
 Map<String, Attribute> attributes = new HashMap<>();
 attributes.put("key1", attribute1);
 Attribute attribute8 = attributes.get("key1");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 int hashCode4 = attribute4.hashCode();
 int hashCode5 = attribute5.hashCode();
 int hashCode6 = attribute6.hashCode();
 int hashCode7 = attribute7.hashCode();
 int hashCode8 = attribute8.hashCode();
 Assert.assertEquals(hashCode1, hashCode6);
 Assert.assertEquals(hashCode1, hashCode7);
 Assert.assertEquals(hashCode1, hashCode8);
 Assert.assertNotEqual(hashCode1, hashCode2);
 Assert.assertNotEqual(hashCode1, hashCode3);
 Assert.assertNotEqual(hashCode1, hashCode4);
 Assert.assertNotEqual(hashCode1, hashCode5);
 }

 @Test
 public void testClone() {
 Attribute attribute = createAttribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}