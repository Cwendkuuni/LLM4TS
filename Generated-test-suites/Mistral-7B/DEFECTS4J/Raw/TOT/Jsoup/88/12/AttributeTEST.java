import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;

 @Before
 public void setUp() {
 attribute = new Attribute("key", "value");
 }

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute expectedAttribute = new Attribute("key", "value");
 assertEquals(expectedAttribute, attribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyThrowsIllegalArgumentException() {
 attribute = new Attribute(null, "value");
 fail("This should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyThrowsIllegalArgumentException() {
 attribute = new Attribute("", "value");
 fail("This should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueThrowsIllegalArgumentException() {
 attribute = new Attribute("key", null);
 fail("This should throw an IllegalArgumentException");
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 String oldValue = attribute.getValue();
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 assertEquals(oldValue, attribute.parent.get(attribute.getKey()));
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 String booleanKey = "booleanAttribute";
 Attribute booleanAttribute = new Attribute(booleanKey, null);
 assertFalse(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings outputSettings = null;
 boolean expectedResult = true;
 Attribute booleanAttribute = new Attribute("booleanAttribute", null);
 assertEquals(expectedResult, booleanAttribute.shouldCollapseAttribute(outputSettings));
 outputSettings = new Document.OutputSettings();
 expectedResult = false;
 Attribute stringAttribute = new Attribute("stringAttribute", "value");
 assertEquals(expectedResult, stringAttribute.shouldCollapseAttribute(outputSettings));
 expectedResult = true;
 Attribute emptyStringAttribute = new Attribute("", "");
 assertEquals(expectedResult, emptyStringAttribute.shouldCollapseAttribute(outputSettings));
 expectedResult = true;
 Attribute sameStringAttribute = new Attribute("key", "key");
 assertEquals(expectedResult, sameStringAttribute.shouldCollapseAttribute(outputSettings));
 }

 @Test
 public void testEquals() {
 Attribute otherAttribute = new Attribute("key", "value");
 Attribute expectedAttribute = attribute;
 assertEquals(expectedAttribute, otherAttribute);
 }

 @Test
 public void testHashCode() {
 Attribute otherAttribute = new Attribute("key", "value");
 int expectedHashCode = attribute.hashCode();
 int otherHashCode = otherAttribute.hashCode();
 assertEquals(expectedHashCode, otherHashCode);
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 }
}