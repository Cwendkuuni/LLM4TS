import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
 // Test creating an attribute with null parent
 Attribute attributeNullParent = new Attribute("key", "value");

 // Test creating an attribute with non-null parent
 Attribute attributeWithParent = new Attribute("key", "value", attributes);

 // Test setting key
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());

 // Test setting value
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());

 // Test setting key and value with parent
 attributeWithParent.setKey("newKey");
 attributeWithParent.setValue("newValue");
 assertEquals("newKey", attributeWithParent.getKey());
 assertEquals("newValue", attributeWithParent.getValue());
 assertSame(attributes, attributeWithParent.parent);

 // Test creating attribute from encoded value
 Attribute encodedAttribute = Attribute.createFromEncoded("key", "value=%20test");
 assertEquals("key", encodedAttribute.getKey());
 assertEquals(" value test", encodedAttribute.getValue());
 }

 @Test
 public void testShouldCollapseAttribute() {
 // Test shouldCollapseAttribute with null parent
 assertTrue(attribute.shouldCollapseAttribute(null));

 // Test shouldCollapseAttribute with non-null parent
 assertFalse(attributeWithParent.shouldCollapseAttribute(null));

 // Test shouldCollapseAttribute with empty value
 assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 assertFalse(attributeWithParent.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

 // Test shouldCollapseAttribute with value equal to key
 assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
 assertFalse(attributeWithParent.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));

 // Test shouldCollapseAttribute with boolean attribute
 assertTrue(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), "checked"));
 assertFalse(attributeWithParent.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), "checked"));

 // Test shouldCollapseAttribute with non-boolean attribute
 assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), "test"));
 assertFalse(attributeWithParent.shouldCollapseAttribute(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), "test"));
 }

 @Test
 public void testEquals() {
 Attribute equalAttribute = new Attribute("key", "value", attributes);
 Attribute notEqualAttribute = new Attribute("key2", "value2", attributes);
 Attribute sameAttribute = attribute;

 assertEquals(attribute, attribute);
 assertEquals(attribute, sameAttribute);
 assertNotEquals(attribute, null);
 assertNotEquals(attribute, notEqualAttribute);
 }

 @Test
 public void testHashCode() {
 Attribute attribute1 = new Attribute("key1", "value1");
 Attribute attribute2 = new Attribute("key2", "value2");
 Attribute attribute3 = new Attribute("key1", "value1");

 int hashCode1 = attribute1.hashCode();
 int hashCode2 = attribute2.hashCode();
 int hashCode3 = attribute3.hashCode();

 assertEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneException() {
 attribute.clone(); // should throw CloneNotSupportedException
 }
}