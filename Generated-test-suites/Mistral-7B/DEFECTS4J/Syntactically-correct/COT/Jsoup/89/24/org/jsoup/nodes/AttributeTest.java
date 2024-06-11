import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

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
 public void testSetKey() {
 Attribute attribute = createAttribute("key1", "value1");
 attribute.setKey("key2");
 Assert.assertEquals("key2", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = createAttribute("key", "value1");
 attribute.setValue("value2");
 Assert.assertEquals("value2", attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 Assert.assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 Attribute attribute = createAttribute("key", "");
 Assert.assertTrue(attribute.shouldCollapseAttribute(out));
 Attribute booleanAttribute = createAttribute("checked", "");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(out));
 Attribute nonBoolAttribute = createAttribute("key", "nonEmptyValue");
 Assert.assertFalse(nonBoolAttribute.shouldCollapseAttribute(out));
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = createAttribute("key", "value");
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = createAttribute("checked", null);
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = createAttribute("key", "value");
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = createAttribute("data-key", "value");
 Assert.assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = createAttribute("key", "value");
 Assert.assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key1", "value1");
 Attribute attribute3 = createAttribute("key2", "value1");
 Assert.assertTrue(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = createAttribute("key1", "value1");
 Attribute attribute2 = createAttribute("key1", "value1");
 Attribute attribute3 = createAttribute("key2", "value1");
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() {
 Attribute attribute = createAttribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 Assert.assertEquals(attribute, clonedAttribute);
 }
}