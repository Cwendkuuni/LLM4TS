import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

 // Test case for constructor
 @Test
 public void testCleanerConstruction() {
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 // Test case for clean method with valid document
 @Test
 public void testCleanValidDocument() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleTextAndSimpleHtml();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 // Test case for clean method with invalid document
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 String html = "<html><body><script>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleTextAndSimpleHtml();
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(dirtyDocument); // Exception should be thrown here
 }

 // Test case for isValid method with valid document
 @Test
 public void testIsValidValidDocument() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleTextAndSimpleHtml();
 Cleaner cleaner = new Cleaner(whitelist);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 // Test case for isValid method with invalid document
 @Test
 public void testIsValidInvalidDocument() {
 String html = "<html><body><script>alert('XSS')</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleTextAndSimpleHtml();
 Cleaner cleaner = new Cleaner(whitelist);
 assertFalse(cleaner.isValid(dirtyDocument));
 }
}