import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setup() {
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

 @Test(expected = NullPointerException.class)
 public void testSetKeyWithNullKey() {
 attribute.setKey(null);
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

 @Test(expected = NullPointerException.class)
 public void testSetValueWithNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws Exception {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", "value");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertFalse(attribute.shouldCollapseAttribute(null));
 assertTrue(attribute.shouldCollapseAttribute(null, null, null));

 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Document.OutputSettings.Syntax.html);
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", null, outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", "", outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", "value", outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", "value", outputSettings, true));

 outputSettings.syntax(Document.OutputSettings.Syntax.xml);
 assertTrue(attribute.shouldCollapseAttribute("key", null, outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", "", outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", "value", outputSettings));
 assertFalse(attribute.shouldCollapseAttribute("key", "value", outputSettings, true));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testIsBooleanAttributeWithBooleanAttributeNames() {
 for (String booleanAttributeName : Attribute.booleanAttributes) {
 Attribute booleanAttribute = new Attribute(booleanAttributeName, null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }
 }

 @Test
 public void testEquals() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testNotEqualsWithDifferentKey() {
 Attribute anotherAttribute = new Attribute("otherKey", "value");
 assertFalse(attribute.equals(anotherAttribute));
 }

 @Test
 public void testNotEqualsWithDifferentValue() {
 Attribute anotherAttribute = new Attribute("key", "otherValue");
 assertFalse(attribute.equals(anotherAttribute));
 }

 @Test
 public void testEqualsWithSameKeyAndSameValue() {
 Attribute anotherAttribute = new Attribute("key", "value");
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testEqualsWithSameKeyButDifferentValue() {
 Attribute anotherAttribute = new Attribute("key", "otherValue");
 assertFalse(attribute.equals(anotherAttribute));
 }

 @Test
 public void testEqualsWithSameKeyButNullValue() {
 Attribute anotherAttribute = new Attribute("key", null);
 assertTrue(attribute.equals(anotherAttribute));
 }

 @Test
 public void testEqualsWithNull() {
 assertFalse(attribute.equals(null));
 }

 @Test
 public void testHashCode() {
 Attribute anotherAttribute = new Attribute("key", "value");
 int thisHashCode = attribute.hashCode();
 int anotherHashCode = anotherAttribute.hashCode();assertEquals(thisHashCode, anotherHashCode); }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}