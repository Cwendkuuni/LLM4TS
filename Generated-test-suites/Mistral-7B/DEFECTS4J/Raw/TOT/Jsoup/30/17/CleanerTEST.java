import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body><h1/></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 Element title = root.getElementById("title");
 assertEquals("Hello World", title.text());
 }

 @Test
 public void testCopySafeNodesInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 assertNull(root.getElementsByTag("script").first());
 }
}