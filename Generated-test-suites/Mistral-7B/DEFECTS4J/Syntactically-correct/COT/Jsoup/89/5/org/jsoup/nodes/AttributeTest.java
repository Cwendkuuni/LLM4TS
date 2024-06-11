import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

 private Attribute attribute;

 @Test
 public void testAttributeConstructionWithKeyAndValue() {
 attribute = new Attribute("key", "value");
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeConstructionWithKeyValueAndParent() {
 attribute = new Attribute("key", "value", null);
 Assert.assertEquals("key", attribute.getKey());
 Assert.assertEquals("value", attribute.getValue());
 }

 @Test
 public void testAttributeConstructionFromEncoded() {
 attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
 Assert.assertEquals("unencodedKey", attribute.getKey());
 Assert.assertEquals("decodedValue", attribute.getValue()); // decodedValue should be the result of Entities.unescape("encodedValue")
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute("data-key", null);
 Attribute nonDataAttribute = new Attribute("key", null);
 Assert.assertTrue(dataAttribute.isDataAttribute());
 Assert.assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test(expected = SerializationException.class)
 public void testHtmlMethodThrowsSerializationException() throws IOException {
 attribute = new Attribute("key", "value");
 attribute.html(); // this method throws SerializationException if not called properly
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntaxFromString("html5");
 Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings)); // shouldCollapseAttribute returns true for boolean attributes with empty or same value as key
 }

 @Test
 public void testShouldCollapseAttributeReturnsFalse() {
 Document.OutputSettings outputSettings = Document.OutputSettings.syntaxFromString("html5");
 String booleanAttributeKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanAttributeKey, "true", null);
 Assert.assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettings)); // shouldCollapseAttribute returns false for boolean attributes with non-empty or different value than key
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute("checked", null);
 Attribute nonBooleanAttribute = new Attribute("key", null);
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Assert.assertFalse(nonBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEquals() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1, attribute1);
 Assert.assertEquals(attribute1, attribute2);
 Assert.assertNotEquals(attribute1, attribute3);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key1", "value1");
 Attribute attribute3 = new Attribute("key2", "value2");
 Assert.assertEquals(attribute1.hashCode(), attribute1.hashCode());
 Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
 Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute originalAttribute = new Attribute("key", "value");
 Attribute clonedAttribute = originalAttribute.clone();
 Assert.assertEquals(originalAttribute, clonedAttribute);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneThrowsCloneNotSupportedException() throws CloneNotSupportedException {
 Attribute attribute = new Attribute("key", "value");
 Attribute clonedAttribute = (Attribute) attribute.clone(); // This should not be possible since the clone method throws CloneNotSupportedException
 }
}