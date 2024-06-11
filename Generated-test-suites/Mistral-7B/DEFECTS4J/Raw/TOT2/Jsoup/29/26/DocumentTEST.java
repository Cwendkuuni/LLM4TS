import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 // Test for constructor with baseUri
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("#document", html.nodeName());
 }

 // Test for head and body accessor methods
 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();

 assertNotNull(head);
 assertNotNull(body);
 assertEquals("#document", head.parentNode().nodeName());
 assertEquals("#document", body.parentNode().nodeName());
 }

 // Test for title method
 @Test
 public void testTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 // Test for title method with null input
 @Test(expected = NullPointerException.class)
 public void testTitleNullInput() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 // Test for createElement method
 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertEquals("p", p.nodeName());
 }

 // Test for normalise method
 @Test
 public void testNormalise() {
 Document doc = new Document("<html><head></head><body><p>Test</p><body></html>");
 doc.normalise();
 assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.outerHtml());
 }

 // Test for findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("<html><body><h1>Test</h1></body></html>");
 Element h1 = doc.findFirstElementByTagName("h1");
 assertEquals("h1", h1.nodeName());
 }

 // Test for findFirstElementByTagName method with null input
 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameNullInput() {
 Document doc = new Document("<html></html>");
 doc.findFirstElementByTagName(null);
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtml() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", doc.outerHtml());
 }

 // Test for text method
 @Test
 public void testText() {
 Document doc = new Document("<html><body><p>Test</p></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.body().text());
 }

 // Test for nodeName method
 @Test
 public void testNodeName() {
 Document doc = new Document("#document");
 assertEquals("#document", doc.nodeName());
 }

 // Test for clone method
 @Test
 public void testClone() {
 Document doc = new Document("<html><head></head><body></body></html>");
 Document clone = doc.clone();
 assertNotSame(doc, clone);
 }

 // Test for OutputSettings class
 @Test
 public void testOutputSettings() {
 Document.OutputSettings settings = new Document.OutputSettings();
 assertEquals(Document.Entities.EscapeMode.base, settings.escapeMode());
 assertEquals("UTF-8", settings.charset().name());
 assertEquals(true, settings.prettyPrint());
 }

 // Test for OutputSettings escapeMode method
 @Test
 public void testOutputSettingsEscapeMode() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.escapeMode(Document.Entities.EscapeMode.extended);
 assertEquals(Document.Entities.EscapeMode.extended, settings.escapeMode());
 }

 // Test for OutputSettings charset method
 @Test
 public void testOutputSettingsCharset() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.charset("UTF-16LE");
 assertEquals("UTF-16LE", settings.charset().name());
 }

 // Test for OutputSettings charset method with null input
 @Test(expected = NullPointerException.class)
 public void testOutputSettingsCharsetNullInput() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.charset(null);
 }

 // Test for OutputSettings encoder method
 @Test
 public void testOutputSettingsEncoder() {
 Document.OutputSettings settings = new Document.OutputSettings();
 assertEquals("UTF-8", settings.encoder().charset().name());
 }

 // Test for OutputSettings prettyPrint method
 @Test
 public void testOutputSettingsPrettyPrint() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.prettyPrint(false);
 assertFalse(settings.prettyPrint());
 }

 // Test for OutputSettings indentAmount method
 @Test
 public void testOutputSettingsIndentAmount() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.indentAmount(4);
 assertEquals(4, settings.indentAmount());
 }

 // Test for OutputSettings clone method
 @Test
 public void testOutputSettingsClone() {
 Document.OutputSettings settings = new Document.OutputSettings();
 Document.OutputSettings clone = settings.clone();
 assertNotSame(settings, clone);
 }
}