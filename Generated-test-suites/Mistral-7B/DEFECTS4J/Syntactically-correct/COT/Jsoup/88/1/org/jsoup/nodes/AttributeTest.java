import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 private static final String BOOLEAN_ATTRIBUTE_KEY = "checked";
 private static final String NON_BOOLEAN_ATTRIBUTE_KEY = "href";
 private static final String BOOLEAN_VALUE = "true";
 private static final String NON_BOOLEAN_VALUE = "index.html";
 private static final String EMPTY_STRING = "";
 private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings();

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Assert.assertEquals(BOOLEAN_ATTRIBUTE_KEY, attribute.getKey());
 Assert.assertEquals(BOOLEAN_VALUE, attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE, null);
 Attribute parentAttribute = new Attribute(Attributes.dataPrefix + "testData", "testValue", null);
 attribute.setParent(parentAttribute);
 Assert.assertEquals(BOOLEAN_ATTRIBUTE_KEY, attribute.getKey());
 Assert.assertEquals(BOOLEAN_VALUE, attribute.getValue());
 Assert.assertEquals(parentAttribute, attribute.getParent());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 attribute.setKey(NON_BOOLEAN_ATTRIBUTE_KEY);
 Assert.assertEquals(NON_BOOLEAN_ATTRIBUTE_KEY, attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyWithEmptyKey() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 attribute.setKey(EMPTY_STRING);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 attribute.setValue(NON_BOOLEAN_VALUE);
 Assert.assertEquals(NON_BOOLEAN_VALUE, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 attribute.html(sb, OUTPUT_SETTINGS);
 String expectedHtml = StringUtil.releaseBuilder(sb);
 Assert.assertEquals("checked=\"true\"", expectedHtml);
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Assert.assertTrue(attribute.shouldCollapseAttribute(OUTPUT_SETTINGS));
 String expectedCollapsedHtml = "checked";
 attribute.html(sb, OUTPUT_SETTINGS);
 String actualCollapsedHtml = StringUtil.releaseBuilder(sb);
 Assert.assertEquals(expectedCollapsedHtml, actualCollapsedHtml);
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeAndEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, EMPTY_STRING);
 Assert.assertTrue(attribute.shouldCollapseAttribute(OUTPUT_SETTINGS));
 String expectedCollapsedHtml = "checked";
 attribute.html(sb, OUTPUT_SETTINGTINGS);
 String actualCollapsedHtml = StringUtil.releaseBuilder(sb);
 Assert.assertEquals(expectedCollapsedHtml, actualCollapsedHtml);
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeAndSameValueAsKey() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_ATTRIBUTE_KEY);
 Assert.assertTrue(attribute.shouldCollapseAttribute(OUTPUT_SETTINGS));
 String expectedCollapsedHtml = "checked=" + BOOLEAN_ATTRIBUTE_KEY;
 attribute.html(sb, OUTPUT_SETTINGS);
 String actualCollapsedHtml = StringUtil.releaseBuilder(sb);
 Assert.assertEquals(expectedCollapsedHtml, actualCollapsedHtml);
 }

 @Test
 public void testShouldCollapseAttributeWithFalseOutputSettings() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 OUTPUT_SETTINGS.syntax(Document.OutputSettings.Syntax.xml);
 Assert.assertFalse(attribute.shouldCollapseAttribute(OUTPUT_SETTINGS));
 String expectedExpandedHtml = "checked=\"true\"";
 attribute.html(sb, OUTPUT_SETTINGS);
 String actualExpandedHtml = StringUtil.releaseBuilder(sb);
 Assert.assertEquals(expectedExpandedHtml, actualExpandedHtml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHtmlWithNullParent() throws IOException {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Assert.assertThrows(IllegalArgumentException.class, () -> attribute.html(null, OUTPUT_SETTINGS));
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithNullOutputSettings() throws IOException {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Assert.assertThrows(SerializationException.class, () -> attribute.html(null, null));
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute2 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute3 = new Attribute(NON_BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute4 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, NON_BOOLEAN_VALUE);
 Attribute attribute5 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute6 = attribute1;
 Assert.assertTrue(attribute1.equals(attribute1));
 Assert.assertTrue(attribute1.equals(attribute6));
 Assert.assertFalse(attribute1.equals(attribute2));
 Assert.assertFalse(attribute1.equals(attribute3));
 Assert.assertFalse(attribute1.equals(attribute4));
 Assert.assertTrue(attribute5.equals(attribute1));
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute2 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute3 = new Attribute(NON_BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute attribute4 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, NON_BOOLEAN_VALUE);
 Attribute attribute5 = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Assert.assertEquals(attribute1.hashCode(), attribute5.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute4.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute attribute = new Attribute(BOOLEAN_ATTRIBUTE_KEY, BOOLEAN_VALUE);
 Attribute clone = attribute.clone();
 Attribute cloneWithParent = attribute.clone();
 cloneWithParent.setParent(new Attribute(Attributes.dataPrefix + "testData", "testValue", null));
 Assert.assertEquals(attribute, clone);
 Assert.assertNotEquals(attribute, cloneWithParent);
 }
}