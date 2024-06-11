import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import java.util.Arrays;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setup() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testCreateAttribute() {
 // Test creating a new Attribute with raw key and value
 Attribute attr = new Attribute("key", "value");
 Validate.assertEquals("key", attr.getKey());
 Validate.assertEquals("value", attr.getValue());

 // Test creating a new Attribute with raw key, value, and parent
 Attribute attrParent = new Attribute("keyParent", null);
 Attribute attrWithParent = new Attribute("key", "value", attrParent);
 Validate.assertEquals("key", attrWithParent.getKey());
 Validate.assertEquals("value", attrWithParent.getValue());
 Validate.assertEquals(attrWithParent, attrParent.get(attrWithParent.getKey()));
 }

 @Test
 public void testSetKey() {
 // Test setting the key with a new value
 attribute.setKey("newKey");
 Validate.assertEquals("newKey", attribute.getKey());

 // Test setting the key with the same value
 attribute.setKey("key");
 Validate.assertEquals("key", attribute.getKey());
 }

 @Test
 public void testSetValue() {
 // Test setting the value with a new value
 attribute.setValue("newValue");
 Validate.assertEquals("newValue", attribute.getValue());

 // Test setting the value with the same value
 attribute.setValue("value");
 Validate.assertEquals("value", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullKey() {
 attribute.setKey(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullValue() {
 attribute.setValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 attribute.setKey("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLongKey() {
 String key = "a".repeat(1000);
 attribute.setKey(key);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 Validate.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsBooleanAttribute() {
 Validate.assertTrue(attribute.isBooleanAttribute());
 String booleanKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanKey, null);
 Validate.assertFalse(booleanAttribute.isBooleanAttribute());
 }
}