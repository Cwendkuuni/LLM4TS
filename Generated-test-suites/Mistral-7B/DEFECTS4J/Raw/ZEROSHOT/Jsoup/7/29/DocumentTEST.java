import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document document = new Document("http://example.com");
 Assert.assertNotNull(document);
 Assert.assertEquals("#document", document.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 Element html = document.head();
 Assert.assertNotNull(html);
 Assert.assertEquals("html", html.nodeName());

 Element head = html.head();
 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.nodeName());

 Element body = html.body();
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.nodeName());
 }

 @Test
 public void testTitleAccessors() {
 Document document = new Document("http://example.com");
 document.title("Test Title");
 Assert.assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Document document = new Document("http://example.com");
 Element p = document.createElement("p");
 Assert.assertNotNull(p);
 Assert.assertEquals("p", p.nodeName());
 }

 @Test
 public void testNormalise() {
 Document document = new Document("http://example.com");
 document.body().appendText("Test 1 ");
 document.head().appendElement("title").appendText("Test Title");
 document.text("Test 2 ");

 document.normalise();

 Element body = document.body();
 TextNode titleText = (TextNode) document.getElementsByTag("title").first().child(0);
 Assert.assertEquals("Test Title", titleText.text());
 TextNode bodyText1 = (TextNode) body.child(0);
 TextNode bodyText2 = (TextNode) body.child(1);
 Assert.assertEquals("Test 1 ", bodyText1.text());
 Assert.assertEquals("Test 2 ", bodyText2.text());
 }

 @Test
 public void testSetOutputSettings() {
 Document document = new Document("http://example.com");
 OutputSettings outputSettings = document.outputSettings();
 OutputSettings newSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
 document.outputSettings(newSettings);
 Assert.assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
 }
}