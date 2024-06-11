import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><head><title>Test</title></html>" +
         "<body><div id='test' class='test' data-test='test'><h1>Hello <script>alert('XSS')</script> World</h1>" +
         "<p>This <b>is</b> a <span style='color:red'>test</span></p></div></body>";

 @Test
 public void testCreateCleaner() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Assert.assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     Document cleanDocument = cleaner.clean(dirtyDocument);

     Assert.assertNotNull(cleanDocument);
     Assert.assertEquals(2, cleanDocument.body().children().size());
     Assert.assertNull(cleanDocument.body().select("script").first());
     Assert.assertNull(cleanDocument.head());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCleanerWithNullWhitelist() {
     Whitelist nullWhitelist = null;
     Cleaner cleaner = new Cleaner(nullWhitelist);
 }

 @Test
 public void testIsValidDocument() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
     boolean isValid = cleaner.isValid(dirtyDocument);

     Assert.assertFalse(isValid);
 }

 @Test
 public void testIsValidDocumentWithValidHtml() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     String validHtml = "<html><body><h1>Valid HTML</h1></body></html>";
     Document dirtyDocument = Jsoup.parse(validHtml);
     boolean isValid = cleaner.isValid(dirtyDocument);

     Assert.assertTrue(isValid);
 }

 @Test
 public void testCleaningVisitor_head_safeTag() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body><h1>Safe Tag</h1></body></html>");
     Element root = dirtyDocument.body();
     Element destination = new Element("div");

     CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
     NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
     traversor.traverse(root);

     Assert.assertEquals(1, destination.children().size());
     Assert.assertEquals("h1", destination.child(0).tagName());
 }

 @Test
 public void testCleaningVisitor_head_notSafeTag() {
     Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("script");
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body><h1>Not safe tag</h1><script>alert('XSS')</script></body></html>");
     Element root = dirtyDocument.body();
     Element destination = new Element("div");

     CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
     NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
     traversor.traverse(root);

     Assert.assertEquals(0, destination.children().size());
 }

 @Test
 public void testCleaningVisitor_head_dataNode() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body><img data-src=\"image.png\"></body></html>");
     Element root = dirtyDocument.body();
     Element destination = new Element("div");

     CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
     NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
     traversor.traverse(root);

     Assert.assertEquals(1, destination.children().size());
     Assert.assertTrue(destination.child(0) instanceof DataNode);
 }

 @Test
 public void testCleaningVisitor_head_notSafeDataNodeParent() {
     Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("script");
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><script src=\"script.js\"></script></html>");
     Element root = dirtyDocument.body();
     Element destination = new Element("div");

     CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
     NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
     traversor.traverse(root);

     Assert.assertEquals(0, destination.children().size());
 }

 @Test
 public void testCopySafeNodes_emptyDocument() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html></html>");
     Element destination = new Element("div");

     int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);

     Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_notSafeTag() {
     Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("script");
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body><h1>Not Safe Tag</h1></body></html>");
     Element destination = new Element("div");

     int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);

     Assert.assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_safeTagWithAttributes() {
     Whitelist whitelist = Whitelist.relaxed();
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body><div id='test' class='test' data-test='test'><h1>Safe Tag with Attributes</h1></div></body></html>");
     Element destination = new Element("div");

     int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);

     Assert.assertEquals(3, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_safeTagWithAttributesEnforced() {
     Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("div");
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body><div id='test' class='test' data-test='test'><h1>Safe Tag with Attributes</h1></div></body></html>");
     Element destination = new Element("div");

     int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);

     Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_safeTagWithNestedNotSafeTag() {
     Whitelist whitelist = new Whitelist.SimpleTagNameElementFilter("div");
     Cleaner cleaner = new Cleaner(whitelist);
     Document dirtyDocument = Jsoup.parse("<html><body>" +
             "<div id='test'><h1>Safe Tag with Nested Not Safe Tag</h1>" +
             "<script>alert('XSS')</script>" +
             "</div></body></html>");
     Element destination = new Element("div");

     int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);

     Assert.assertEquals(1, numDiscarded);
 }

}