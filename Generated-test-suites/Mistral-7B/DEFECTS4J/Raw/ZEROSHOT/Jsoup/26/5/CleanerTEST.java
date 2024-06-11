import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 Whitelist whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);

 String html = "<html><body><div id=\"test\" class=\"test\" title=\"test title\" data-test=\"test data\"><h1>Test Heading</h1><script>alert('Test Script');</script></div></body></html>";
 dirtyDocument = Jsoup.parse(html);
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateCleaner() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertEquals(cleanDocument.body().text(), cleanedDocument.body().text());
 assertFalse(cleanedDocument.body().hasText()); // No text nodes should be created
 assertFalse(cleanedDocument.body().hasMetaCharsequences());
 assertFalse(cleanedDocument.body().hasChildNodes());
 }

 @Test
 public void testIsValidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithEmptyWhitelist() {
 Whitelist emptyWhitelist = Whitelist.empty();
 Cleaner cleanerWithEmptyWhitelist = new Cleaner(emptyWhitelist);
 assertTrue(cleanerWithEmptyWhitelist.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithAllowedElementsAndAttributes() {
 Whitelist whitelist = Whitelist.simpleText();
 Whitelist whitelistWithAllowedElements = new Whitelist.SimpleElementFilter(new String[]{"div", "h1"});
 cleaner = new Cleaner(whitelistWithAllowedElements);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCleanerWithNullWhitelist() {
 try {
 Whitelist nullWhitelist = null;
 new Cleaner(nullWhitelist);
 } catch (IllegalArgumentException e) {
 assertEquals("whitelist must not be null", e.getMessage());
 throw e;
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCleanDocumentWithNullDocument() {
 try {
 cleaner.clean(null);
 } catch (NullPointerException e) {
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesWithNullSource() {
 try {
 cleaner.copySafeNodes(null, cleanDocument.body());
 } catch (IllegalArgumentException e) {
 assertEquals("source must not be null", e.getMessage());
 throw e;
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullDestination() {
 try {
 cleaner.copySafeNodes(dirtyDocument.body(), null);
 } catch (NullPointerException e) {
 throw e;
 }
 }

 @Test
 public void testCopySafeNodesWithEmptySource() {
 Element emptySource = new Element(Tag.valueOf("div"));
 cleaner.copySafeNodes(emptySource, cleanDocument.body());
 assertFalse(cleanDocument.body().hasChildNodes());
 }

}