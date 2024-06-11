import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanMethod() {
 // Test with a valid HTML document
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.outerHtml());

 // Test with an invalid HTML document
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanInvalidDocument = cleaner.clean(invalidDocument);
 assertNull(cleanInvalidDocument.body().getElementsByTag("script").first());
 }

 @Test
 public void testIsValidMethod() {
 // Test with a valid HTML document
 Document validDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 assertFalse(cleaner.isValid(validDocument));

 // Test with an invalid HTML document
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertTrue(cleaner.isValid(invalidDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidMethodWithNullDocument() {
 cleaner.isValid(null);
 }
}