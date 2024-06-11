import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test the clean method with a valid document
 */
 @Test
 public void testCleanValidDocument() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><h1></h1></body></html>", cleanDocument.html());
 }

 /**
 * Test the clean method with an invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 String html = "<html><head><script>alert('XSS');</script></head><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 cleaner.clean(dirtyDocument); // This test case should throw an IllegalArgumentException
 }

 /**
 * Test the isValid method with a valid document
 */
 @Test
 public void testIsValidValidDocument() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test the isValid method with an invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidDocument() {
 String html = "<html><head><script>alert('XSS');</script></head><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test the clean method with a document containing a parse error
 */
 @Test(expected = ParseErrorList.ParseError.class)
 public void testCleanParseError() {
 String html = "<html><body><script>alert('XSS');</script><h1>Hello World</h1></body>";
 Document dirtyDocument = Jsoup.parseBodyFragment(html);
 cleaner.clean(dirtyDocument); // This test case should throw a ParseError
 }
}