import org.junit.jupitertesting.BeforeEach;
import org.junit.jupitertesting.TestInstance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CleanerTest {

 private Document dirtyDocument;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @BeforeEach
 void setUp() {
 dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello World')</script><div id='test'>Test Content</div></body></html>");
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 void testCleanMethod() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 Assertions.assertFalse(body.select("script").size() > 0);
 Assertions.assertEquals("Test Content", body.select("#test").text());
 }

 @Test
 void testIsValidMethod_ValidDocument() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertTrue(isValid);
 }

 @Test
 void testIsValidMethod_InvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('Hello World')</script><div id='test' onclick='alert('XSS')'>Test Content</div></body></html>");
 boolean isValid = cleaner.isValid(invalidDocument);
 Assertions.assertFalse(isValid);
 }

 @Test
 void testIsValidMethod_WithWhitelist() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertTrue(isValid);
 }
}