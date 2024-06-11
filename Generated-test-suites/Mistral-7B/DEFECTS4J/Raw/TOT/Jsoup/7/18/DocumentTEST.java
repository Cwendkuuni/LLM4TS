import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertEquals("html", html.tagName());
 Element head = html.head();
 Element body = html.body();
 assertEquals("head", head.tagName());
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetHead() {
 document.title("Test Document");
 assertEquals("Test Document", document.head().text());
 }

 @Test
 public void testGetBody() {
 document.title("Test Document");
 document.body().appendText("Hello World!");
 assertEquals("Hello World!", document.body().text());
 }

 @Test
 public void testGetTitle() {
 document.title("Test Document");
 assertEquals("Test Document", document.title());
 }

 @Test
 public void testSetTitle() {
 document.title("Test Document");
 document.title("New Title");
 assertEquals("New Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element link = document.createElement("a");
 assertEquals("a", link.tagName());
 }

 @Test
 public void testNormalise() {
 Element p = document.createElement("p");
 p.text("Test");
 document.head().appendElement(p);
 document.normalise();
 Element body = document.body();
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test", textNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("invalidTag");
 }
}