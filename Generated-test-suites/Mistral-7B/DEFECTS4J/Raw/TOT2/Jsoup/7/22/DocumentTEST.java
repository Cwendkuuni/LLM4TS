import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DocumentTest {

 // Test for constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("head", html.tagName());
 }

 // Test for head method
 @Test
 public void testHeadMethod() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 // Test for body method
 @Test
 public void testBodyMethod() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 // Test for title method
 @Test
 public void testTitleMethod() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 // Test for title method with existing title
 @Test
 public void testTitleMethodWithExistingTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Original Title");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 // Test for createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 // Test for normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>\n</title>\n</head>\n<body>\n<textarea>\nTest Text\n</textarea>\n</body>\n</html>");
 doc.normalise();
 Element body = doc.body();
 assertTrue(body.text().contains("Test Text"));
 }

 // Test for findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagNameMethod() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>");
 Element head = doc.findFirstElementByTagName("head", doc);
 Element title = head.getElementsByTagName("title").first();
 assertEquals("Test Title", title.text());
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>");
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", doc.outerHtml());
 }

 // Test for text method
 @Test(expected = IllegalArgumentException.class)
 public void testTextMethodNullText() {
 Document doc = new Document("http://example.com");
 doc.text(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 // Test for text method with empty string
 @Test
 public void testTextMethodEmptyString() {
 Document doc = new Document("http://example.com");
 doc.text("");
 assertEquals("", doc.text().toString());
 }

 // Test for text method with non-empty string
 @Test
 public void testTextMethodNonEmptyString() {
 Document doc = new Document("http://example.com");
 doc.text("Testing");
 assertEquals("Testing", doc.text().toString());
 }

 // Test for nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("#document");
 assertEquals("#document", doc.nodeName());
 }
}