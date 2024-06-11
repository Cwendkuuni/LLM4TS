import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @Test
 public void testAttributeCreation() {
 // Test creating an attribute with raw key and value
 Attribute a = new Attribute("key", "value");
 assertEquals("key", a.getKey());
 assertEquals("value", a.getValue());

 // Test creating an attribute with raw key, value, and parent
 Attribute b = new Attribute("key", "value", attributes);
 assertEquals("key", b.getKey());
 assertEquals("value", b.getValue());
 assertEquals(attributes, b.parent);
 }

 @Test
 public void testSettingKey() {
 Attribute c = new Attribute("key", "value");
 c.setKey("newKey");
 assertEquals("newKey", c.getKey());
 }

 @Test
 public void testSettingValue() {
 Attribute d = new Attribute("key", "value");
 String oldValue = d.setValue("newValue");
 assertEquals("newValue", d.getValue());
 assertEquals("value", oldValue);
 }

 @Test
 public void testHtmlGeneration() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 Attribute e = new Attribute("key", "value");
 e.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertEquals("<key=\"value\">", html);
 }

 @Test
 public void testToString() {
 Attribute f = new Attribute("key", "value");
 assertEquals("<key=\"value\">", f.toString());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute g = Attribute.createFromEncoded("key", "<key=\"value\">");
 assertEquals("key", g.getKey());
 assertEquals("value", g.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 assertTrue(Attribute.isDataAttribute("data-key"));
 assertFalse(Attribute.isDataAttribute("key"));
 }

 @Test
 public void testShouldCollapseAttribute() throws IOException {
 Document.OutputSettings settings = new Document.OutputSettings();
 Attribute h = new Attribute("boolean", "");
 assertTrue(h.shouldCollapseAttribute(settings));
 settings.setSyntax(Document.OutputSettings.Syntax.xml);
 Attribute i = new Attribute("boolean", "");
 assertFalse(i.shouldCollapseAttribute(settings));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute j = new Attribute(null, "value");
 fail("Null key should not be accepted");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute k = new Attribute("key", null);
 fail("Null value should not be accepted");
 }

 @Test
 public void testEquals() {
 Attribute l = new Attribute("key1", "value1");
 Attribute m = new Attribute("key1", "value1");
 Attribute n = new Attribute("key2", "value2");
 assertTrue(l.equals(m));
 assertFalse(l.equals(n));
 }

 @Test
 public void testHashCode() {
 Attribute o = new Attribute("key1", "value1");
 Attribute p = new Attribute("key1", "value1");
 Attribute q = new Attribute("key2", "value2");
 int hashCode1 = o.hashCode();
 int hashCode2 = p.hashCode();
 int hashCode3 = q.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attribute r = new Attribute("key", "value");
 Attribute s = r.clone();
 assertEquals(r, s);
 }
}