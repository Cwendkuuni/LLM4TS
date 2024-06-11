import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><head><title>Test</title></html><body><h1 id='test'>Hello World</h1></body>";

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 }

 @Test
 public void testCleanMethod() {
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 Assert.assertNotNull(cleanDocument.body());
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><h1>Valid Document</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);

 Assert.assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><h1>Invalid Document</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);

 Assert.assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesWithValidElements() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='test'>Hello World</h1><p>Test Paragraph</p></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, dest);

 Element expectedDest = new Element("div", dirtyDocument.baseUri(), new Attributes().put("id", "test"));
 expectedDest.appendChild(new Element("h1", dirtyDocument.baseUri(), new Attributes()).text("Hello World"));
 expectedDest.appendChild(new Element("p", dirtyDocument.baseUri(), new Attributes()).text("Test Paragraph"));

 Assert.assertNotNull(dest);
 Assert.assertEquals(expectedDest.html(), dest.html());
 Assert.assertEquals(2, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithInvalidElementInHead() {
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS');</script><title>Test</title></head><body><h1 id='test'>Hello World</h1></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element root = dirtyDocument.body();
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, dest);

 Element expectedDest = new Element("div", dirtyDocument.baseUri(), new Attributes().put("id", "test"));
 expectedDest.appendChild(new Element("h1", dirtyDocument.baseUri(), new Attributes()).text("Hello World"));

 Assert.assertNotNull(dest);
 Assert.assertEquals(expectedDest.html(), dest.html());
 Assert.assertEquals(0, numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullRoot() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element root = null;
 Element dest = new Element("div");
 cleaner.copySafeNodes(root, dest);
 }

 @Test
 public void testCreateSafeElementWithValidElement() {
 Element sourceEl = new Element("h1", null, null).text("Test Element");
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(elementMeta.el);
 Assert.assertEquals("h1", elementMeta.el.tagName());
 Assert.assertEquals("Test Element", elementMeta.el.text());
 Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithInvalidElement() {
 Element sourceEl = new Element("script", null, new Attributes().put("src", "javascript:alert('XSS')"));
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 Assert.assertNull(elementMeta.el);
 }

 @Test
 public void testCreateSafeElementWithEmptyAttributes() {
 Element sourceEl = new Element("div", null, new Attributes());
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(elementMeta.el);
 Assert.assertEquals("div", elementMeta.el.tagName());
 Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithInvalidAttribute() {
 Element sourceEl = new Element("div", null, new Attributes().put("onclick", "javascript:alert('XSS')"));
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 Assert.assertNull(elementMeta.el);
 }

 @Test
 public void testCreateSafeElementWithValidAttribute() {
 Element sourceEl = new Element("div", null, new Attributes().put("class", "test"));
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(elementMeta.el);
 Assert.assertEquals("div", elementMeta.el.tagName());
 Assert.assertEquals("test", elementMeta.el.classes().get(0));
 Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithMultipleAttributes() {
 Element sourceEl = new Element("div", null, new Attributes().put("class", "test").put("id", "test"));
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(elementMeta.el);
 Assert.assertEquals("div", elementMeta.el.tagName());
 Assert.assertEquals("test", elementMeta.el.classes().get(0));
 Assert.assertEquals("test", elementMeta.el.id());
 Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testIsSafeTagWithValidTag() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Assert.assertTrue(cleaner.whitelist.isSafeTag("div"));
 }

 @Test
 public void testIsSafeTagWithInvalidTag() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Assert.assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttributeWithValidTagAndAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Assert.assertTrue(cleaner.whitelist.isSafeAttribute("div", null, "class"));
 }

 @Test
 public void testIsSafeAttributeWithInvalidTagAndAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Assert.assertFalse(cleaner.whitelist.isSafeAttribute("script", null, "onclick"));
 }

 @Test
 public void testGetEnforcedAttributesWithValidTag() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Attributes expectedEnforcedAttrs = new Attributes().put("class", "test").put("id", "test");
 Attributes actualEnforcedAttrs = cleaner.whitelist.getEnforcedAttributes("div");

 Assert.assertEquals(expectedEnforcedAttrs.toString(), actualEnforcedAttrs.toString());
 }

 @Test
 public void testGetEnforcedAttributesWithInvalidTag() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Assert.assertNull(cleaner.whitelist.getEnforcedAttributes("script"));
 }

}