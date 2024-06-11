import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String HTML_VALID = "<html><body><h1>Valid HTML</h1></body></html>";
 private static final String HTML_INVALID_TAG = "<html><body><script>alert('XSS');</script></body></html>";
 private static final String HTML_INVALID_ATTRIBUTE = "<html><body><a href=\"javascript:alert('XSS');\" id=\"xss\">Click me</a></body></html>";

 @Test
 public void testCleanValidHtml() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_VALID);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 Assert.assertEquals(1, root.children().size());
 Assert.assertEquals("h1", root.tagName());
 }

 @Test
 public void testCleanInvalidTag() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_INVALID_TAG);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 }

 @Test
 public void testCleanInvalidAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_INVALID_ATTRIBUTE);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 Assert.assertEquals(1, root.children().size());
 Assert.assertEquals("a", root.tagName());
 Assert.assertFalse(root.hasAttr("id"));
 Assert.assertFalse(root.hasAttr("href"));
 }

 @Test
 public void testIsValidValidHtml() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_VALID);

 Assert.assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidHtml() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_INVALID_TAG);

 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_INVALID_ATTRIBUTE);

 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesValidHtml() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_VALID);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 int numChildren = root.childNodes().size();

 Cleaner cleaner2 = new Cleaner(Whitelist.empty());
 Document dirtyDocument2 = Jsoup.parse(HTML_VALID);
 Document cleanDocument2 = cleaner2.clean(dirtyDocument2);

 Element root2 = cleanDocument2.body();
 int numChildren2 = root2.childNodes().size();

 Assert.assertEquals(numChildren, numChildren2);
 }

 @Test
 public void testCopySafeNodesInvalidTag() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_INVALID_TAG);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 int numChildren = root.childNodes().size();

 Assert.assertEquals(0, numChildren);
 }

 @Test
 public void testCopySafeNodesInvalidAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_INVALID_ATTRIBUTE);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 int numChildren = root.childNodes().size();

 Assert.assertEquals(1, numChildren);
 }
}