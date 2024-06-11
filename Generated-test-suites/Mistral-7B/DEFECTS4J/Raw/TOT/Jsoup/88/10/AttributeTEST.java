import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testAttributeCreation() {
 Attribute createdAttribute = new Attribute("key", "value");
 assertEquals(createdAttribute.getKey(), "key");
 assertEquals(createdAttribute.getValue(), "value");
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value", null);
 attribute.setKey("newKey");
 assertEquals(attribute.getKey(), "newKey");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute("key", "value", null);
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", null, null);
 String oldValue = attribute.getValue();
 attribute.setValue("newValue");
 assertEquals(attribute.getValue(), "newValue");
 assertEquals(oldValue, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute("key", null, null);
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute("key", "value", attributes);
 attribute.html(sb, new Document("").outputSettings());
 assertEquals(sb.toString(), "key=\"value\"");
 }

 @Test
 public void testHtmlWithParent() throws IOException {
 Attributes parentAttributes = new Attributes();
 Attribute attribute = new Attribute("key", "value", parentAttributes);
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals(sb.toString(), "key=\"value\"");
 parentAttributes.put("key", attribute);
 attribute.html(sb, new Document("").outputSettings());
 assertEquals(sb.toString(), "key=\"value\"");
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals(attribute.getKey(), "key");
 assertEquals(attribute.getValue(), "value");
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 Attribute attribute = new Attribute("key", null, null);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 attribute.setValue("value");
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute attribute = new Attribute("boolean", null);
 assertTrue(attribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("nonBoolean", null);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }
}