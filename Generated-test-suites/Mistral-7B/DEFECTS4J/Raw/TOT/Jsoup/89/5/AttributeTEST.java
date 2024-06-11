import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
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
 public void testAttributeCreation() {
 Attribute newAttribute = new Attribute("newKey", "newValue", attributes);
 assertEquals("newKey", newAttribute.getKey());
 assertEquals("newValue", newAttribute.getValue());
 }

 @Test
 public void testKeySet() {
 attribute.setKey("newKey");
 assertEquals("newKey", attribute.getKey());
 }

 @Test
 public void testKeySetNull() {
 try {
 attribute.setKey(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testValueSet() {
 attribute.setValue("newValue");
 assertEquals("newValue", attribute.getValue());
 }

 @Test
 public void testValueSetNull() {
 try {
 attribute.setValue(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("<key value=\"value\">", html);
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(attribute.isDataAttribute());
 Attribute dataAttribute = new Attribute("data-key", null, attributes);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.syntax(Document.OutputSettings.Syntax.html);
 assertFalse(attribute.shouldCollapseAttribute(settings));
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.shouldCollapseAttribute(settings));
 }

 @Test
 public void testIsBooleanAttribute() {
 assertFalse(attribute.isBooleanAttribute());
 Attribute booleanAttribute = new Attribute("checked", null, attributes);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }
}