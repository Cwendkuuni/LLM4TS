import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setup() {
 attribute = new Attribute("key", "value");
 attributes = new Attributes();
 attributes.put(attribute);
 }

 @Test
 public void testCreateAttribute() {
 Attribute testAttribute = new Attribute("testKey", "testValue");
 assertEquals("testKey", testAttribute.getKey());
 assertEquals("testValue", testAttribute.getValue());
 }

 @Test
 public void testCreateAttributeWithParent() {
 Attribute testAttribute = new Attribute("testKey", "testValue", attributes);
 assertEquals("testKey", testAttribute.getKey());
 assertEquals("testValue", testAttribute.getValue());
 assertEquals(attributes, testAttribute.parent);
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, null);
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", "");
 assertTrue(booleanAttribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testShouldCollapseAttributeWithValueDifferentFromKey() {
 Attribute testAttribute = new Attribute("testKey", "testValue");
 assertFalse(testAttribute.shouldCollapseAttribute(null));
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute testAttribute = Attribute.createFromEncoded("testKey", "testValue=\"testValue\"");
 assertEquals("testKey", testAttribute.getKey());
 assertEquals("testValue", testAttribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithDataAttribute() {
 Attribute dataAttribute = new Attribute("data-test", "test");
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}