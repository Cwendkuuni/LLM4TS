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
 public void testAttributeCreation() {
 Attribute newAttribute = new Attribute("key", "value2");
 assertEquals("value", attribute.getValue());
 assertEquals("key", attribute.getKey());
 assertEquals(newAttribute.getKey(), "key");
 assertEquals(newAttribute.getValue(), "value2");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyInput() {
 try {
 new Attribute(null, "value");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyInput() {
 try {
 new Attribute("", "value");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueInput() {
 try {
 new Attribute("key", null);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyAfterSet() {
 try {
 attribute.setKey("");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = SerializationException.class)
 public void testIOExceptionThrownDuringHtmlGeneration() {
 try {
 attribute.html();
 fail("Should have thrown an IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testHtmlGeneration() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute newAttribute = Attribute.createFromEncoded("key", "key=\"value\"");
 assertEquals("value", newAttribute.getValue());
 assertEquals("key", newAttribute.getKey());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 Attribute nonDataAttribute = new Attribute("key", null);
 assertFalse(nonDataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute booleanAttribute = new Attribute("checked", null);
 assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
 Attribute nonBooleanAttribute = new Attribute("nonBooleanKey", "nonBooleanValue");
 assertFalse(nonBooleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
 }
}