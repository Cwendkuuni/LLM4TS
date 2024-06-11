import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

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
 assertNotNull(doc);
 Element html = doc.select("html").first();
 Element head = html.getElementsByTag("head").first();
 Element body = html.getElementsByTag("body").first();
 assertTrue(head != null);
 assertTrue(body != null);
 }

 @Test
 public void testGetDocumentHead() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testGetDocumentBody() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetDocumentTitle() {
 Document doc = new Document("http://example.com");
 String title = doc.title();
 assertEquals("", title);

 doc.title("Test Title");
 String newTitle = doc.title();
 assertEquals("Test Title", newTitle);
 }

 @Test
 public void testSetDocumentTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 Element title = doc.getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.body().text("Test Body");
 doc.head().appendElement("p").text("Test Head");
 doc.appendElement("p").text("Test Root");

 doc.normalise();

 Element body = doc.body();
 TextNode titleText = (TextNode) doc.getElementsByTag("title").first().childNodes().get(0);
 TextNode rootText = (TextNode) doc.getElementsByTag("p").first().childNodes().get(0);

 assertEquals("Test Title", titleText.text());
 assertEquals("Test Body", body.text().trim());
 assertEquals("Test Root", rootText.text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.body().text("Test Body");
 doc.head().appendElement("p").text("Test Head");
 doc.appendElement("p").text("Test Root");

 String html = doc.outerHtml();
 assertTrue(html.contains("<title>Test Title</title>"));
 assertTrue(html.contains("<body><p></p></body>"));
 assertTrue(html.contains("<p></p>"));
 }

 @Test
 public void testTextDocument() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.body().text("Test Body");
 doc.head().appendElement("p").text("Test Head");
 doc.appendElement("p").text("Test Root");

 doc.text("New Text");

 assertEquals("New Text", doc.outerHtml());
 }
}