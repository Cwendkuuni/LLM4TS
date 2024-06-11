import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document("");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("");
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 Element body = html.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testAccessors() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 Element head = document.head();
 assertNotNull(head);
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormalise() {
 document.title("Test Title 1");
 document.head().appendElement("title").text("Test Title 2");
 document.body().text("Test Body Text");
 document.normalise();
 assertEquals("Test Title 1", document.title());
 assertEquals("Test Body Text", document.body().text());
 assertEquals(0, document.getElementsByTag("title").size());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 document.createElement("&");
 }

 @Test
 public void testGetFirstElementByTagName() {
 document.title("Test Title");
 Element title = document.findFirstElementByTagName("title", this);
 assertNotNull(title);
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testOuterHtml() {
 document.title("Test Title");
 assertEquals("<html><head></head><body><title>Test Title</title></body></html>", document.outerHtml());
 }

 @Test
 public void testText() {
 document.title("Test Title");
 document.body().text("Test Body Text");
 assertEquals("Test Title\nTest Body Text", document.text());
 }

 @Test
 public void testNodeName() {
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testClone() {
 Document original = new Document("");
 Document clone = original.clone();
 assertNotNull(clone);
 assertEquals(original.outputSettings(), clone.outputSettings());
 }

 @Test
 public void testOutputSettings() {
 Document document = new Document("");
 OutputSettings outputSettings = new OutputSettings();
 document.outputSettings(outputSettings);
 OutputSettings clonedSettings = document.outputSettings();
 assertEquals(outputSettings, clonedSettings);
 }

 @Test
 public void testQuirksMode() {
 Document document = new Document("");
 document.quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, document.quirksMode());
 }
}