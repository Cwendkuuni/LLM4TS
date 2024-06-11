import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testClean() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><div>Hello World</div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><div>Hello World</div></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValid_ValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValid_InvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><div>Hello World</div></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodes_SafeTag() {
 Element sourceEl = Jsoup.parse("<div id='test' class='test' title='test title'><p>Safe tag</p></div>").body().getElementsByTag("div").first();
 Element dest = new Element(Tag.DIV, sourceEl.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
 assertEquals(0, numDiscarded);
 assertEquals("<div class='test' title='test title'><p>Safe tag</p></div>", dest.html());
 }

 @Test
 public void testCopySafeNodes_InvalidTag() {
 Element sourceEl = Jsoup.parse("<html><body><script>alert('XSS')</script><div>Hello World</div></body></html>").body().getElementsByTag("script").first();
 int numDiscarded = cleaner.copySafeNodes(sourceEl, new Element(Tag.DIV, sourceEl.baseUri()));
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodes_TextNode() {
 Element sourceEl = Jsoup.parse("<html><body><script>alert('XSS')</script><div>Hello World <script>alert('XSS')</script></div></body></html>").body().getElementsByTag("div").first();
 Element dest = new Element(Tag.DIV, sourceEl.baseUri());
 int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);
 assertEquals(2, numDiscarded);
 assertEquals("<div>Hello World </div>", dest.html());
 }

}