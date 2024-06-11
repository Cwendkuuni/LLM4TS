import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 // Test for constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.getFirstElementByTagName("head");
 Element body = html.getFirstElementByTagName("body");

 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 // Test for head method
 @Test
 public void testHeadMethod() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();

 assertNotNull(head);
 }

 // Test for body method
 @Test
 public void testBodyMethod() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();

 assertNotNull(body);
 }

 // Test for title method - get
 @Test
 public void testTitleMethodGet() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();

 assertEquals("Test Title", title);
 }

 // Test for title method - set
 @Test
 public void testTitleMethodSet() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.title("New Title");
 String title = doc.title();

 assertEquals("New Title", title);
 }

 // Test for createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");

 assertNotNull(element);
 }

 // Test for normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("http://example.com");
 doc.outerHtml("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 doc.normalise();

 Element html = doc.head();
 Element head = html.getFirstElementByTagName("head");
 Element body = html.getFirstElementByTagName("body");
 Element title = body.getFirstElementByTagName("title");
 Element p = body.getFirstElementByTagName("p");
 TextNode textNode = (TextNode) title.childNodes().get(0);
 String titleText = textNode.text();

 assertEquals("Test", titleText);
 assertEquals("Test", p.text());
 }

 // Test for edge case: empty constructor
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyConstructor() {
 Document doc = new Document();
 }

 // Test for edge case: null baseUri
 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 // Test for edge case: createShell with null baseUri
 @Test(expected = IllegalArgumentException.class)
 public void testCreateShellNullBaseUri() {
 Document.createShell(null);
 }

 // Test for edge case: head method with null document
 @Test(expected = IllegalArgumentException.class)
 public void testHeadNullDocument() {
 Document doc = new Document("http://example.com");
 Element head = doc.head(null);
 }

 // Test for edge case: body method with null document
 @Test(expected = IllegalArgumentException.class)
 public void testBodyNullDocument() {
 Document doc = new Document("http://example.com");
 Element body = doc.body(null);
 }

 // Test for edge case: title method with null document
 @Test(expected = IllegalArgumentException.class)
 public void testTitleNullDocument() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 // Test for edge case: createElement with null tagName
 @Test(expected = IllegalArgumentException.class)
 public void testCreateElementNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 // Test for edge case: createElement with empty tagName
 @Test(expected = IllegalArgumentException.class)
 public void testCreateElementEmptyTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("");
 }

 // Test for edge case: normalise method with null document
 @Test(expected = IllegalArgumentException.class)
 public void testNormaliseNullDocument() {
 Document doc = new Document("http://example.com");
 doc.normalise(null);
 }
}