import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 private Document document;

 @Before
 public void setup() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testDocumentCreation() {
 assertNotNull(document);
 assertEquals("http://example.com", document.baseUri());
 assertNull(document.head());
 assertNull(document.body());
 }

 @Test
 public void testHeadAndBodyAccessors() {
 Element head = document.head();
 Element body = document.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testTitleAccessors() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleSettingNull() {
 document.title(null);
 assertNull(document.title());
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("div");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 document.title("Test Title");
 document.text("Test text");
 Element div = document.createElement("div");
 div.text("Div text");
 document.body().appendElement(div);
 document.normalise();
 assertEquals("Test Title\nTest text\n<div>\nDiv text\n</div>", document.outerHtml());
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 assertNull(outputSettings.encoder());
 assertTrue(outputSettings.prettyPrint());
 assertEquals(1, outputSettings.indentAmount());
 }

 @Test
 public void testOutputSettingsEscapeMode() {
 document.outputSettings().escapeMode(Entities.EscapeMode.extended);
 assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
 }

 @Test
 public void testOutputSettingsCharset() {
 document.outputSettings().charset("UTF-16");
 assertEquals("UTF-16", document.outputSettings().charset().name());
 }

 @Test
 public void testOutputSettingsPrettyPrint() {
 document.outputSettings().prettyPrint(false);
 assertFalse(document.outputSettings().prettyPrint());
 }
}