import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attributes attributes;
 private Attribute attribute;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testGetKey() {
 String expectedKey = "key";
 assertEquals(expectedKey, attribute.getKey());
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testGetValue() {
 String expectedValue = "value";
 assertEquals(expectedValue, attribute.getValue());
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder expectedOutput = StringUtil.borrowBuilder();
 Attribute.html("key", "value", expectedOutput, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, expectedOutput.toString());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlWithIOException() throws IOException {
 StringBuilder output = StringUtil.borrowBuilder();
 Attribute.html("key", "value", output, null);
 fail("IOException should be thrown");
 }

 @Test
 public void testCreateFromEncoded() {
 String encodedKey = "key=\"value\"";
 Attribute attributeFromEncoded = Attribute.createFromEncoded("key", encodedKey);
 assertEquals("key", attributeFromEncoded.getKey());
 assertEquals("value", attributeFromEncoded.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 String dataKey = "data-key";
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute(dataKey, null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute());
 }

 @Test
 public void testShouldCollapseAttributeFalse() {
 String nonBooleanKey = "non-boolean-key";
 assertFalse(new Attribute(nonBooleanKey, "value", attributes).shouldCollapseAttribute());
 }

 @Test
 public void testEquals() {
 Attribute equalAttribute = new Attribute("key", "value", attributes);
 assertTrue(attribute.equals(equalAttribute));
 }

 @Test
 public void testNotEqualDifferentKey() {
 Attribute differentKeyAttribute = new Attribute("different-key", "value", attributes);
 assertFalse(attribute.equals(differentKeyAttribute));
 }

 @Test
 public void testNotEqualDifferentValue() {
 Attribute differentValueAttribute = new Attribute("key", "different-value", attributes);
 assertFalse(attribute.equals(differentValueAttribute));
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}