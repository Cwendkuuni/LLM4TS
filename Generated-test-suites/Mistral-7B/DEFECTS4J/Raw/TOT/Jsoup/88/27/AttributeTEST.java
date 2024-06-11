import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testGetKey() {
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testGetValue() {
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "key=value";
 Attribute attributeFromEncoded = Attribute.createFromEncoded("key", encodedKey);
 assertEquals("key", attributeFromEncoded.getKey());
 assertEquals("value", attributeFromEncoded.getValue());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 throw new IOException();
 attribute.html(sb, new Document("").outputSettings());
 }

 @Test
 public void testIsBooleanAttribute() {
 boolean expected = false;
 assertEquals(expected, attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 boolean expected = false;
 assertEquals(expected, attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithDataPrefix() {
 String dataKey = "data-key";
 Attribute dataAttribute = new Attribute(dataKey, null, null);
 boolean expected = true;
 assertEquals(expected, dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 boolean expected = false;
 assertEquals(expected, attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 boolean expected = true;
 assertEquals(expected, attribute.shouldCollapseAttribute("booleanAttribute", null, out));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() {
 Document.OutputSettings out = new Document.OutputSettings();
 String emptyValue = "";
 boolean expected = true;
 assertEquals(expected, attribute.shouldCollapseAttribute("emptyValue", emptyValue, out));
 }

 @Test
 public void testShouldCollapseAttributeWithSameValueAsKey() {
 Document.OutputSettings out = new Document.OutputSettings();
 boolean expected = true;
 Attribute attributeSameValue = new Attribute("sameValue", "sameValue");
 assertEquals(expected, attributeSameValue.shouldCollapseAttribute(out));
 }

 @Test
 public void testEquals() {
 Attribute otherAttribute = new Attribute("key", "value");
 assertEquals(attribute, otherAttribute);
 }

 @Test
 public void testNotEqualsWithDifferentKey() {
 Attribute otherAttribute = new Attribute("otherKey", "value");
 assertNotEquals(attribute, otherAttribute);
 }

 @Test
 public void testNotEqualsWithDifferentValue() {
 Attribute otherAttribute = new Attribute("key", "otherValue");
 assertNotEquals(attribute, otherAttribute);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}