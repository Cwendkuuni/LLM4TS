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
 Attribute createdAttribute = new Attribute("key", "value");
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 }

 @Test
 public void testConstructorWithKeyAndValueAndParent() {
 Attribute createdAttribute = new Attribute("key", "value", new Attributes()); // Assuming Attributes is a valid class name
 assertEquals("key", createdAttribute.getKey());
 assertEquals("value", createdAttribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullKey() {
 Attribute createdAttribute = new Attribute(null, "value");
 fail("Should not be able to create an Attribute with a null key");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullValue() {
 Attribute createdAttribute = new Attribute("key", null);
 fail("Should not be able to create an Attribute with a null value");
 }

 @Test
 public void testSettingKey() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullKey() {
 attribute.setKey(null);
 fail("Should not be able to set a null key");
 }

 @Test
 public void testSettingEmptyKey() {
 attribute.setKey("");
 assertEquals("", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullValue() {
 attribute.setValue(null);
 fail("Should not be able to set a null value");
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String expectedHtml = "key=\"value\"";
 assertEquals(expectedHtml, StringUtil.releaseBuilder(sb).trim());
 }

 @Test
 public void testShouldCollapseAttribute() {
 assertTrue(attribute.shouldCollapseAttribute((Document.OutputSettings) null));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testIsDataAttribute() {
 assertFalse(attribute.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeWithDataPrefixKey() {
 Attribute dataAttribute = new Attribute("data-key", null);
 assertTrue(dataAttribute.isDataAttribute());
 }
}