import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals(Tag.valueOf("#root"), doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals(Tag.HTML, html.tagName());
 }

 @Test
 public void testHeadAccess() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals(Tag.HEAD, head.tagName());
 }

 @Test
 public void testBodyAccess() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals(Tag.BODY, body.tagName());
 }

 @Test
 public void testTitleAccess() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testTitleSetNull() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 assertEquals("", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertEquals(Tag.P, p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("<html><head></head><body><p>test</p></body></html>");
 doc.normalise();
 Element body = doc.body();
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("test", textNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameNullNode() {
 Document doc = new Document("");
 Element element = findFirstElementByTagName(Docment.class, null);
 assertNull(element);
 }

 @Test
 public void testFindFirstElementByTagNameEmptyDocument() {
 Document doc = new Document("");
 Element element = findFirstElementByTagName(Tag.BODY, doc);
 assertNull(element);
 }

 @Test
 public void testFindFirstElementByTagNameRoot() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 Element element = findFirstElementByTagName(Tag.BODY, doc);
 assertNotNull(element);
 assertEquals(Tag.BODY, element.tagName());
 }

 @Test
 public void testFindFirstElementByTagNameNonExistent() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 Element element = findFirstElementByTagName(Tag.HEADER, doc);
 assertNull(element);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 String html = doc.outerHtml();
 assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", html);
 }

 @Test
 public void testText() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 doc.text("<html><head><title>Test</title></head><body>Replaced</body></html>");
 assertEquals("Replaced", doc.body().text());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testOutputSettingsGetters() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 }

 @Test
 public void testOutputSettingsSetters() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 outputSettings.escapeMode(Entities.EscapeMode.extended);
 outputSettings.charset("UTF-16");
 assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
 assertEquals(Charset.forName("UTF-16"), outputSettings.charset());
 }

 @Test
 public void testOutputSettingsEncoder() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 CharsetEncoder encoder = outputSettings.encoder();
 assertNotNull(encoder);
 }

 @Test
 public void testPrettyPrint() {
 Document doc = new Document("<html><head></head><body><p>Test</p></body></html>");
 OutputSettings outputSettings = doc.outputSettings();
 outputSettings.prettyPrint(false);
 assertEquals("<html><body><p>Test</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testIndentAmount() {
 Document doc = new Document("<html><head></head><body><p>Test</p></body></html>");
 OutputSettings outputSettings = doc.outputSettings();
 outputSettings.indentAmount(2);
 assertEquals("<html>\n<head>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>", doc.outerHtml());
 }

 private Element findFirstElementByTagName(Class<?> clazz, Node node) {
 if (node.getClass().isAssignableFrom(clazz)) {
 return (Element) node;
 }
 for (Node child : node.childNodes()) {
 Element element = findFirstElementByTagName(clazz, child);
 if (element != null) {
 return element;
 }
 }
 return null;
 }
}