import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("testKey", "testValue", attributes);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attr = new Attribute("key", "value", attributes);
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 assertEquals(attributes, attr.parent);
 }

 @Test
 public void testSetKey() {
 Attribute attr = new Attribute("key", "value", attributes);
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 Attribute attr = new Attribute("key", "value", attributes);
 attr.setKey(null);
 }

 @Test
 public void testSetValue() {
 Attribute attr = new Attribute("key", "value", attributes);
 String newValue = "newValue";
 String oldValue = attr.setValue(newValue);
 assertEquals("newValue", attr.getValue());
 assertEquals("value", oldValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 Attribute attr = new Attribute("key", "value", attributes);
 attr.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "testKey=\"testValue\"";
 assertEquals(expectedHtml, sb.toString());
 }

 @Test
 public void testToString() {
 assertEquals("Attribute{key='testKey', value='testValue'}", attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "testKey=testValue";
 Attribute attr = Attribute.createFromEncoded("testKey", encodedKey);
 assertEquals("testKey", attr.getKey());
 assertEquals("testValue", attr.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-test", null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("test", null, attributes);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute nonBooleanAttribute = new Attribute("test", null, attributes);
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithEmptyValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 Attribute emptyValueAttribute = new Attribute("emptyValue", "", attributes);
 assertFalse(emptyValueAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithNonBooleanValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 String nonBooleanValue = "nonBooleanValue";
 Attribute nonBooleanAttribute = new Attribute("nonBoolean", nonBooleanValue, attributes);
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 Attribute otherAttribute = new Attribute("testKey", "testValue", attributes);
 assertTrue(attribute.equals(otherAttribute));
 Attribute otherAttributeDifferentParent = new Attribute("testKey", "testValue", null);
 assertFalse(attribute.equals(otherAttributeDifferentParent));
 Attribute otherAttributeDifferentKey = new Attribute("differentKey", "testValue", attributes);
 assertFalse(attribute.equals(otherAttributeDifferentKey));
 Attribute otherAttributeDifferentValue = new Attribute("testKey", "differentValue", attributes);
 assertFalse(attribute.equals(otherAttributeDifferentValue));
 }

 @Test
 public void testHashCode() {
 Attribute otherAttribute = new Attribute("testKey", "testValue", attributes);
 int thisHashCode = attribute.hashCode();
 int otherHashCode = otherAttribute.hashCode();
 assertEquals(thisHashCode, otherHashCode);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertEquals(attribute.parent, clonedAttribute.parent);
 }
}