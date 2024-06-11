import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Jsoup.parse("<html><body><div id='test' onclick='alert(\"XSS\")'>Test<script>alert('XSS');</script></div></body></html>");
 }

 @Test
 public void testCleanMethod() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><div id='test'>Test</div></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethod() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesMethod_SafeTag() {
 Element sourceEl = dirtyDocument.body().createElement("p");
 sourceEl.text("Safe tag test");

 Element dest = new Element("p");
 int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
 assertEquals("Safe tag test", dest.text());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethod_UnsafeTag() {
 Element sourceEl = dirtyDocument.body().createElement("script");
 sourceEl.text("alert('XSS')");

 Element dest = new Element("p");
 int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
 assertEquals(1, numDiscarded);
 assertNull(dest.childNodes().get(0));
 }

 @Test
 public void testCreateSafeElementMethod() {
 Element sourceEl = dirtyDocument.body().createElement("p");
 sourceEl.setAttribute("id", "test");
 sourceEl.setAttribute("onclick", "alert(\"XSS\")");
 sourceEl.text("Test");

 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 Element dest = meta.el;
 assertEquals("p", dest.tagName());
 assertEquals(2, meta.numAttribsDiscarded);
 assertNull(dest.attributes().get("onclick"));
 }
}