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
 public void setup() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><div id='test' attr1='test'><script>alert('XSS');</script></div></body></html>");
 }

 /**
 * Test method for {@link Cleaner#Cleaner(Whitelist)}
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test method for {@link Cleaner#clean(Document)}
 */
 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().child(0).tagName().equals("script"));
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)}
 */
 @Test
 public void testIsValidMethod() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)}
 */
 @Test
 public void testCopySafeNodesMethod() {
 Element source = dirtyDocument.body().child(0);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEquals(source.tagName(), destination.tagName());
 assertFalse(destination.child(0).tagName().equals("script"));
 assertEquals(1, numDiscarded); // test for attribute discarding
 }

 /**
 * Test method for {@link Cleaner#createSafeElement(Element)}
 */
 @Test
 public void testCreateSafeElementMethod() {
 Element source = dirtyDocument.body().child(0);
 Element dest = cleaner.createSafeElement(source);
 assertEquals(source.tagName(), dest.tagName());
 assertFalse(dest.child(0).tagName().equals("script"));
 }

 /**
 * Test method for {@link Cleaner#Cleaner(Whitelist)} with null whitelist
 */
 @Test(expected = NullPointerException.class)
 public void testCleanerConstructorWithNullWhitelist() {
 new Cleaner(null);
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with null document
 */
 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)} with null document
 */
 @Test(expected = NullPointerException.class)
 public void testIsValidMethodWithNullDocument() {
 cleaner.isValid(null);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} with null source
 */
 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethodWithNullSource() {
 cleaner.copySafeNodes(null, new Element("div"));
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} with null destination
 */
 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethodWithNullDestination() {
 cleaner.copySafeNodes(new Element("div"), null);
 }

 /**
 * Test method for {@link Cleaner#createSafeElement(Element)} with null element
 */
 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementMethodWithNullElement() {
 cleaner.createSafeElement(null);
 }
}