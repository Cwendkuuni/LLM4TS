import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attribute = new Attribute("key", "value");
 Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

 assertEquals("key", attribute.getKey());
 assertEquals("value", attribute.getValue());
 assertNull(attribute.getParent());
 assertEquals("key", attributeWithParent.getKey());
 assertEquals("value", attributeWithParent.getValue());
 assertNotNull(attributeWithParent.getParent());
 }

 @Test
 public void testSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute attribute = new Attribute("key", "value");
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, attribute.getValueBeforeChange());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder stringBuilder = StringUtil.borrowBuilder();
 attribute.html(stringBuilder, new Document("").outputSettings());
 String htmlOutput = StringUtil.releaseBuilder(stringBuilder);
 assertEquals("key=\"value\"", htmlOutput);
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", "");
 Attribute nonBooleanAttribute = new Attribute("customAttribute", "value");
 assertTrue(booleanAttribute.isBooleanAttribute());
 assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute booleanAttribute1 = new Attribute("checked", "");
 Attribute booleanAttribute2 = new Attribute("checked", "");
 Attribute booleanAttribute3 = new Attribute("checked", "true");
 Attribute nonBooleanAttribute = new Attribute("customAttribute", "value");

 assertTrue(booleanAttribute1.shouldCollapseAttribute(outputSettings));
 assertTrue(booleanAttribute2.shouldCollapseAttribute(outputSettings));
 assertFalse(booleanAttribute3.shouldCollapseAttribute(outputSettings));
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testCloneMethod() {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 assertNotSame(attribute, clonedAttribute);
 }
}