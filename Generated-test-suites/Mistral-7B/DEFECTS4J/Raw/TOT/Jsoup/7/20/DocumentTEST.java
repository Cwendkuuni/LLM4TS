import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("head", html.tagName());
 }

 @Test
 public void testGetHeadAndBody() {
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
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("<html><head></head><body><div>Test</div></body></html>");
 doc.normalise();
 assertEquals("Test", doc.head().text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><div>Test</div></body></html>");
 Element title = doc.findFirstElementByTagName("title", doc);
 assertNotNull(title);
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><head></head><body><div>Test</div></body></html>");
 assertEquals("<html><head></head><body><div>Test</div></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("<html><head></head><body></body></html>");
 doc.text("Test");
 assertEquals("Test", doc.outerHtml());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("#document");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertNotNull(settings.charset());
 assertNotNull(settings.encoder());
 assertTrue(settings.prettyPrint());
 assertEquals(1, settings.indentAmount());
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
 }
}