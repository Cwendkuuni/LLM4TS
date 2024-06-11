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
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("http://example.com");
 doc.head().text("Test Head");
 doc.body().text("Test Body");
 doc.appendElement("html").text("Test HTML");
 doc.normalise();
 assertEquals("Test HTML<html><head>Test Head</head><body>Test Body</body></html>", doc.outerHtml());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertEquals("html", html.nodeName());
 Element head = doc.findFirstElementByTagName("head", doc);
 assertEquals("head", head.nodeName());
 Element body = doc.findFirstElementByTagName("body", doc);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testTextDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.text().toString());
 doc.text("Test Text");
 assertEquals("Test Text", doc.text().toString());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals("UTF-8", settings.charset().name());
 }
}