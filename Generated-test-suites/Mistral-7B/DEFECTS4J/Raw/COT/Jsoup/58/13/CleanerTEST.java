import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>");
 cleanDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Element body = dirtyDocument.body();
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body(), cleanedDocument.body());
 }

 @Test
 public void testIsValidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document emptyDocument = Jsoup.parse("<html></html>");
 assertTrue(cleaner.isValid(emptyDocument));
 }

 @Test
 public void testIsValidValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element source = dirtyDocument.body();
 Element destination = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesDiscardsAttributes() {
 Element source = Jsoup.parse("<div id='test' class='class' style='color: red;'><script>alert('XSS')</script></div>").body();
 Element destination = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(2, numDiscarded);
 }

 @Test
 public void testCopySafeNodesPreservesAttributes() {
 Element source = Jsoup.parse("<div id='test' class='class' style='color: red;'></div>").body();
 Element destination = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 assertThat(destination.tagName(), is("div"));
 assertThat(destination.attr("id"), is("test"));
 assertThat(destination.attr("class"), is("class"));
 }

 @Test
 public void testCopySafeNodesPreservesText() {
 Element source = Jsoup.parse("<div>Hello World</div>").body();
 Element destination = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 assertEquals("Hello World", destination.text());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanDocumentNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidDocumentNullDocument() {
 cleaner.isValid(null);
 }

 @Test
 public void testCleaningVisitor() {
 Parser parser = new Parser();
 Document document = parser.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>");
 Element root = document.body();
 Element destination = Jsoup.parse("<html><body></body></html>").body();
 CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(document.body());
 assertEquals(8, cleaningVisitor.numDiscarded);
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceEl = Jsoup.parse("<div id='test' class='class' style='color: red;'><script>alert('XSS')</script></div>").body();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEquals("DIV", meta.el.tagName());
 assertEquals(2, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementDoesNotCreateScriptTag() {
 Element sourceEl = Jsoup.parse("<div id='test' class='class' style='color: red;'><script>alert('XSS')</script></div>").body();
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el.childNodes().get(0));
 }
}