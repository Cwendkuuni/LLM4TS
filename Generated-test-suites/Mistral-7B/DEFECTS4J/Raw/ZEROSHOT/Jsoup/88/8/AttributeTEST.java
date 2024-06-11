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
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testCreateNewAttribute() {
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals("key", newAttribute.getKey());
 assertEquals("value", newAttribute.getValue());
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyEmpty() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("value", attribute.getValue());
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testEquals() {
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals(attribute, newAttribute);
 }

 @Test
 public void testNotEquals() {
 Attribute newAttribute = new Attribute("key2", "value");
 assertNotEquals(attribute, newAttribute);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeFalse() {
 Attribute attribute = new Attribute("notDataAttribute", "value");
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeFalse() throws IOException {
 Document.OutputSettings out = new Document.OutputSettings();
 String value = "newValue";
 attribute.setValue(value);
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeFalse() {
 Attribute attribute = new Attribute("notBooleanAttribute", "value");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeTrue() {
 Attribute attribute = new Attribute("allowfullscreen", null);
 assertTrue(attribute.isBooleanAttribute());
 }
}