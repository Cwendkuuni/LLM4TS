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
 assertEquals("head", html.nodeName());
 Element head = html.head();
 assertEquals("html", head.parentNode().nodeName());
 Element body = doc.body();
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testAccessHeadAndBody() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title 1");
 doc.title("Test Title 2");
 String title = doc.title();
 assertEquals("Test Title 2", title);
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
 doc.text("Test normalise text");
 Element p = doc.createElement("p");
 p.text("Test inner text");
 doc.body().appendChild(p);
 doc.normalise();
 Element body = doc.body();
 TextNode textNode = (TextNode) body.getChildNodes().get(0);
 assertEquals("Test normalise textTest inner text", textNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 Element head = doc.findFirstElementByTagName("head", doc);
 Element body = doc.findFirstElementByTagName("body", doc);
 assertEquals("html", html.nodeName());
 assertEquals("head", head.nodeName());
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testEscapeMode() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
 Element title = doc.createElement("title");
 title.text("<script>alert('Test');</script>");
 doc.head().appendChild(title);
 String titleText = doc.head().title().text();
 assertEquals("&lt;script&gt;alert(&apos;Test&apos;);&lt;/script&gt;", titleText);
 }

 @Test
 public void testCharset() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().charset("ISO-8859-1");
 Element p = doc.createElement("p");
 p.text("Non-UTF-8 character: é");
 doc.body().appendChild(p);
 String pText = doc.body().getText();
 assertEquals("Non-UTF-8 character: é", pText);
 }

 @Test
 public void testPrettyPrint() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().prettyPrint(false);
 Element html = doc.createElement("html");
 html.appendElement("head").appendElement("title").text("Test Title");
 html.appendElement("body").appendElement("p").text("Test Text");
 String htmlText = doc.outerHtml();
 assertTrue(htmlText.startsWith("<html>"));
 assertTrue(htmlText.endsWith("</html>"));
 assertFalse(htmlText.contains("\n"));
 }
}