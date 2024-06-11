import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertTrue(doc instanceof Document);
 assertEquals("http://example.com", doc.baseUri());
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertTrue(doc instanceof Document);
 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testAccessHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertTrue(head instanceof Element);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testAccessBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertTrue(body instanceof Element);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetDocumentTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetDocumentTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element div = doc.createElement("div");
 assertTrue(div instanceof Element);
 assertEquals("div", div.tagName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 doc.normalise();
 assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("&");
 }

 @Test
 public void testTitleWithEmptyString() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 Element head = doc.findFirstElementByTagName("head", doc);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testCreateShellWithEmptyHeadAndBody() {
 Document doc = Document.createShell("http://example.com");
 doc.head().remove();
 doc.body().remove();
 assertEquals("<html></html>", doc.outerHtml());
 }

 @Test
 public void testTitleWithNullString() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 assertEquals("", doc.title());
 }

 @Test
 public void testNormaliseTextNodesWithEmptyElement() {
 Document doc = new Document("");
 Element emptyElement = doc.createElement("empty");
 doc.appendChild(emptyElement);
 doc.normalise();
 assertEquals("", emptyElement.outerHtml());
 }

 @Test
 public void testNullDocumentWhenCallingFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element head = doc.findFirstElementByTagName("head", null);
 assertNull(head);
 }

 @Test
 public void testCreateDocumentWithEmptyStringBaseUri() {
 Document doc = new Document("");
 assertTrue(doc instanceof Document);
 assertEquals("", doc.baseUri());
 }

 @Test
 public void testCreateShellWithEmptyStringBaseUri() {
 Document doc = Document.createShell("");
 assertTrue(doc instanceof Document);
 assertEquals("", doc.baseUri());
 }

 @Test
 public void testCreateElementWithEmptyStringTagName() {
 Document doc = new Document("http://example.com");
 Element div = doc.createElement("");
 assertEquals("", div.tagName());
 }

 @Test
 public void testNormaliseTextNodesWithNullElement() {
 Document doc = new Document("http://example.com");
 Node node = doc.createTextNode("Test");
 doc.normaliseTextNodes(null);
 assertEquals("Test", node.toString());
 }
}