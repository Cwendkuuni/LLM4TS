import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

 @Test
 void testAttributeCreationWithRawKeyAndValue() {
 String key = "class";
 String value = "active";
 Attribute attribute = new Attribute(key, value);
 Assertions.assertEquals(key, attribute.getKey());
 Assertions.assertEquals(value, attribute.getValue());
 }

 @Test
 void testAttributeCreationWithRawKeyAndValueAndParent() {
 String key = "id";
 String value = "elementId";
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(key, value, parent);
 Assertions.assertEquals(key, attribute.getKey());
 Assertions.assertEquals(value, attribute.getValue());
 Assertions.assertNotNull(parent);
 }

 @Test
 void testSettingKey() {
 String key = "key";
 String value = "value";
 Attribute attribute = new Attribute(key, value);
 attribute.setKey("newKey");
 Assertions.assertEquals("newKey", attribute.getKey());
 }

 @Test
 void testSettingValue() {
 String key = "key";
 String oldValue = "oldValue";
 String newValue = "newValue";
 Attribute attribute = new Attribute(key, oldValue);
 String oldValueAfterChange = attribute.setValue(newValue);
 Assertions.assertEquals(newValue, attribute.getValue());
 Assertions.assertEquals(oldValue, oldValueAfterChange);
 }

 @Test
 void testHtmlMethod() throws IOException {
 String key = "key";
 String value = "value";
 Attribute attribute = new Attribute(key, value);
 StringBuilder sb = StringUtil.borrowBuilder();
 document.outputSettings().syntax = Document.OutputSettings.Syntax.html;
 attribute.html(sb, document.outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 Assertions.assertEquals("key=\"value\"", html);
 }

 @Test
 void testToStringMethod() {
 String key = "key";
 String value = "value";
 Attribute attribute = new Attribute(key, value);
 Assertions.assertEquals("key=\"value\"", attribute.toString());
 }

 @Test
 void testCreateFromEncoded() {
 String unencodedKey = "class";
 String encodedValue = "class=\"active\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 Assertions.assertEquals(unencodedKey, attribute.getKey());
 Assertions.assertEquals("active", attribute.getValue());
 }

 @Test
 void testIsDataAttribute() {
 String key = "data-custom";
 Attribute attribute = new Attribute(key, null);
 Assertions.assertTrue(attribute.isDataAttribute());
 }

 @Test
 void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax = Document.OutputSettings.Syntax.html;
 Attribute attribute = new Attribute("checked", "");
 Assertions.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 void testIsBooleanAttribute() {
 String key = "checked";
 Attribute attribute = new Attribute(key, null);
 Assertions.assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 void testEquals() {
 String key1 = "key1";
 String value1 = "value1";
 String key2 = "key2";
 String value2 = "value2";
 Attribute attribute1 = new Attribute(key1, value1);
 Attribute attribute2 = new Attribute(key2, value2);
 Attribute attribute3 = new Attribute(key1, value1);
 Assertions.assertTrue(attribute1.equals(attribute3));
 Assertions.assertFalse(attribute1.equals(attribute2));
 }

 @Test
 void testClone() throws CloneNotSupportedException {
 String key = "key";
 String value = "value";
 Attribute attribute = new Attribute(key, value);
 Attribute clonedAttribute = attribute.clone();
 Assertions.assertEquals(key, clonedAttribute.getKey());
 Assertions.assertEquals(value, clonedAttribute.getValue());
 }

 @Test
 void testCreateAttributeMap() {
 Map<String, Attribute> attributeMap = new HashMap<>();
 attributeMap.put(new Attribute("id", "elementId"), new Attribute("class", "active"));
 Assertions.assertEquals("active", attributeMap.get("class").getValue());
 }
}