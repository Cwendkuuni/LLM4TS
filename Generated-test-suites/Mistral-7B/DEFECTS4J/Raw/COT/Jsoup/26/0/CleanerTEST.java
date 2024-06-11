import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class CleanerTest {

 private Document dirtyDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title' onclick='alert(\"XSS\")'>Title<script>alert('XSS')</script></body></html>");
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText());
 assertFalse(cleanDocument.body().hasAttr("onclick"));
 }

 @Test
 public void testIsValidMethod() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesHappyPath() {
 Document cleanDocument = cleaner.clean(Jsoup.parse("<html><body><h1>Title</h1></body></html>"));
 Element h1 = cleanDocument.selectFirst("h1");
 assertNotNull(h1);
 assertEquals("Title", h1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesNullDocument() {
 cleaner.copySafeNodes(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesNullElement() {
 cleaner.copySafeNodes(dirtyDocument, null);
 }

 @Test
 public void testCopySafeNodesUnsafeTag() {
 Document dirtyDocumentWithScript = Jsoup.parse("<html><body><h1 onclick='alert(\"XSS\")'>Title<script>alert('XSS')</script></body></html>");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocumentWithScript.body(), null);
 assertEquals(2, numDiscarded);
 }

 @Test
 public void testCreateSafeElementHappyPath() {
 Element dirtyElement = dirtyDocument.selectFirst("h1");
 ElementMeta safeElementMeta = cleaner.createSafeElement(dirtyElement);
 assertNotNull(safeElementMeta.el);
 assertEquals(0, safeElementMeta.numAttribsDiscarded);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementNullElement() {
 cleaner.createSafeElement(null);
 }

 @Test
 public void testCreateSafeElementUnsafeAttribute() {
 Element dirtyElementWithOnclick = new Element(Tag.H1, dirtyDocument.baseUri(), null);
 dirtyElementWithOnclick.setAttribute("onclick", "alert('XSS')");
 ElementMeta safeElementMeta = cleaner.createSafeElement(dirtyElementWithOnclick);
 assertNotNull(safeElementMeta.el);
 assertEquals(1, safeElementMeta.numAttribsDiscarded);
 assertNull(safeElementMeta.el.attr("onclick"));
 }

 @Test
 public void testCreateSafeElementSafeAttribute() {
 Element dirtyElementWithClass = new Element(Tag.H1, dirtyDocument.baseUri(), null);
 dirtyElementWithClass.classes().add("myclass");
 ElementMeta safeElementMeta = cleaner.createSafeElement(dirtyElementWithClass);
 assertNotNull(safeElementMeta.el);
 assertEquals(0, safeElementMeta.numAttribsDiscarded);
 assertNotNull(safeElementMeta.el.classes());
 assertNotNull(safeElementMeta.el.className());
 }

 @Test
 public void testIsSafeTagAndIsSafeAttributeEdgeCases() {
 Whitelist whitelistWithSafeScript = Whitelist.simpleText();
 whitelistWithSafeScript.addTags("script");
 cleaner = new Cleaner(whitelistWithSafeScript);
 Element dirtyElementWithSafeScript = new Element(Tag.SCRIPT, dirtyDocument.baseUri(), null);
 assert(cleaner.isSafeTag("script", dirtyDocument, null) == true);
 assert(cleaner.isSafeAttribute("script", dirtyDocument, null, "src", null) == true);
 }

 @Test
 public void testIsSafeTagAndIsSafeAttributeExceptions() {
 cleaner = new Cleaner(whitelist);
 Element dirtyElementWithMalformedTag = new Element(Tag.BODY, dirtyDocument.baseUri(), null);
 dirtyElementWithMalformedTag.tagName("malformedTag");
 assert(cleaner.isSafeTag("malformedTag", dirtyDocument, null) == false);
 Element dirtyElementWithMalformedAttribute = new Element(Tag.DIV, dirtyDocument.baseUri(), null);
 dirtyElementWithMalformedAttribute.tagName("div");
 dirtyElementWithMalformedAttribute.setAttribute("malformedAttribute", "value");
 assert(cleaner.isSafeAttribute("div", dirtyDocument, null, "malformedAttribute", null) == false);
 }

}