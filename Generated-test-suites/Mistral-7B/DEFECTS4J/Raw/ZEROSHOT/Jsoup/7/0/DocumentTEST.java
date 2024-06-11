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
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("#html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("#head", head.nodeName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("#body", body.nodeName());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("#head", head.nodeName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("#body", body.nodeName());
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
 doc.title("Test Title");
 String oldTitle = doc.title();
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 assertEquals("Test Title", oldTitle);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("#p", p.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("http://example.com");
 doc.head().appendText("Test Head Text");
 doc.appendText("Test Document Text");
 doc.body().appendText("Test Body Text");
 doc.normalise();
 Element body = doc.body();
 List<Node> children = body.children();
 assertEquals(3, children.size());
 assertTrue(children.get(0) instanceof TextNode);
 TextNode headText = (TextNode) doc.head().firstChild();
 assertEquals("Test Head Text", headText.text());
 TextNode documentText = (TextNode) children.get(1);
 assertEquals("Test Document Text", documentText.text());
 TextNode bodyText = (TextNode) children.get(2);
 assertEquals("", bodyText.text());
 }

 @Test (expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertNotNull(outputSettings);
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 }

 @Test
 public void testSetOutputSettingsEscapeMode() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings newSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
 assertEquals(Entities.EscapeMode.extended, newSettings.escapeMode());
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 }

 @Test
 public void testSetOutputSettingsCharset() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings newSettings = outputSettings.charset(Charset.forName("UTF-16"));
 assertNotEquals(Charset.forName("UTF-8"), newSettings.charset());
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 }

 @Test
 public void testSetOutputSettingsPrettyPrint() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings newSettings = outputSettings.prettyPrint(false);
 assertFalse(newSettings.prettyPrint());
 assertTrue(outputSettings.prettyPrint());
 }

 @Test
 public void testSetOutputSettingsIndentAmount() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings newSettings = outputSettings.indentAmount(5);
 assertEquals(5, newSettings.indentAmount());
 assertEquals(1, outputSettings.indentAmount());
 }
}