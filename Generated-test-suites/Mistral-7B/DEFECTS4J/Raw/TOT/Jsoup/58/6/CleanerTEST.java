import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 Validate.notNull(whitelist);
 assertTrue(cleaner instanceof Cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><img src=evil.com></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocumentBodyEmpty() {
 Document emptyDocument = Jsoup.parse("<html><head></head></html>");
 assertFalse(cleaner.isValid(emptyDocument));
 }

 @Test
 public void testIsValidDocumentBodyInvalid() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testIsValidDocumentBodyValid() {
 Document validDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidDocumentBodyWithAllowedTags() {
 Document allowedDocument = Jsoup.parse("<html><body><p><img src=\"image.jpg\"></p></body></html>");
 assertTrue(cleaner.isValid(allowedDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testValidationWithNullDocument() {
 cleaner.isValid(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleaningWithNullDocument() {
 cleaner.clean(null);
 }
}