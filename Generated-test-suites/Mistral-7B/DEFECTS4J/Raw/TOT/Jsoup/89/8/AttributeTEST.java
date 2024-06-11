import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setup() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testAttributeCreation() {
 Attribute a = new Attribute("key", "value");
 assertEquals("key", a.getKey());
 assertEquals("value", a.getValue());
 }

 @Test
 public void testSettingKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullKey() {
 attribute.setKey(null);
 }

 @Test
 public void testSettingEmptyKey() {
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNullValue() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", sb.toString());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null, attributes);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertFalse(attribute.shouldCollapseAttribute());
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.shouldCollapseAttribute());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute createdAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
}
}