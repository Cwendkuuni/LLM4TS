import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", null);
 Attribute parentAttribute = new Attribute("parentKey", "parentValue");
 Attribute attributeWithParent = new Attribute("key", "value", parentAttribute);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 Assert.assertEquals(parentAttribute, attribute.parent);
 }

 @Test
 public void testGetKey() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = AssertionError.class)
 public void testSetKeyWithNullKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test
 public void testgetValue() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 attribute.setValue(newValue);
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = AssertionError.class)
 public void testSetValueWithNullValue() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 Assert.assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", "value");
 Assert.assertTrue(attribute.isDataAttribute());
 Attribute attributeWithoutPrefix = new Attribute("key", "value");
 Assert.assertFalse(attributeWithoutPrefix.isDataAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked");
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithNullValue() {
 Attribute booleanAttributeWithNullValue = new Attribute("checked");
 Assert.assertTrue(booleanAttributeWithNullValue.isBooleanAttribute());
 }

 @Test(expected = AssertionError.class)
 public void testShouldCollapseAttributeWithInvalidOutputSettings() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.shouldCollapseAttribute((Document.OutputSettings) null));
 }

 @Test
 public void testShouldCollapseAttributeWithHtmlSyntaxAndEmptyValue() {
 Attribute attribute = new Attribute("key", "");
 Assert.assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testShouldCollapseAttributeWithHtmlSyntaxAndValueEqualToKey() {
 Attribute attribute = new Attribute("key", "key");
 Assert.assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testShouldCollapseAttributeWithHtmlSyntaxAndBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", "");
 Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testShouldCollapseAttributeWithHtmlSyntaxAndBooleanAttributeWithValue() {
 Attribute booleanAttributeWithValue = new Attribute("checked", "checked");
 Assert.assertTrue(booleanAttributeWithValue.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testShouldCollapseAttributeWithHtmlSyntaxAndNonBooleanAttribute() {
 Attribute nonBooleanAttribute = new Attribute("key", "value");
 Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 }

 @Test
 public void testShouldCollapseAttributeWithXmlSyntax() {
 Attribute attribute = new Attribute("key", "value");
 Assert.assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml)));
 }
}