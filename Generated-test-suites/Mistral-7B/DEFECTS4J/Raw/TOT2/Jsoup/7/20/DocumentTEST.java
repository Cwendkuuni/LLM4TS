import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.head();
 Element body = html.body();
 assertNotNull(doc);
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testAccessHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testAccessBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetDocumentTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetDocumentTitle() {
 Document doc = new Document("http://example.com");
 doc.title("");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("");
 doc.appendText(" test ");
 Element body = doc.createElement("body");
 doc.appendChild(body);
 doc.appendChild(new TextNode(" test "));
 doc.normalise();
 Element textInBody = doc.getElementsByTag("body").first().getElementsByTag("text").first();
 assertEquals(" test ", textInBody.text());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameWithNullNode() {
 Document doc = new Document("http://example.com");
 Element nullNode = null;
 Element result = doc.findFirstElementByTagName("html", nullNode);
 }

 @Test
 public void testExtractDocumentTitleWithEmptyTitle() {
 Document doc = new Document("http://example.com");
 String title = doc.title();
 assertEquals("", title);
 }

 @Test
 public void testSetTitleWithNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 String title = doc.title();
 assertEquals("", title);
 }
}