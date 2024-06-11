import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 String key = "id";
 String value = "element1";
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(key, value, parent);
 assertEquals(key, attribute.getKey());
 assertEquals(value, attribute.getValue());
 assertEquals(attribute, parent.get(key));
 }

 @Test
 public void testSetKey() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);
 attribute.setKey("style");
 assertEquals("style", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attribute = new Attribute("", "value");
 }

 @Test
 public void testSetValue() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testHtml() throws IOException {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("class=\"active\"", sb.toString());
 }

 @Test
 public void testHtmlWithEmptyValue() throws IOException {
 String key = "class";
 Attribute attribute = new Attribute(key, "");
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("class", sb.toString());
 }

 @Test
 public void testIsBooleanAttribute() {
 String key = "class";
 assertTrue(Attribute.isBooleanAttribute(key) == false);
 }

 @Test
 public void testIsBooleanAttributeWithBooleanKey() {
 String key = "checked";
 assertTrue(Attribute.isBooleanAttribute(key) == true);
 }

 @Test
 public void testClone() {
 String key = "class";
 String value = "active";
 Attribute original = new Attribute(key, value);
 Attribute clone = original.clone();
 assertEquals(original, clone);
 }

 @Test
 public void testEquals() {
 String key1 = "class";
 String value1 = "active";
 String key2 = "class";
 String value2 = "active";
 Attribute attribute1 = new Attribute(key1, value1);
 Attribute attribute2 = new Attribute(key2, value2);
 Attribute attribute3 = new Attribute(key1, value1);
 assertTrue(attribute1.equals(attribute1));
 assertTrue(attribute1.equals(attribute3));
 assertTrue(attribute1.equals(attribute2));
 }

 @Test
 public void testNotEqualsDifferentObjects() {
 String key1 = "class";
 String value1 = "active";
 String key2 = "id";
 String value2 = "element1";
 Attribute attribute1 = new Attribute(key1, value1);
 Attribute attribute2 = new Attribute(key2, value2);
 assertFalse(attribute1.equals(attribute2));
 }

 @Test
 public void testNotEqualsDifferentValues() {
 String key = "class";
 String value1 = "active";
 String value2 = "inactive";
 Attribute attribute = new Attribute(key, value1);
 assertFalse(attribute.equals(new Attribute(key, value2)));
 }
}