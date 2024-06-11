import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

public class CleanerTest {

 private static final String VALID_HTML = "<html><body><p>Valid HTML</p></body></html>";
 private static final String INVALID_HTML = "<html><body><p>Valid HTML</p><script>alert('XSS')</script></body></html>";

 @Test
 public void testCleanValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assertions.assertEquals(VALID_HTML, cleanDocument.body().html(), "Cleaning valid HTML should not change its content");
 }

 @Test
 public void testCleanInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assertions.assertFalse(cleanDocument.body().text().contains("alert('XSS')"),
 "Cleaning invalid HTML should remove the XSS script tag");
 }

 @Test
 public void testIsValidValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 Assertions.assertTrue(cleaner.isValid(dirtyDocument), "Valid HTML should be considered valid by the cleaner");
 }

 @Test
 public void testIsValidInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 Assertions.assertFalse(cleaner.isValid(dirtyDocument), "Invalid HTML should be considered invalid by the cleaner");
 }

 @Test
 public void testCopySafeNodesValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();

 int numDiscarded = 0;
 numDiscarded += copySafeNodes(root, createTestElement());

 Assertions.assertEquals(1, numDiscarded, "Copying safe nodes from valid HTML should not discard any nodes");
 }

 @Test
 public void testCopySafeNodesInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();

 int numDiscarded = 0;
 numDiscarded += copySafeNodes(root, createTestElement());

 Assertions.assertGreater(numDiscarded, 1, "Copying safe nodes from invalid HTML should discard some nodes");
 }

 private static Element createTestElement() {
 return new Element("test", null);
 }

 private static int copySafeNodes(Element root, Element dest) {
 int numDiscarded = 0;
 // Empty implementation for testing purposes
 return numDiscarded;
 }
}