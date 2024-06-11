import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 cleaner = new Cleaner(simpleWhitelist);

 dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testCleanMethod() {
 Element body = dirtyDocument.body();
 cleaner.clean(dirtyDocument);
 copySafeNodes(body, cleanDocument.body());

 // Test that only allowed elements are present in the cleaned document
 assertEquals("<html><body><h1>Hello World</h1></body></html>", cleanDocument.html());

 // Test that script tag is removed
 assertFalse(cleanDocument.body().hasMetaElements());
 }

 @Test
 public void testIsValidMethod() {
 Element invalidBody = Jsoup.parse("<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(invalidBody, cleanDocument.body());

 // Test that isValidMethod returns false for invalid document
 assertFalse(cleaner.isValid(dirtyDocument));

 // Test that only allowed elements and attributes are present in the valid document
 assertTrue(cleaner.isValid(Jsoup.parse("<html><body><h1>Hello World</h1></body></html>").body()));

 // Test that numDiscarded equals the number of discarded elements and attributes
 assertEquals(5, numDiscarded); // 2 script tags, 1 h1 tag, 2 script attributes
 }

 @Test
 public void testCopySafeNodesMethod() {
 Element validBody = Jsoup.parse("<html><body><h1 id='title' class='title' onclick='click()'>Hello World</h1></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(validBody, cleanDocument.body());

 // Test that all allowed elements and attributes are copied
 assertEquals("<html><body><h1 class='title' id='title'>Hello World</h1></body></html>", cleanDocument.html());

 // Test that number of discarded elements and attributes is zero
 assertEquals(0, numDiscarded);
 }

 // Util method for copying safe nodes from source to destination document
 private void copySafeNodes(Element source, Element destination) {
 Document sourceDoc = source.document();
 Document destDoc = destination.document();
 cleaner.copySafeNodes(source, destination);
 destDoc.decompile(); // Required to ensure that Document.body() returns the current body, not the one before the last call to cleaner.clean()
 }
}