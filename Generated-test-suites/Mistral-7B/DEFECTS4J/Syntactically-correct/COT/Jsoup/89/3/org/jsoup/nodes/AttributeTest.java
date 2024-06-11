import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.Map;

public class AttributeTest {

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute("key", "value");
 String expectedKey = "key";
 String expectedValue = "value";

 assertEquals(expectedKey, attribute.getKey());
 assertEquals(expectedValue, attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attribute attribute = new Attribute("key", "value", new Attributes());
 String expectedKey = "key";
 String expectedValue = "value";

 assertEquals(expectedKey, attribute.getKey());
 assertEquals(expectedValue, attribute.getValue());
 }

 @Test (expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, "value");
 }

 @Test (expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute("key", null);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute("key", "value");
 String newKey = "newKey";

 attribute.setKey(newKey);
 String expectedNewKey = "newKey";

 assertEquals(expectedNewKey, attribute.getKey());
 }

 @Test (expected = NullPointerException.class)
 public void testNullNewKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey(null);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

 @Test
 public void testGetValue() {
 Attribute attribute = new Attribute("key", "value");
 String expectedValue = "value";

 assertEquals(expectedValue, attribute.getValue());
 }

 @Test (expected = SerializationException.class)
 public void testHtmlMethodThrowsSerializationException() {
 Attribute attribute = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 try {
 attribute.html(sb, new Document("").outputSettings());
 } catch (IOException e) {
 throw new SerializationException(e);
 }
 }

 @Test
 public void testToStringMethod() {
 Attribute attribute = new Attribute("key", "value");
 String expectedHtml = "key=\"value\"";

 assertEquals(expectedHtml, attribute.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 String unencodedKey = "key";
 String encodedValue = "key=\"value\"";
 Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
 String expectedKey = "key";
 String expectedValue = "value";

 assertEquals(expectedKey, attribute.getKey());
 assertEquals(expectedValue, attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attribute = new Attribute("data-key", null);
 boolean expected = true;

 assertTrue(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithNonDataAttribute() {
 Attribute attribute = new Attribute("key", null);
 boolean expected = false;

 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 boolean expected = false;

 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeAndEmptyValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("checked", "");
 boolean expected = true;

 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeAndValueSameAsKey() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("checked", "checked");
 boolean expected = true;

 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanAttributeAndNullValue() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("checked", null);
 boolean expected = true;

 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeWithNonBooleanAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 Attribute attribute = new Attribute("key", "value");
 boolean expected = false;

 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testEmptyKeyWhenSettingKey() {
 Attribute attribute = new Attribute("key", "value");
 attribute.setKey("");
 }

}