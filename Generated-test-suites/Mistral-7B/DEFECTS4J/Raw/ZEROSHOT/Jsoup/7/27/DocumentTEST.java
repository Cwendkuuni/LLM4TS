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
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
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
 public void testGetHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("JSoup Test");
 String title = doc.title();
 assertEquals("JSoup Test", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("");
 doc.title("JSoup Test");
 String title = doc.title();
 assertEquals("JSoup Test", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Hello World");
 Element p = doc.createElement("p");
 p.text("This is a test.");
 doc.appendChild(p);
 doc.normalise();
 assertEquals("Hello WorldThis is a test.", doc.text().trim());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertNotNull(html);
 Element head = doc.findFirstElementByTagName("head", doc);
 assertNotNull(head);
 Element body = doc.findFirstElementByTagName("body", doc);
 assertNotNull(body);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.title("JSoup Test");
 doc.text("Hello World");
 Element p = doc.createElement("p");
 p.text("This is a test.");
 doc.appendChild(p);
 doc.normalise();
 String html = doc.outerHtml();
 assertTrue(html.contains("<html><head><title>JSoup Test</title></head><body>Hello WorldThis is a test.</body></html>"));
 }

 @Test
 public void testText() {
 Document doc = new Document("http://example.com");
 doc.text("Hello World");
 String text = doc.text();
 assertEquals("Hello World", text);
 }
}