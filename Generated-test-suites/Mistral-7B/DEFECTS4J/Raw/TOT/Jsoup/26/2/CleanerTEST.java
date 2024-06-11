import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Document dirtyDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.standard();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Jsoup.parse("<html><body>" +
 " <h1 id='title'>Hello World</h1>" +
 " <script>alert('XSS')</script>" +
 " <p>This is a paragraph.</p>" +
 "</body></html>");
 }

 /**
 * Test cleaning a valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'>Hello World</h1><p></p></body></html>", cleanDocument.body().html());
 }

 /**
 * Test cleaning an invalid document
 */
 @Test(expected = IllegalStateException.class)
 public void testCleanInvalidDocument() {
 cleaner.isValid(dirtyDocument);
 }

 /**
 * Test cleaning a document with a safe tag and attributes
 */
 @Test
 public void testCleanSafeTagAndAttributes() {
 Document cleanDocument = cleaner.clean(Jsoup.parse("<html><body><p id='p1' class='class1'>Hello World</p></body></html>"));
 Element pElement = cleanDocument.selectFirst("p");
 assertEquals("p", pElement.tagName());
 assertTrue(pElement.hasAttr("id"));
 assertTrue(pElement.hasAttr("class"));
 }

 /**
 * Test cleaning a document with an unsafe tag
 */
 @Test(expected = IllegalStateException.class)
 public void testCleanUnsafeTag() {
 cleaner.clean(Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>"));
 }

 /**
 * Test cleaning a document with an unsafe attribute
 */
 @Test(expected = IllegalStateException.class)
 public void testCleanUnsafeAttribute() {
 cleaner.clean(Jsoup.parse("<html><body><p id='p1' onclick='alert('XSS')'>Hello World</p></body></html>"));
 }

 /**
 * Test cleaning a document with multiple unsafe elements
 */
 @Test(expected = IllegalStateException.class)
 public void testCleanMultipleUnsafeElements() {
 cleaner.clean(Jsoup.parse("<html><body><h1 id='title'>Hello World</h1><script>alert('XSS')</script>" +
 " <p>This is a paragraph.</p><script>alert('XSS')</script></body></html>"));
 }
}