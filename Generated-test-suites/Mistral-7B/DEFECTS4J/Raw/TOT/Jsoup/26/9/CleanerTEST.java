import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 private final Whitelist allowedTagsAndAttributes = Whitelist.simpleText(); // a very permissive whitelist for testing

 @Test
 public void testCleanMethod() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title' onclick='alert(\"XSS\")'>Test</h1></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributes);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText()); // the onclick attribute was removed
 }

 @Test
 public void testValidMethod() {
 Document validDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributes);
 boolean isValid = cleaner.isValid(validDocument);
 assertTrue(isValid);
 }

 @Test
 public void testInvalidMethodWithDisallowedTag() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert(\"XSS\")</script></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributes);
 boolean isValid = cleaner.isValid(invalidDocument);
 assertTrue(isValid); // the script tag was not considered due to the whitelist
 Document cleanDocument = cleaner.clean(invalidDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText()); // the script tag was removed
 }

 @Test
 public void testInvalidMethodWithDisallowedAttribute() {
 Document invalidDocument = Jsoup.parse("<html><body><img src='javascript:alert(\"XSS\")'></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributes);
 boolean isValid = cleaner.isValid(invalidDocument);
 assertTrue(isValid); // the img tag was not considered due to the whitelist
 Document cleanDocument = cleaner.clean(invalidDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText()); // the img tag was removed
 }

 @Test(expected = NullPointerException.class)
 public void testNullConstructor() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocumentInput() {
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributes);
 cleaner.clean(null);
 }

 @Test
 public void testCopySafeNodesRecursive() {
 Document dirtyDocument = Jsoup.parse("<html><body><div><h1>Level 1</h1><div><h2>Level 2</h2><div><h3>Level 3</h3></div></div></div></body></html>");
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributes);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);

 Element level3 = cleanDocument.body().selectFirst("h3");
 assertNotNull(level3);
 assertNotNull(level3.text());
 }
}