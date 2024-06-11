import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor with null whitelist
 */
 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 /**
 * Test clean method with valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().text().equals("Hello World"));
 }

 /**
 * Test clean method with invalid document
 */
 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse(cleanDocument.body().text().contains("XSS"));
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document
 */
 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with empty root
 */
 @Test
 public void testCopySafeNodesEmptyRoot() {
 Element root = Jsoup.parseBodyFragment("");
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with safe tag and attributes
 */
 @Test
 public void testCopySafeNodesSafeTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title' class='title' style='color:red'>Hello World</h1></body></html>");
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(0), destination);
 assertEquals(0, numDiscarded);
 assertTrue(destination.text().equals("Hello World"));
 }

 /**
 * Test copySafeNodes method with unsafe tag
 */
 @Test
 public void testCopySafeNodesUnsafeTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body().get(0), destination);
 assertEquals(1, numDiscarded);
 assertTrue(destination.text().isEmpty());
 }

 /**
 * Test createSafeElement method with safe tag and attributes
 */
 @Test
 public void testCreateSafeElementSafeTag() {
 Element sourceEl = Jsoup.parse("<html><body><h1 id='title' class='title' style='color:red'>Hello World</h1></body></html>").body().get(0);
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertTrue(meta.el.tagName().equals("h1"));
 assertEquals(2, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with unsafe tag
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementUnsafeTag() {
 Element sourceEl = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>").body().get(0);
 cleaner.createSafeElement(sourceEl);
 }
}