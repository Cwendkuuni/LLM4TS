import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Arrays;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value, null);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyAndValueAndParent() {
 Attribute attribute = createAttribute("key", "value", new Attributes());
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullKey() {
 Attribute attribute = new Attribute(null, "value");
 fail("Should have thrown IllegalArgumentException with null key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyKey() {
 Attribute attribute = new Attribute("", "value");
 fail("Should have thrown IllegalArgumentException with empty key");
 }

 @Test
 public void testGetKey() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = createAttribute("key1", "value");
 attribute.setKey("key2");
 assertEquals("key2", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = createAttribute("key1", "value");
 attribute.setKey(null);
 fail("Should have thrown IllegalArgumentException with null key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithEmptyKey() {
 Attribute attribute = createAttribute("key1", "value");
 attribute.setKey("");
 fail("Should have thrown IllegalArgumentException with empty key");
 }

 @Test
 public void testGetValue() {
 Attribute attribute = createAttribute("key", "value");
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value1");
 attribute.setValue("value2");
 assertEquals("value2", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = createAttribute("key", "value1");
 attribute.setValue(null);
 fail("Should have thrown IllegalArgumentException with null value");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = createAttribute("data-key", null);
 assertTrue(attribute.isDataAttribute());
 Attribute attribute2 = createAttribute("key", null);
 assertFalse(attribute2.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = createAttribute("key", "value");
 assertFalse(attribute.shouldCollapseAttribute(out));
 Attribute booleanAttribute = createAttribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = createAttribute("checked", null);
 assertTrue(attribute.isBooleanAttribute());
 Attribute attribute2 = createAttribute("key", null);
 assertFalse(attribute2.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithBooleanValue() {
 Attribute attribute = createAttribute("checked", "true");
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key1", "value1");
 Attribute attribute3 = createAttribute("key2", "value2");
 assertTrue(attribute1.equals(attribute2));
 assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key1", "value1");
 Attribute attribute3 = createAttribute("key2", "value2");
 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();
 assertEquals(hashCode1, hashCode2); // they should have the same hashcode since they're equal
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = createAttribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}