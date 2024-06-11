import org.junit.jupiter.api.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jsoup.safety.Cleaner.*;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;

 @BeforeEach
 public void setup() {
 Whitelist.Simple textOnlyWhitelist = new Whitelist.Simple();
 whitelist = textOnlyWhitelist;

 dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello World!</h1><script>alert('XSS')</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testCleaner_ValidInput() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanedDocument.body().text().equals("Hello World!"));
 }

 @Test
 public void testCleaner_InvalidInput() {
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodes_SafeTag() {
 Element sourceEl = dirtyDocument.body().getElementById("title");
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(sourceEl, cleanDocument.body());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_UnsafeTag() {
 Element scriptEl = dirtyDocument.body().getElementsByTag("script").first();
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(scriptEl, cleanDocument.body());
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_EmptyTextNode() {
 TextNode sourceText = dirtyDocument.body().childNodes().get(1);
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(sourceText, cleanDocument.body());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_TextWithSpecialCharacters() {
 TextNode sourceText = dirtyDocument.body().childNodes().get(2);
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(sourceText, cleanDocument.body());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testIsValid_ValidInput() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValid_InvalidInput() {
 Element scriptEl = dirtyDocument.body().getElementsByTag("script").first();
 Document scriptDocument = Jsoup.parse(scriptEl.outerHtml());
 Cleaner cleaner = new Cleaner(whitelist);
 assertFalse(cleaner.isValid(scriptDocument));
 }

 @Test
 public void testCreateSafeElement_SafeTag() {
 Element sourceEl = dirtyDocument.body().getElementById("title");
 Cleaner cleaner = new Cleaner(whitelist);
 Element metaDest = cleaner.createSafeElement(sourceEl);
 assertEquals(sourceEl.tagName(), metaDest.tagName());
 }

 @Test
 public void testCreateSafeElement_UnsafeTag() {
 Element scriptEl = dirtyDocument.body().getElementsByTag("script").first();
 Cleaner cleaner = new Cleaner(whitelist);
 Element metaDest = cleaner.createSafeElement(scriptEl);
 assertNull(metaDest);
 }

 @Test
 public void testCreateSafeElement_EmptyTextNode() {
 TextNode sourceText = dirtyDocument.body().childNodes().get(1);
 Cleaner cleaner = new Cleaner(whitelist);
 Element metaDest = cleaner.createSafeElement(sourceText);
 assertNull(metaDest);
 }

 @Test
 public void testCreateSafeElement_TextWithSpecialCharacters() {
 TextNode sourceText = dirtyDocument.body().childNodes().get(2);
 Cleaner cleaner = new Cleaner(whitelist);
 Element metaDest = cleaner.createSafeElement(sourceText);
 assertNull(metaDest);
 }

}