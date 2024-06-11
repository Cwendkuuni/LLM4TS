import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://test.com");
 }

 @Test
 public void testCreateDocument() {
 Document newDoc = new Document("http://test.com");
 assertEquals("#document", newDoc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://test.com");
 Element html = doc.getElementsByTag("html").first();
 Element head = html.getElementsByTag("head").first();
 Element body = html.getElementsByTag("body").first();
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testHeadElement() {
 doc.title("Test Title");
 Element head = doc.head();
 Element title = head.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testBodyElement() {
 doc.title("Test Title");
 Element body = doc.body();
 assertTrue(body.text().isEmpty());
 }

 @Test
 public void testTitleElement() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitleElement() {
 doc.title("");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("a");
 assertEquals("a", newElement.tagName());
 }

 @Test
 public void testNormalise() {
 doc.body().text("Test <b>Text</b>");
 doc.normalise();
 Element body = doc.getElementsByTag("body").first();
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test Text", textNode.text());
 }

 @Test
 public void testOuterHtml() {
 doc.body().text("Test <b>Text</b>");
 doc.normalise();
 assertEquals("<html><head><title></title></head><body>Test Text</body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 doc.body().text("Test <b>Text</b>");
 doc.normalise();
 doc.text("New Test Text");
 assertEquals("New Test Text", doc.outerHtml().replaceAll("<[^>]*>", ""));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 doc.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTitle() {
 doc.title("");
 }
}