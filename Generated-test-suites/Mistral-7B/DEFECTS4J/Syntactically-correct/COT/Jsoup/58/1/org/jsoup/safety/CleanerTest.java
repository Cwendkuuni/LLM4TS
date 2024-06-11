import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 dirtyDocument = Jsoup.parse("<html><body><h1 id='title' onclick='alert(\"XSS\")'>Title</h1><script>alert(\"XSS\")</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().text().isEmpty());
 assertNull(cleanDocument.body().getElementsByTag("script").first());
 assertNull(cleanDocument.body().getElementsByTag("h1").get(0).attributes().get("onclick"));
 }

 @Test
 public void testIsValidMethod() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanerConstructorWithNullWhitelist() {
 try {
 new Cleaner(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyInput() {
 Element emptyInput = Jsoup.parse("<html><body></body></html>").body();
 Element output = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(emptyInput, output);
 assertTrue(numDiscarded == 0);
 }

 @Test
 public void testCopySafeNodesMethod() {
 Element input = Jsoup.parse("<html><body><h1 id='title' onclick='alert(\"XSS\")'>Title</h1></body></html>").body();
 Element output = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(input, output);
 assertTrue(numDiscarded > 0);
 }

 @Test
 public void testCopySafeNodesMethodWithAllowedAttributes() {
 Element input = Jsoup.parse("<html><body><p id='title' class='title' style='color: red'>Content</p></body></html>").body();
 Element output = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(input, output);
 assertTrue(numDiscarded == 0);
 Element title = output.getElementsByTag("p").get(0);
 assertTrue(title.attributes().get("id").equals("title"));
 assertTrue(title.attributes().get("class").equals("title"));
 }

 @Test
 public void testCopySafeNodesMethodWithDisallowedAttributes() {
 Element input = Jsoup.parse("<html><body><p id='title' onclick='alert(\"XSS\")'>Content</p></body></html>").body();
 Element output = new Element("body");
 int numDiscarded = cleaner.copySafeNodes(input, output);
 assertTrue(numDiscarded > 0);
 Element title = output.getElementsByTag("p").get(0);
 assertNull(title.attributes().get("onclick"));
 }

 @Test
 public void testCreateSafeElementMethod() {
 Element source = Jsoup.parse("<html><body><p id='title' onclick='alert(\"XSS\")'>Content</p></body></html>").body().getElementsByTag("p").get(0);
 ElementMeta dest = cleaner.createSafeElement(source);
 assertNotNull(dest.el);
 assertFalse(dest.el.attributes().containsKey("onclick"));
 }

}