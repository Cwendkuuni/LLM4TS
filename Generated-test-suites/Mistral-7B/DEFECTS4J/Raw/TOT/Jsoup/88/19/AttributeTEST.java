import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testKey() {
 String expectedKey = "key";
 assertEquals(expectedKey, attribute.getKey());
 assertEquals(expectedKey, attribute.toString());
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testValue() {
 String expectedValue = "value";
 assertEquals(expectedValue, attribute.getValue());
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, attribute.getValue()); // Old value should be returned
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 Attribute nullKeyAttribute = new Attribute(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute emptyKeyAttribute = new Attribute("", "value");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsDataAttribute() {
 String dataKey = "data-test";
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute(dataKey, null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeFalse() {
 String nonDataKey = "key";
 assertFalse(attribute.isDataAttribute());
 assertFalse(new Attribute(nonDataKey, null).isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testShouldCollapseAttributeFalse() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String value = "testValue";
 attribute.setValue(value);
 assertFalse(attribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute encodedAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals(attribute, encodedAttribute);
 }
}