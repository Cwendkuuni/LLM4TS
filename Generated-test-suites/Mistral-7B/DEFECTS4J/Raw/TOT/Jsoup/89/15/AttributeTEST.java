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
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals(attribute, newAttribute);
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
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("key=\"value\"", html);
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 attribute.setKey("checked");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute());
 attribute.setValue("value");
 assertFalse(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testEquals() {
 Attribute newAttribute = new Attribute("key", "value");
 assertEquals(attribute, newAttribute);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test
 public void testPutAndGetFromAttributes() {
 attributes.put("key", "value");
 Attribute getAttribute = attributes.get("key");
 assertEquals(attribute, getAttribute);
 }
}