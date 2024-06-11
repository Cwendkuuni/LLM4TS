import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.nodeName());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
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
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("p");
 assertNotNull(element);
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
 "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
 "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
 "</head>\n" +
 "<body>\n" +
 "<p>Test</p>\n" +
 "<script src=\"script.js\"></script>\n" +
 "</body>\n" +
 "</html>");
 doc.normalise();
 List<Node> bodyChildren = doc.body().childNodes();
 assertEquals(3, bodyChildren.size());
 for (Node node : bodyChildren) {
 if (node instanceof Element) {
 assertEquals("p", node.nodeName());
 } else if (node instanceof TextNode) {
 assertEquals("Test", ((TextNode) node).text().trim());
 }
 }
}

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testInvalidBaseUri() {
 Document doc = new Document("javascript:alert('Hello')");
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testTitleWithEmptyString() {
 Document doc = new Document("http://example.com");
 doc.title("");
 assertEquals("", doc.title());
 }

 @Test
 public void testTitleWithNullString() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 assertNull(doc.title());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals("UTF-8", settings.charset().name());
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = new OutputSettings();
 settings.escapeMode(Entities.EscapeMode.extended);
 doc.outputSettings(settings);
 assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
 }
}