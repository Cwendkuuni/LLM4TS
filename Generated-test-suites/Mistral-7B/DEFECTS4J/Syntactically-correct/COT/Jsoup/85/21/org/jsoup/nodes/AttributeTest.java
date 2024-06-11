import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attributes.AttributesNotFound;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

 private static final String KEY = "testKey";
 private static final String VALUE = "testValue";
 private static final String ENCODED_VALUE = "testValue%20encoded";
 private static final String BOOLEAN_ATTRIBUTE = "testBooleanAttribute";
 private static final String DATA_ATTRIBUTE = "data-testAttribute";

 @Test
 public void testConstructorWithKeyAndValue() {
 Attribute attribute = new Attribute(KEY, VALUE);
 Assert.assertEquals(KEY, attribute.getKey());
 Assert.assertEquals(VALUE, attribute.getValue());
 }

 @Test
 public void testConstructorWithKeyValueAndParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(KEY, VALUE, parent);
 parent.put(KEY, attribute);
 Assert.assertEquals(KEY, attribute.getKey());
 Assert.assertEquals(VALUE, attribute.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attribute = new Attribute(null, VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 Attribute attribute = new Attribute("", VALUE);
 }

 @Test
 public void testSetKey() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.setKey("newKey");
 Assert.assertEquals("newKey", attribute.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attribute = new Attribute(KEY, null);
 }

 @Test
 public void testSetValue() {
 Attribute attribute = new Attribute(KEY, VALUE);
 Assert.assertEquals(VALUE, attribute.getValue());
 attribute.setValue("newValue");
 Assert.assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.html(sb, new Document("").outputSettings());
 Assert.assertEquals(KEY + "=\"" + VALUE + "\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Assert.assertTrue(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), KEY, VALUE));
 Assert.assertTrue(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), null, VALUE));
 Assert.assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), KEY, ""));
 Assert.assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), KEY, VALUE.toUpperCase()));
 Assert.assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "test", VALUE));
 Assert.assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), KEY, VALUE + " otherValue"));
 Assert.assertFalse(Attribute.shouldCollapseAttribute(new Document.OutputSettings(), "test", "otherValue"));
 }

 @Test
 public void testIsBooleanAttribute() {
 Attribute booleanAttribute = new Attribute(BOOLEAN_ATTRIBUTE);
 Assert.assertTrue(booleanAttribute.isBooleanAttribute());
 Attribute notBooleanAttribute = new Attribute("notBooleanAttribute");
 Assert.assertFalse(notBooleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attribute = Attribute.createFromEncoded(KEY, ENCODED_VALUE);
 Assert.assertEquals(KEY, attribute.getKey());
 Assert.assertEquals(VALUE, attribute.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute dataAttribute = new Attribute(DATA_ATTRIBUTE, VALUE);
 Assert.assertTrue(dataAttribute.isDataAttribute());
 Attribute notDataAttribute = new Attribute("notDataAttribute", VALUE);
 Assert.assertFalse(notDataAttribute.isDataAttribute());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFromEncodedWithNullUnencodedKey() {
 Attribute.createFromEncoded(null, VALUE);
 }

 @Test
 public void testCreateFromEncodedWithEmptyEncodedValue() {
 Attribute attribute = Attribute.createFromEncoded(KEY, "");
 Assert.assertEquals(KEY, attribute.getKey());
 Assert.assertNull(attribute.getValue());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute original = new Attribute(KEY, VALUE);
 Attribute cloned = original.clone();
 Assert.assertEquals(original.getKey(), cloned.getKey());
 Assert.assertEquals(original.getValue(), cloned.getValue());
 }

 @Test(expected = AttributesNotFound.class)
 public void testGetParentNotFound() {
 Attribute attribute = new Attribute(KEY, VALUE);
 attribute.getParent();
 }

 @Test
 public void testPutGetFromParent() {
 Attributes parent = new Attributes();
 Attribute attribute = new Attribute(KEY, VALUE);
 parent.put(KEY, attribute);
 Attribute retrieved = (Attribute) parent.get(KEY);
 Assert.assertEquals(attribute.getKey(), retrieved.getKey());
 Assert.assertEquals(attribute.getValue(), retrieved.getValue());
 }
}