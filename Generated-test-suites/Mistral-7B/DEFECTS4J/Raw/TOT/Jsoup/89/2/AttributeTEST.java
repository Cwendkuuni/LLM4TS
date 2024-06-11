import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attribute attribute;
 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 attribute = new Attribute("key", "value", attributes);
 }

 @Test
 public void testSetKey() {
 String newKey = "newKey";
 attribute.setKey(newKey);
 assertEquals(newKey, attribute.getKey());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetKeyNull() {
 attribute.setKey(null);
 }

 @Test
 public void testSetValue() {
 String newValue = "newValue";
 attribute.setValue(newValue);
 assertEquals(newValue, attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueNull() {
 attribute.setValue(null);
 }

 @Test
 public void testHtml() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithEmptyValue() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setValue("");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("key\"", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testHtmlWithBooleanAttribute() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.setKey("checked");
 attribute.html(sb, new Document("").outputSettings());
 assertEquals("checked", StringUtil.releaseBuilder(sb));
 }

 @Test
 public void testShouldCollapseAttribute() {
 Document.OutputSettings out = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testShouldCollapseAttributeWithBooleanValue() {
 Document.OutputSettings out = new Document.OutputSettings();
 attribute.setValue("checked");
 assertFalse(attribute.shouldCollapseAttribute(out));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertTrue(attribute.isBooleanAttribute());
 attribute.setKey("notBoolean");
 assertFalse(attribute.isBooleanAttribute());
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute.getKey(), clonedAttribute.getKey());
 assertEquals(attribute.getValue(), clonedAttribute.getValue());
 }
}