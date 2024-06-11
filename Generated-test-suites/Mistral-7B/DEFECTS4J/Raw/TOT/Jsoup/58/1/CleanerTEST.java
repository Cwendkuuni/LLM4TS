import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private static final Whitelist BASIC_WHITELIST = Whitelist.basic();

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>");
 Cleaner cleaner = new Cleaner(BASIC_WHITELIST);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", cleanDocument.html());
 assertNull(cleanDocument.select("script").first());
 }

 @Test
 public void testInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1><img src='javascript:alert(\"XSS\")'></body></html>");
 Cleaner cleaner = new Cleaner(BASIC_WHITELIST);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element source = Jsoup.parse("<html><body><h1 id='title' class='title'><span>Hello World</span></h1></body></html>").body().get(0);
 Element destination = Element.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals("<html><body><h1 id='title' class='title'><span>Hello World</span></h1></body></html>", destination.html());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithInvalidAttributes() {
 Element source = Jsoup.parse("<html><body><h1 id='title' class='title' onclick='alert(\"XSS\")'><span>Hello World</span></h1></body></html>").body().get(0);
 Element destination = Element.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals("<html><body><h1 id='title' class='title'></h1></body></html>", destination.html());
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithInvalidTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Element destination = Element.parse("<html><body></body></html>").body();
 Cleaner cleaner = new Cleaner(BASIC_WHITELIST);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
 assertEquals("<html><body></body></html>", destination.html());
 assertEquals(1, numDiscarded);
 }

}