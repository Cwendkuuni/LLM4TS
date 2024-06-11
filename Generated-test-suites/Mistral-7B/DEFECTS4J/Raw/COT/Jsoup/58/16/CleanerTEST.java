import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DataNode;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);

 String html = "<html><head><title>Test</title></head><body><h1 id='title' class='test'>Hello World</h1><script>alert('XSS')</script></body></html>";
 dirtyDocument = Parser.parseBodyFragment(html, "");
 }

 @Test
 public void testCleanerWithValidHtml() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().text().equals("Hello World"));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidWithValidHtml() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = AssertionError.class)
 public void testIsValidWithInvalidHtml() {
 dirtyDocument = Parser.parseBodyFragment("<html><head><title>Test</title><script>alert('XSS')</script></body></html>", "");
 assertFalse(cleaner.isValid(dirtyDocument));
 cleanDocument = cleaner.clean(dirtyDocument);
 }

 @Test
 public void testCopySafeNodesWithValidElements() {
 Element source = dirtyDocument.body().getElementsByTag("h1").first();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithInvalidAttributes() {
 Element source = dirtyDocument.body().getElementsByTag("h1").first();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(2, numDiscarded);
 }

 @Test
 public void testCreateSafeElementWithValidAttributes() {
 Element source = dirtyDocument.body().getElementsByTag("h1").first();
 Element dest = cleaner.createSafeElement(source);
 assertEquals("h1", dest.tagName());
 }

 @Test
 public void testCreateSafeElementWithInvalidAttributes() {
 Element source = dirtyDocument.body().getElementsByTag("h1").first();
 Element dest = cleaner.createSafeElement(source);
 assertEquals(1, dest.attributes().size());
 }
}