import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String VALID_HTML = "<html><body><p>Valid HTML with allowed tags and attributes.</p></body></html>";
 private static final String INVALID_HTML = "<html><body><script>alert('XSS Attack!');</script></body></html>";

 @Test
 public void testCleanValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly()); // A simple white-list for testing
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 // Check if the clean document only contains safe elements and attributes
 Element body = cleanedDocument.body();
 Assert.assertEquals("Only body elements are expected", 1, body.childNodes().size());
 Assert.assertTrue("Only safe elements should remain", body.childNode(0) instanceof Paragraph);
 }

 @Test
 public void testCleanInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 // Check if the clean document doesn't contain any unsafe elements or scripts
 Element body = cleanedDocument.body();
 Assert.assertEquals("No script tags should remain", 0, body.getElementsByTag("script").size());
 }

 @Test
 public void testIsValidValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // Check if the valid document is considered valid by the cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assert.assertTrue("Valid HTML should be considered valid", isValid);
 }

 @Test
 public void testIsValidInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // Check if the invalid document is considered invalid by the cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assert.assertFalse("Invalid HTML should be considered invalid", isValid);
 }

 @Test
 public void testCopySafeNodesWithSafeElements() {
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes().put("class", "test"));
 sourceElement.text("Test content");
 Document dirtyDocument = Document.createShell("http://example.com");
 dirtyDocument.body().appendChild(sourceElement);

 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = Document.createShell("http://example.com");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 // Check if the safe elements and their attributes are copied correctly
 Element destElement = cleanDocument.body().childNode(0);
 Assert.assertEquals("The copied element should have the same tag name", "div", destElement.tagName());
 Assert.assertEquals("The copied element should have the same class attribute", "test", destElement.className());
 Assert.assertEquals("The copied element should contain the same content", "Test content", destElement.text());
 Assert.assertEquals("No elements should be discarded when copying safe elements", 0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithUnsafeElements() {
 Element sourceElement = new Element(Tag.valueOf("script"), null, new Attributes().put("src", "malicious.js"));
 Document dirtyDocument = Document.createShell("http://example.com");
 dirtyDocument.body().appendChild(sourceElement);

 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = Document.createShell("http://example.com");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 // Check if the unsafe elements are discarded correctly
 Assert.assertEquals("One script element should be discarded", 1, numDiscarded);
 Assert.assertNull("The copied document should not contain the unsafe script element", cleanDocument.body().getElementsByTag("script").get(0));
 }
}