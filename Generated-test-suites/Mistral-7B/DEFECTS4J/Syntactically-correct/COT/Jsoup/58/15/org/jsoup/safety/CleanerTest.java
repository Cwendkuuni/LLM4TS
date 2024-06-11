import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1><script>alert('XSS')</script></body></html>");
 }

 @Test
 public void testCleanMethod() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'></h1></body></html>", cleanedDocument.html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 dirtyDocument.getElementsByTag("script").first().attr("src", "malicious.js");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidMethodWithNullDocument() {
 cleaner.isValid(null);
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyDocument() {
 Element destination = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(null, destination);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithValidDocument() {
 Element source = dirtyDocument.getElementsByTag("h1").first();
 Element destination = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(source.attributes().size(), numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethodWithNullSource() {
 cleaner.copySafeNodes(null, new Element("body"));
 }

 @Test
 public void testCopySafeNodesMethodWithNullDestination() {
 Element source = dirtyDocument.getElementsByTag("h1").first();
 cleaner.copySafeNodes(source, null);
 }

 @Test
 public void testCreateSafeElementMethodWithEmptyElement() {
 Element emptyElement = new Element("", dirtyDocument.baseUri());
 ElementMeta elementMeta = cleaner.createSafeElement(emptyElement);
 assertNull(elementMeta.el);
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithValidElement() {
 Element sourceElement = dirtyDocument.getElementsByTag("h1").first();
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNotNull(elementMeta.el);
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithMaliciousElement() {
 Element sourceElement = dirtyDocument.getElementsByTag("script").first();
 sourceElement.attr("src", "malicious.js");
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 assertNull(elementMeta.el);
 assertEquals(sourceElement.attributes().size(), elementMeta.numAttribsDiscarded);
 }

}