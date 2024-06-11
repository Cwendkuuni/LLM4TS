import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

 private Attribute createAttribute(String key, String value) {
 return new Attribute(key, value);
 }

 @Test
 public void testAttributeCreationUnencodedKeyAndValue() {
 String key = "class";
 String value = "test";
 Attribute attribute = createAttribute(key, value);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 }

 @Test
 public void testAttributeCreationUnencodedKeyAndValueWithParent() {
 String key = "class";
 String value = "test";
 Attributes parent = new Attributes();
 Attribute attribute = createAttribute(key, value, parent);
 parent.put(attribute);
 Assert.assertEquals(key, attribute.getKey());
 Assert.assertEquals(value, attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationNullKey() {
 Attribute attribute = createAttribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeCreationEmptyKey() {
 Attribute attribute = createAttribute("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeCreationEmptyValue() {
 Attribute attribute = createAttribute("key", "");
 }

 @Test
 public void testAttributeCreationEncodedKeyAndValue() {
 String encodedKey = "class=\"test\"";
 String decodedValue = "test";
 Attribute attribute = Attribute.createFromEncoded(encodedKey.substring(1, encodedKey.length() - 1), decodedValue);
 Assert.assertEquals("class", attribute.getKey());
 Assert.assertEquals(decodedValue, attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-test", null);
 Assert.assertTrue(attribute.isDataAttribute());
 Attribute attribute2 = new Attribute("class", null);
 Assert.assertFalse(attribute2.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = new Attribute("class", "test");
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 Attribute attribute2 = new Attribute("checked", null);
 Assert.assertTrue(attribute2.shouldCollapseAttribute(out));
 Attribute attribute3 = new Attribute("data-test", "test");
 Assert.assertFalse(attribute3.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("checked", null);
 Assert.assertTrue(attribute.isBooleanAttribute());
 Attribute attribute2 = new Attribute("class", null);
 Assert.assertFalse(attribute2.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("class", "test");
 Attribute attribute2 = new Attribute("class", "test");
 Attribute attribute3 = new Attribute("data-test", "test");
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("class", "test");
 Attribute attribute2 = new Attribute("class", "test");
 Attribute attribute3 = new Attribute("data-test", "test");
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("class", "test");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}