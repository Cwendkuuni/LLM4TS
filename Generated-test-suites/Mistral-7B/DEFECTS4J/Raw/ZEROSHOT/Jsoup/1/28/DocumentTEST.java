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
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.select("html").first();
 assertTrue(html != null);
 Element head = html.getElementsByTag("head").first();
 Element body = html.getElementsByTag("body").first();
 assertTrue(head != null);
 assertTrue(body != null);
 }

 @Test
 public void testGetHeadElement() {
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("#head", head.nodeName());
 }

 @Test
 public void testGetBodyElement() {
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("#body", body.nodeName());
 }

 @Test
 public void testGetTitleElementAndText() {
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitleElementAndText() {
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 doc.title("New Title");
 title = doc.title();
 assertEquals("New Title", title);
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("div");
 assertNotNull(newElement);
 assertEquals("#document", newElement.parent().nodeName());
 }

 @Test
 public void testNormalise() {
 Element div = doc.createElement("div");
 TextNode textNode = doc.createElement("TextNode").text("Test Text");
 div.appendChild(textNode);
 doc.body().appendChild(div);
 doc.normalise();
 Element body = doc.select("body").first();
 TextNode bodyTextNode = body.getElementsByTag("textNode").first();
 assertEquals("Test Text", bodyTextNode.text());
 }

 @Test
 public void testOuterHtml() {
 String html = doc.outerHtml();
 assertTrue(html.contains("<html>"));
 assertTrue(html.contains("<head>"));
 assertTrue(html.contains("<body>"));
 }

 @Test
 public void testText() {
 doc.text("Test Text");
 String text = doc.body().text();
 assertEquals("Test Text", text);
 }
}