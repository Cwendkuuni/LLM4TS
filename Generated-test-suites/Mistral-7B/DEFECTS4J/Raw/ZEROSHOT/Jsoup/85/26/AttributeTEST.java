import org.junit.After;
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

 @After
 public void tearDown() {
 attribute = null;
 attributes = null;
 }

 @Test
 public void testAttributeCreation() {
 // Test creation with unencoded key and value.
 Attribute createdAttribute = new Attribute("key", "value");
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 assertNull(createdAttribute.getParent());

 // Test creation with unencoded key, value and parent.
 createdAttribute = new Attribute("key", "value", attributes);
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 assertSame(attributes, createdAttribute.getParent());
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 assertSame(attributes, attribute.getParent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertSame(attributes, attribute.getParent());
 assertEquals(oldValue, attributes.get(attribute.getKey()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = StringUtil.releaseBuilder(sb);
 String html = attribute.html();
 assertEquals(expectedHtml, html);
 }

 @Test
 public void testToString() {
 String expectedToString = attribute.html();
 String toString = attribute.toString();
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testCreateFromEncoded() {
 String unencodedKey = "unencodedKey";
 String encodedValue = "encodedValue";
 Attribute createdAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 assertEquals(unencodedKey, createdAttribute.getKey());
 assertEquals(encodedValue, createdAttribute.getValue());
 assertNull(createdAttribute.getParent());
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 String booleanAttributeKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanAttributeKey, null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 assertFalse(new Attribute("value", "", attributes).shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 Attribute otherAttribute = new Attribute("key", "value", attributes);
 assertTrue(attribute.equals(otherAttribute));
 otherAttribute.setValue("otherValue");
 assertFalse(attribute.equals(otherAttribute));
 }

 @Test
 public void testHashCode() {
 Attribute otherAttribute = new Attribute("key", "value", attributes);
 int thisHashCode = attribute.hashCode();
 int otherHashCode = otherAttribute.hashCode();
 assertEquals(thisHashCode, otherHashCode);
 otherAttribute.setValue("otherValue");
 assertNotEquals(thisHashCode, otherHashCode);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertNotSame(attribute, clonedAttribute);
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 assertSame(attributes, clonedAttribute.getParent());
 }
}