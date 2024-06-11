import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;

 @Before
 public void setup() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
 }

 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body><h1 id='title'></h1></body></html>", cleanDocument.body().html());
 assertNull(cleanDocument.body().getElementsByTag("script").first());
 }

 @Test
 public void testIsValidMethod() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullWhitelist() {
 cleaner = new Cleaner(null);
 }
}