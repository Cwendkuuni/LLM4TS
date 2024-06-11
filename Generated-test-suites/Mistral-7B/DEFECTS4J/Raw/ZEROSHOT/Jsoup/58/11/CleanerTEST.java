import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTagNames("p", "a");
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testClean() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test'>Hello World</p>" +
 "<script>alert('xss')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><p id='test'>Hello World</p></body></html>", cleanDocument.body().html());
 assertNull(cleanDocument.body().getElementsByTag("script").first());
 }

 @Test
 public void testIsValid() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test'>Hello World</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));

 Document invalidDocument = Jsoup.parse("<html><body><script>alert('xss')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCleanWithEmptyWhitelist() {
 Whitelist emptyWhitelist = Whitelist.emptyTagNames();
 Cleaner cleanerWithEmptyWhitelist = new Cleaner(emptyWhitelist);
 Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
 Document cleanDocument = cleanerWithEmptyWhitelist.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.body().html());
 }

 @Test(expected = NullPointerException.class)
 public void testNullWhitelist() {
 Cleaner cleanerWithNullWhitelist = new Cleaner(null);
 }
}