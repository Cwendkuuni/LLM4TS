import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.AllowAllElementFilter;
import org.jsoup.safety.Whitelist.AllowAllElements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleList();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Jsoup.parse("<html><body>" +
 "<div id='test' class='test' style='color:red;'>" +
 "<script>alert('XSS');</script>" +
 "<h1>Test</h1>" +
 "<input type='text' name='name' value='John Doe'>" +
 "</div>" +
 "</body></html>");
 }

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(null);
 assertNull(cleaner.whitelist);
 }

 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("Test", cleanDocument.select("h1").text());
 assertNull(cleanDocument.select("script").text());
 assertNull(cleanDocument.select("#test").attr("style"));
 assertNull(cleanDocument.select("#test > input").attr("value"));
 }

 @Test
 public void testIsValidMethod() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 Whitelist invalidWhitelist = new Whitelist(new AllowAllElements(), new AllowAllElementFilter());
 Cleaner invalidCleaner = new Cleaner(invalidWhitelist);
 Document invalidDocument = Jsoup.parse("<html><body>" +
 "<script>alert('XSS');</script>" +
 "</body></html>");

 assertFalse(invalidCleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesMethodWithSafeElement() {
 Element sourceElement = dirtyDocument.selectFirst("h1");
 Element destElement = new Element(Tag.H1, sourceElement.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithUnsafeElement() {
 Element sourceElement = dirtyDocument.selectFirst("#test");
 Element destElement = new Element(Tag.DIV, sourceElement.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
 assertEquals(4, numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethodWithNullSourceElement() {
 Element destElement = new Element(Tag.DIV, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(null, destElement);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethodWithNullDestinationElement() {
 Element sourceElement = dirtyDocument.selectFirst("h1");
 int numDiscarded = cleaner.copySafeNodes(sourceElement, null);
 }
}