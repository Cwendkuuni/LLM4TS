import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document document = new Document("http://example.com");
 assertNotNull(document);
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testGetHeadElement() {
 Document document = new Document("http://example.com");
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Document document = new Document("http://example.com");
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document document = new Document("http://example.com");
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document document = new Document("http://example.com");
 document.title("");
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document document = new Document("http://example.com");
 Element element = document.createElement("div");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document document = new Document("http://example.com");
 document.title("Test Title");
 document.head().appendElement("p").text("Test Paragraph");
 document.text("Test Text");
 document.normalise();
 assertNotNull(document.head());
 assertEquals(1, document.head().childNodeSize());
 assertEquals("Test Title", document.head().text().trim());
 assertEquals("Test Text", document.body().text().trim());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document document = new Document("http://example.com");
 Element html = document.findFirstElementByTagName("html", document);
 assertNotNull(html);
 assertEquals("html", html.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document document = new Document("http://example.com");
 document.title("Test Title");
 document.head().appendElement("p").text("Test Paragraph");
 document.text("Test Text");
 document.normalise();
 assertEquals("<html><head></head><body>Test Text</body></html>", document.outerHtml());
 }

 @Test
 public void testText() {
 Document document = new Document("http://example.com");
 document.body().appendText("Test Text");
 String text = document.body().text();
 assertEquals("Test Text", text);
 }

 @Test
 public void testNodeName() {
 Document document = new Document("http://example.com");
 assertEquals("#document", document.nodeName());
 }
}