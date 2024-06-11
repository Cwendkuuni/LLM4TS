import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerWithInvalidDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertFalse(cleanedDocument.body().hasText());
 }

 @Test
 public void testCleanerWithValidDocument() {
 Document cleanedDocument = cleaner.clean(cleanDocument);
 assertNotNull(cleanedDocument);
 assertNotNull(cleanedDocument.body());
 assertNull(cleanedDocument.body().getElementsByTag("script").first());
 }

 @Test
 public void testIsValidWithValidDocument() {
 boolean isValid = cleaner.isValid(cleanDocument);
 assertTrue(isValid);
 }

 @Test
 public void testIsValidWithInvalidDocument() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanerWithNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanerWithNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleaningVisitorWithNullDestination() {
 new CleaningVisitor(new Element("html"), null);
 }

 @Test
 public void testCleaningVisitorWithSafeTag() {
 Element source = new Element("div");
 source.addClass("testClass");
 Element dest = new Element("div");
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(source);
 assertEquals(1, dest.numChildren());
 assertEquals("div", dest.tagName());
 assertEquals("testClass", dest.classes().get(0));
 }

 @Test
 public void testCleaningVisitorWithUnsafeTag() {
 Element source = new Element("script");
 Element dest = new Element("div");
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(source);
 assertEquals(0, dest.numChildren());
 }

 @Test
 public void testCleaningVisitorWithTextNode() {
 Element source = new Element("div");
 TextNode textNode = new TextNode("Test text");
 source.textNodeSafelySetText(textNode);
 Element dest = new Element("div");
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(source);
 assertEquals(1, dest.numChildren());
 assertEquals("Test text", dest.child(0).text());
 }

 @Test
 public void testCleaningVisitorWithDataNode() {
 DataNode sourceData = new DataNode("<![CDATA[Test data]]>");
 Element dest = new Element("div");
 CleaningVisitor cleaningVisitor = new CleaningVisitor(sourceData.parent(), dest);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(sourceData.parent());
 assertEquals(1, dest.numChildren());
 assertEquals("<![CDATA[Test data]]>", dest.child(0).data());
 }

 private static class TestCleaner extends Cleaner {
 @Override
 public Document clean(Document dirtyDocument) {
 Parser parser = new Parser();
 Document clean = parser.parse(cleaner.toString(dirtyDocument));
 return clean;
 }
 }

 @Test
 public void testCleanerWithEmptyWhitelist() {
 Cleaner testCleaner = new TestCleaner(null);
 Document cleanedDocument = testCleaner.clean(cleanDocument);
 assertNotNull(cleanedDocument);
 assertNotNull(cleanedDocument.body());
 assertFalse(cleanedDocument.body().hasText());
 }

 @Test
 public void testCleanerWithSimpleWhitelist() {
 Whitelist whitelist = Whitelist.simple();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><div class='test'>Test</div></body></html>");
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertNotNull(cleanedDocument.body());
 assertEquals(1, cleanedDocument.body().numChildren());
 assertEquals("div", cleanedDocument.body().child(0).tagName());
 assertEquals("test", cleanedDocument.body().child(0).classes().get(0));
 assertEquals("Test", cleanedDocument.body().child(0).text());
 }

 @Test
 public void testCopySafeNodesWithSafeTagAndAttribute() {
 Element source = new Element("div");
 source.addClass("test");
 source.setAttribute("testAttribute", "Test attribute");
 Element dest = new Element("div");
 int numDiscarded = copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 assertEquals(2, dest.numChildren());
 assertEquals("div", dest.tagName());
 assertEquals("test", dest.classes().get(0));
 assertEquals("Test attribute", dest.attr("testAttribute"));
 }

 @Test
 public void testCopySafeNodesWithUnsafeTagAndAttribute() {
 Element source = new Element("script");
 source.setAttribute("testAttribute", "Test attribute");
 Element dest = new Element("div");
 int numDiscarded = copySafeNodes(source, dest);
 assertEquals(1, numDiscarded);
 assertEquals(0, dest.numChildren());
 }

 private static int copySafeNodes(Element source, Element dest) {
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(source);
 return cleaningVisitor.numDiscarded;
 }

}