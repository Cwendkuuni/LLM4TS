import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 }

 @Test
 public void testConstructorWithKeyValAndParent() {
 Attribute attribute = new Attribute("key", "value", attributes);
 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertSame(attributes, attribute.getParent());
 }

 @Test
 public void testGetKey() {
 assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 assertSame(attributes, attribute.getParent());
 }

 @Test
 public void testGetValue() {
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 assertSame(attributes, attribute.getParent());
 }

 @Test
 public void testHtml() {
 StringBuilder sb = StringUtil.borrowBuilder();
 try {
 attribute.html(sb, new Document("").outputSettings());
 } catch (IOException e) {
 throw new SerializationException(e);
 }
 String expectedHtml = String.format("key=\"value\"", "key", "value");
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testToString() {
 assertEquals(attribute.html(), attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute createdAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 assertNull(createdAttribute.getParent());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "test", null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithValue() {
 Attribute booleanAttributeWithValue = new Attribute("checked", "true", attributes);
 assertTrue(booleanAttributeWithValue.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute anotherAttribute = new Attribute("key", "value", attributes);
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testHashCode() {
 Attribute anotherAttribute = new Attribute("key", "value", attributes);
 assertEquals(attribute.hashCode(), anotherAttribute.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}