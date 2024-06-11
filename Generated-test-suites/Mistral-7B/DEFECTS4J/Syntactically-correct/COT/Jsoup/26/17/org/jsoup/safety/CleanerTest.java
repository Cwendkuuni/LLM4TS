import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><div id='test' onclick='alert(\"XSS\")'>Test<script>alert('XSS')</script></div></body></html>");
 }

 /**
 * Test method for {@link Cleaner#Cleaner(Whitelist)}
 */
 @Test
 public void testCleanerWithNullWhitelist() {
 try {
 new Cleaner(null);
 fail("Expected IllegalArgumentException with message: notNull");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 /**
 * Test method for {@link Cleaner#clean(Document)}
 */
 @Test
 public void testClean() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><div id='test'>Test</div></body></html>", cleanDocument.html());
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)}
 */
 @Test
 public void testIsValid() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)}
 */
 @Test
 public void testCopySafeNodes() {
 Element sourceEl = dirtyDocument.selectFirst("div#test");
 Element destinationEl = new Element(Tag.DIV, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceEl, destinationEl);
 assertEquals(2, numDiscarded); // onclick and script tags

 Element safeEl = cleanDocument.body().getElementById("test");
 assertEquals("Test", safeEl.text());
 assertFalse(safeEl.hasAttrs());
 }

 /**
 * Test method for {@link Cleaner#createSafeElement(Element)}
 */
 @Test
 public void testCreateSafeElement() {
 Element sourceEl = dirtyDocument.selectFirst("div#test");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertEquals("<div></div>", meta.el.html());
 assertEquals(2, meta.numAttribsDiscarded); // onclick and script attributes
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} with unsafe tag as parent
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testCopySafeNodesWithUnsafeParent() {
 Element sourceEl = dirtyDocument.selectFirst("html");
 Element destinationEl = new Element(Tag.DIV, dirtyDocument.baseUri());
 cleaner.copySafeNodes(sourceEl, destinationEl);
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)} with empty document
 */
 @Test
 public void testIsValidEmptyDocument() {
 Document emptyDocument = Jsoup.parse("<html></html>");
 assertTrue(cleaner.isValid(emptyDocument));
 }
}