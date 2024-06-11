import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 assertNotNull(html);
 assertEquals("head", html.tagName());
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
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
 doc.title("");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendText(" Test ");
 doc.appendText(" Text ");
 doc.normalise();
 Element body = doc.body();
 assertNotNull(body);
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals(" Test Text ", textNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTitleNull() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("http://example.com");
 doc.text("Test Text");
 assertEquals("Test Text", doc.text());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testClone() {
 Document doc = new Document("http://example.com");
 Document clone = doc.clone();
 assertNotNull(clone);
 assertEquals("#document", clone.nodeName());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertNotNull(outputSettings);
 assertFalse(outputSettings.prettyPrint());
 }

 @Test
 public void testOutputSettingsPrettyPrintTrue() {
 Document doc = new Document("http://example.com");
 doc.outputSettings(new OutputSettings().prettyPrint(true));
 assertTrue(doc.outputSettings().prettyPrint());
 }
}