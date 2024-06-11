import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>");
 }

 /**
 * Test case for the constructor
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test case for the clean method
 */
 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", cleanDocument.html());
 }

 /**
 * Test case for the isValid method
 */
 @Test
 public void testIsValidMethod() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test case for the clean method with a valid document
 */
 @Test
 public void testCleanMethodWithValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanValidDocument = cleaner.clean(validDocument);
 assertNotNull(cleanValidDocument);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", cleanValidDocument.html());
 }

 /**
 * Test case for the copySafeNodes method with a valid document and a safe element
 */
 @Test
 public void testCopySafeNodesWithValidDocumentAndSafeElement() {
 Element source = dirtyDocument.selectFirst("h1");
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(0, numDiscarded);
 assertEquals("<h1>Hello World</h1>", destination.html());
 }

 /**
 * Test case for the copySafeNodes method with a valid document and an unsafe element
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesWithValidDocumentAndUnsafeElement() {
 Element source = dirtyDocument.selectFirst("script");
 Element destination = new Element("div");
 cleaner.copySafeNodes(source, destination);
 fail("Expected an IllegalArgumentException");
 }

 /**
 * Test case for the createSafeElement method with a safe tag and attributes
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndAttributes() {
 Element source = dirtyDocument.selectFirst("h1");
 ElementMeta elementMeta = cleaner.createSafeElement(source);
 assertNotNull(elementMeta.el);
 assertEquals("<h1></h1>", elementMeta.el.html());
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 /**
 * Test case for the createSafeElement method with an unsafe tag and attributes
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementWithUnsafeTagAndAttributes() {
 Element source = dirtyDocument.selectFirst("script");
 ElementMeta elementMeta = cleaner.createSafeElement(source);
 fail("Expected an IllegalArgumentException");
 }
}