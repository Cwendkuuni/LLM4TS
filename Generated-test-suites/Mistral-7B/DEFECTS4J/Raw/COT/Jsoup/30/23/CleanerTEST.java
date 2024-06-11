import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementHelper;
import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 Whitelist whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);

 String html = "<html><head></head><body><div id='test' data-test='123'>Hello <script>alert('XSS');</script> World</div></body></html>";
 dirtyDocument = Jsoup.parse(html);
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 /**
 * Test for cleaner constructor
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(null);
 assertNull(cleaner.whitelist);
 }

 /**
 * Test for clean method with valid input
 */
 @Test
 public void testCleanValidInput() {
 Document cleaned = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().text(), cleaned.body().text());
 }

 /**
 * Test for clean method with invalid input
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidInput() {
 cleaner.clean(null);
 }

 /**
 * Test for isValid method with valid input
 */
 @Test
 public void testIsValidValidInput() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test for isValid method with invalid input
 */
 @Test
 public void testIsValidInvalidInput() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test for copySafeNodes method with safe tag and attributes
 */
 @Test
 public void testCopySafeNodesSafeTagAndAttributes() {
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals(0, numDiscarded);
 }

 /**
 * Test for copySafeNodes method with unsafe tag and attributes
 */
 @Test
 public void testCopySafeNodesUnsafeTagAndAttributes() {
 int numDiscarded = cleaner.copySafeNodes(ElementHelper.parse("<div id='test' data-test='123'>Hello <script>alert('XSS');</script> World</div>"), cleanDocument.body());
 assertEquals(1, numDiscarded);
 }

 /**
 * Test for createSafeElement method with safe tag and attributes
 */
 @Test
 public void testCreateSafeElementSafeTagAndAttributes() {
 ElementMeta meta = cleaner.createSafeElement(dirtyDocument.body().elementSibling(0));
 assertEquals("DIV", meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Test for createSafeElement method with unsafe tag and attributes
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementUnsafeTagAndAttributes() {
 cleaner.createSafeElement(dirtyDocument.body().elementSibling(1));
 }

}