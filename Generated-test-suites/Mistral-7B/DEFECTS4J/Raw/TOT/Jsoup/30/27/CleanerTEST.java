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

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello</h1><p>World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1><p></p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithCustomWhitelist() {
 Whitelist customWhitelist = Whitelist.simpleText();
 cleaner = new Cleaner(customWhitelist);

 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello</h1><p>World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<p></p>", cleanDocument.html());
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 cleaner.clean(null);
 }
}