import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.ParseSetup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Document dirtyDocument;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 ParseSetup.setup(); // needed for Jsoup to work in test environment
 whitelist = Whitelist.simpleTagNames("p", "a");
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><p id='test'><a href='/test'>Test</a></p></body></html>");
 }

 @Test
 public void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p id='test'><a href='/test'>Test</a></p></body></html>", cleanDocument.body().html());
 assertNull(cleanDocument.body().select("script").first());
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testIsValidMethodWithEmptyDocument() {
 Document emptyDocument = Jsoup.parse("<html></html>");
 assertTrue(cleaner.isValid(emptyDocument));
 }
}