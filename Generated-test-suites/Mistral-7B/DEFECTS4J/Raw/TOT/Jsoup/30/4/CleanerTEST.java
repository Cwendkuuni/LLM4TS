import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.ElementHelper;
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
 public void setup() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
 }

 @Test
 public void testCleanValidDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'></h1></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document cleanDocument = cleaner.clean(Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>"));
 assertTrue(cleanDocument.body().text().isEmpty());
 }

 @Test
 public void testIsValidValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 assertFalse(cleaner.isValid(Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>")));
 }

 @Test
 public void testIsValidWithAllowedTag() {
 Whitelist newWhitelist = Whitelist.relaxed();
 cleaner = new Cleaner(newWhitelist);
 Document dirtyDocumentWithTag = Jsoup.parse("<html><body><script src=\"script.js\"></script></body></html>");
 assertTrue(cleaner.isValid(dirtyDocumentWithTag));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullWhitelist() {
 Cleaner cleanerNullWhitelist = new Cleaner(null);
 cleanerNullWhitelist.clean(dirtyDocument);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocumentToClean() {
 cleaner.clean(null);
 }
}