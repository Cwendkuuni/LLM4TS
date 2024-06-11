import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
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
 Document doc = new Document("");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("");
 doc.title(null);
 assertNull(doc.title());

 doc.title("Test Title");
 doc.title("Test Title 2");
 String title = doc.title();
 assertEquals("Test Title 2", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("");
 Element element = doc.createElement("p");
 assertNotNull(element);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateElementNullTagName() {
 Document doc = new Document("");
 doc.createElement(null);
 }

 @Test
 public void testNormalizeDocument() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 doc.normalize();
 Element head = doc.head();
 Element body = doc.body();
 assertNotNull(head);
 assertNotNull(body);
 assertEquals(1, head.childNodeSize());
 assertEquals(1, body.childNodeSize());
 }

 @Test
 public void testNormalizeTextNodes() {
 Document doc = new Document("<html><head><p>Test</p></head><body></body></html>");
 doc.normalize();
 Element body = doc.body();
 List<Node> textNodes = new ArrayList<>();
 for (Node node : body.childNodes()) {
 if (node instanceof TextNode) {
 textNodes.add(node);
 }
 }
 assertEquals(1, textNodes.size());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 Element head = doc.findFirstElementByTagName("head", doc);
 assertNotNull(head);
 Element body = doc.findFirstElementByTagName("body", doc);
 assertNotNull(body);
 }

 @Test
 public void testTextDocument() {
 Document doc = new Document("<html><head><title>Test</title></head><body></body></html>");
 doc.text("Test Text");
 String text = doc.outerHtml();
 assertEquals("Test Text", text);
 }

 @Test(expected = NullPointerException.class)
 public void testTextNullDocument() {
 Document doc = null;
 doc.text("Test Text");
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document("");
 OutputSettings outputSettings = new OutputSettings();
 doc.outputSettings(outputSettings);
 OutputSettings outputSettingsCopy = doc.outputSettings();
 assertEquals(outputSettings, outputSettingsCopy);
 }

 @Test(expected = NullPointerException.class)
 public void testSetOutputSettingsNullOutputSettings() {
 Document doc = new Document("");
 doc.outputSettings(null);
 }
}