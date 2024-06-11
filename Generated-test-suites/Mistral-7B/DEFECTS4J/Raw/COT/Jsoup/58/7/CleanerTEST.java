import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;
import org.jsoup.select.Elements;

public class CleanerTest {

 private Document dirtyDocument;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 Parser parser = new Parser();
 String html = "<html><head><script>alert('XSS')</script></head><body><h1 id='title'>Hello</h1><p>You <b>have</b> been <i>cleaned</i></p></body></html>";
 dirtyDocument = parser.parseBodyFragment(html);
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 Assert.assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element titleElement = cleanDocument.selectFirst("h1");
 Assert.assertNotNull(titleElement);
 Assert.assertEquals("Hello", titleElement.text());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidMethod() {
 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 Parser parser = new Parser();
 String html = "<html><body><h1>Hello</h1></body></html>";
 Document validDocument = parser.parseBodyFragment(html);
 Assert.assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testCopySafeNodesMethod() {
 Element root = dirtyDocument.body();
 Element dest = Document.createShell(dirtyDocument.baseUri()).body();
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 Element titleElement = dest.selectFirst("h1");
 Assert.assertNotNull(titleElement);
 Assert.assertEquals("Hello", titleElement.text());
 Assert.assertEquals(2, numDiscarded); // head and script tag
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyDocument() {
 Element root = Document.createShell(null).body();
 Element dest = Document.createShell(null).body();
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCreateSafeElementMethod() {
 Element sourceElement = dirtyDocument.body().selectFirst("h1");
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 Assert.assertNotNull(elementMeta.el);
 Assert.assertEquals("h1", elementMeta.el.tagName());
 }

 @Test
 public void testCreateSafeElementMethodWithScriptTag() {
 Element sourceElement = dirtyDocument.head().selectFirst("script");
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 Assert.assertNull(elementMeta.el);
 }

 @Test
 public void testCreateSafeElementMethodWithUnknownTag() {
 Element sourceElement = dirtyDocument.body().selectFirst("x");
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 Assert.assertNull(elementMeta.el);
 }

 @Test
 public void testCopySafeNodesMethodWithAttributes() {
 Element root = dirtyDocument.body();
 Element dest = Document.createShell(dirtyDocument.baseUri()).body();
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 Element titleElement = dest.selectFirst("h1");
 Element metaElement = dest.selectFirst("meta");
 Element scriptElement = dest.selectFirst("script");

 Assert.assertNotNull(titleElement);
 Assert.assertEquals("Hello", titleElement.text());
 Assert.assertNull(metaElement);
 Assert.assertNull(scriptElement);
 Assert.assertEquals(3, numDiscarded); // head, script, and meta tag
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyAttributes() {
 Element root = dirtyDocument.body();
 Element dest = Document.createShell(dirtyDocument.baseUri()).body();
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 Element titleElement = dest.selectFirst("h1");
 Element metaElement = dest.selectFirst("meta");
 Element scriptElement = dest.selectFirst("script");

 Assert.assertNotNull(titleElement);
 Assert.assertEquals("Hello", titleElement.text());
 Assert.assertNull(metaElement);
 Assert.assertNull(scriptElement);
 Assert.assertEquals(2, numDiscarded); // head and script tag
 }

 @Test
 public void testCopySafeNodesMethodWithAttributesWithScriptTag() {
 Element root = dirtyDocument.body();
 Element dest = Document.createShell(dirtyDocument.baseUri()).body();
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 Element titleElement = dest.selectFirst("h1");
 Element metaElement = dest.selectFirst("meta");
 Element scriptElement = dest.selectFirst("script");

 Assert.assertNotNull(titleElement);
 Assert.assertEquals("Hello", titleElement.text());
 Assert.assertNull(metaElement);
 Assert.assertNotNull(scriptElement);
 Assert.assertEquals("alert('XSS')", scriptElement.attr("src"));
 Assert.assertEquals(3, numDiscarded); // head, script, and meta tag
 }

 @Test
 public void testCopySafeNodesMethodWithFramesetDocument() {
 Parser parser = new Parser();
 String html = "<html><head><frameset></frameset></head><body></body></html>";
 Document framesetDocument = parser.parseBodyFragment(html);
 Document cleanDocument = cleaner.clean(framesetDocument);
 Element bodyElement = cleanDocument.body();
 Assert.assertNotNull(bodyElement);
 Assert.assertEquals(0, bodyElement.childrenSize());
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyDocument() {
 Document emptyDocument = Document.createShell(null);
 Document cleanDocument = cleaner.clean(emptyDocument);
 Assert.assertNotNull(cleanDocument);
 Assert.assertEquals(0, cleanDocument.body().childrenSize());
 }
}