import org.jsoup.helper.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = new TextDocument();
 dirtyDocument.body().text("Invalid HTML: <script>alert('XSS');</script>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanerConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testClean() {
 Document result = cleaner.clean(dirtyDocument);
 assertNotNull(result);
 assertFalse(result.body().text().contains("alert('XSS')"));
 }

 @Test
 public void testIsValid() {
 assertFalse(cleaner.isValid(dirtyDocument));
 assertTrue(cleaner.isValid(new TextDocument()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanerInvalidWhitelist() {
 Whitelist invalidWhitelist = null;
 Cleaner invalidCleaner = new Cleaner(invalidWhitelist);
 }

 @Test
 public void testCopySafeNodes() {
 Element root = new Element("html", dirtyDocument.baseUri());
 root.body().html(dirtyDocument.body().html());
 Element dest = cleanDocument.body();
 int numDiscarded = cleaner.copySafeNodes(root, dest);
 assertTrue(numDiscarded > 0);
 assertNull(dest.child(0));
 }

 @Test
 public void testCreateSafeElement() {
 Element source = new Element("div", null);
 source.attr("id", "testId");
 source.attr("style", "color: red; background-color: blue;");
 Element result = cleaner.createSafeElement(source);
 assertNotNull(result);
 assertTrue(result.tagName().equals("div"));
 assertTrue(result.attr("id").equals("testId"));
 assertNull(result.attr("style"));
 }

 @Test
 public void testIsSafeTag() {
 assertTrue(cleaner.whitelist.isSafeTag("div"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 assertTrue(cleaner.whitelist.isSafeAttribute("div", null, "class"));
 assertFalse(cleaner.whitelist.isSafeAttribute("div", null, "script"));
 }

 @Test
 public void testGetEnforcedAttributes() {
 String[] expected = new String[]{"class"};
 Assert.assertArrayEquals(expected, cleaner.whitelist.getEnforcedAttributes("div"));
 }

}