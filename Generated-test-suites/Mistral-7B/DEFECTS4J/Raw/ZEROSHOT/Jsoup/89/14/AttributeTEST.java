import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Arrays;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 // Test creating a new Attribute with a key and value
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());

 // Test creating a new Attribute with a key, value, and parent
 Attribute attrParent = new Attributes();
 Attribute attrWithParent = new Attribute("keyWithParent", "valueWithParent", attrParent);
 assertEquals("keyWithParent", attrWithParent.getKey());
 assertEquals("valueWithParent", attrWithParent.getValue());
 Assert.assertEquals(attrWithParent, attrParent.put("keyWithParent", "valueWithParent"));
 }

 @Test
 public void testAttributeSetters() {
 // Test setting the key
 Attribute attr = new Attribute("initialKey", "initialValue");
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());

 // Test setting the value
 Attribute attrValue = new Attribute("valueKey", "");
 attrValue.setValue("newValue");
 assertEquals("newValue", attrValue.getValue());

 // Test setting the key and value together
 Attribute attrBoth = new Attribute("bothKey", "bothValue");
 attrBoth.setKey("newKey");
 attrBoth.setValue("newValue");
 assertEquals("newKey", attrBoth.getKey());
 assertEquals("newValue", attrBoth.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 // Test creating an Attribute and calling html method
 Attribute attr = new Attribute("testKey", "testValue");
 String htmlOutput = attr.html();
 String expected = "testkey=\"testValue\"";
 assertEquals(expected, htmlOutput);

 // Test creating an Attribute with a parent and calling html method
 Attributes parentAttrs = new Attributes();
 Attribute attrParent = new Attribute("parentKey", "parentValue", parentAttrs);
 parentAttrs.put("parentKey", attrParent);
 attrParent.setValue("newValue");
 String htmlOutputParent = attrParent.html();
 String expectedParent = "parentkey=\"newValue\"";
 assertEquals(expectedParent, htmlOutputParent);
 }

 @Test
 public void testToStringMethod() {
 // Test creating an Attribute and calling toString method
 Attribute attr = new Attribute("toStringKey", "toStringValue");
 String toStringOutput = attr.toString();
 String expected = "toStringkey=\"toStringValue\"";
 assertEquals(expected, toStringOutput);
 }

 @Test
 public void testCreateFromEncodedMethod() {
 // Test creating an Attribute from encoded key and value
 String encodedKey = "data-test-encoded-key";
 String encodedValue = "data-test-encoded-value";
 Attribute attrEncoded = Attribute.createFromEncoded("testKey", encodedValue);
 String decodedKey = attrEncoded.getKey();
 String decodedValue = attrEncoded.getValue();
 assertEquals("testKey", decodedKey);
 assertEquals("data-test-encoded-value", decodedValue);
 }

 @Test
 public void testIsDataAttributeMethod() {
 // Test checking if an Attribute is a data attribute
 Attribute dataAttr = new Attribute("dataTestKey", "");
 assertTrue(dataAttr.isDataAttribute());

 // Test checking if a non-data Attribute is not a data attribute
 Attribute nonDataAttr = new Attribute("nonDataKey", "");
 assertFalse(nonDataAttr.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethod() throws IOException {
 // Test checking if an Attribute should be collapsed based on its key and value
 Attribute attr = new Attribute("testKey", "testValue");
 Document.OutputSettings outputSettings = Document.OutputSettings.htmlParagraphMode();
 boolean shouldCollapse = attr.shouldCollapseAttribute(outputSettings);
 assertFalse(shouldCollapse);

 Attribute boolAttr = new Attribute("booleanAttr", "");
 shouldCollapse = boolAttr.shouldCollapseAttribute(outputSettings);
 assertTrue(shouldCollapse);
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 // Test checking if an Attribute is a boolean attribute
 Attribute booleanAttr = new Attribute("booleanAttr", "");
 assertTrue(Attribute.isBooleanAttribute("booleanAttr"));

 Attribute nonBooleanAttr = new Attribute("nonBooleanAttr", "");
 assertFalse(Attribute.isBooleanAttribute("nonBooleanAttr"));
 }

 @Test
 public void testEqualsMethod() {
 // Test checking if two equal Attributes are equal
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key1", "value1");
 assertEquals(attr1, attr2);

 // Test checking if two non-equal Attributes are not equal
 Attribute attr3 = new Attribute("key2", "value2");
 assertNotEquals(attr1, attr3);
 }

 @Test
 public void testHashCodeMethod() {
 // Test checking if two equal Attributes have the same hash code
 Attribute attr1 = new Attribute("key1", "value1");
 Attribute attr2 = new Attribute("key1", "value1");
 int hashCode1 = attr1.hashCode();
 int hashCode2 = attr2.hashCode();
 assertEquals(hashCode1, hashCode2);

 // Test checking if two non-equal Attributes have different hash codes
 Attribute attr3 = new Attribute("key2", "value2");
 int hashCode3 = attr3.hashCode();
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testCloneMethod() {
 // Test cloning an Attribute
 Attribute attr = new Attribute("clonedKey", "clonedValue");
 Attribute clonedAttr = attr.clone();
 assertEquals(attr, clonedAttr);
 }
}