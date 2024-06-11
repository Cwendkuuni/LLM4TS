import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetEmptyTitle() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateElementWithNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("").appendText("Test").appendText(" ").appendText("Test2");
 Element html = doc.head();
 html.text("Test Title");
 doc.normalise();
 Element body = doc.body();
 List<Node> children = body.childNodes();
 assertEquals(2, children.size());
 assertEquals("Test", children.get(0).text());
 assertEquals("Test2", children.get(1).text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("").appendElement("html").appendElement("head").appendElement("body");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertEquals("html", html.tagName());
 }

 @Test
 public void testFindFirstElementByTagNameNull() {
 Document doc = new Document("").appendElement("html");
 Element html = doc.findFirstElementByTagName("body", doc);
 assertNull(html);
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 }

 @Test
 public void testOutputSettingsUpdate() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 outputSettings.charset("UTF-16");
 assertEquals("UTF-16", outputSettings.charset().name());
 }
}