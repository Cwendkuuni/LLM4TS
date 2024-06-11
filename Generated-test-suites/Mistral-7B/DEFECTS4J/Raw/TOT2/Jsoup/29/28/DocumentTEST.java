import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class DocumentTest {

 // Test constructor with valid baseUri
 @Test
 public void testDocumentConstructorWithValidBaseUri() {
 Document document = new Document("http://example.com");
 assertNotNull(document);
 }

 // Test createShell method with valid baseUri
 @Test
 public void testCreateShellWithValidBaseUri() {
 Document document = Document.createShell("http://example.com");
 assertNotNull(document);
 Element html = document.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = document.body();
 assertNotNull(body);
 }

 // Test head method with empty document
 @Test
 public void testHeadWithEmptyDocument() {
 Document document = new Document("");
 assertNotNull(document);
 Element head = document.head();
 assertNull(head);
 }

 // Test body method with empty document
 @Test
 public void testBodyWithEmptyDocument() {
 Document document = new Document("");
 assertNotNull(document);
 Element body = document.body();
 assertNull(body);
 }

 // Test title method with empty document
 @Test
 public void testTitleWithEmptyDocument() {
 Document document = new Document("");
 assertEmpty(document.title());
 }

 // Test title method with title set
 @Test
 public void testTitleWithTitleSet() {
 Document document = new Document("");
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 // Test createElement method with valid tagName
 @Test
 public void testCreateElementWithValidTagName() {
 Document document = new Document("");
 Element element = document.createElement("p");
 assertNotNull(element);
 }

 // Test normalise method with empty document
 @Test
 public void testNormaliseWithEmptyDocument() {
 Document document = new Document("");
 Document normalisedDocument = document.normalise();
 assertNotNull(normalisedDocument);
 }

 // Test normaliseTextNodes method with empty document
 @Test
 public void testNormaliseTextNodesWithEmptyDocument() {
 Document document = new Document("");
 document.normaliseTextNodes(document);
 }

 // Test normaliseStructure method with multiple <head> elements
 @Test(expected = RuntimeException.class)
 public void testNormaliseStructureWithMultipleHeads() {
 Document document = new Document("");
 Element head1 = document.createElement("head");
 Element head2 = document.createElement("head");
 document.appendChild(head1);
 document.appendChild(head2);
 document.normalise();
 }

 // Test normaliseStructure method with multiple <body> elements
 @Test(expected = RuntimeException.class)
 public void testNormaliseStructureWithMultipleBodies() {
 Document document = new Document("");
 Element body1 = document.createElement("body");
 Element body2 = document.createElement("body");
 document.appendChild(body1);
 document.appendChild(body2);
 document.normalise();
 }

 // Test findFirstElementByTagName method with non-existent tag
 @Test
 public void testFindFirstElementByTagNameWithNonExistentTag() {
 Document document = new Document("");
 assertNull(document.findFirstElementByTagName("nonExistentTag", document));
 }

 // Test outerHtml method with empty document
 @Test
 public void testOuterHtmlWithEmptyDocument() {
 Document document = new Document("");
 assertEquals("<html><head></head><body></body></html>", document.outerHtml());
 }

 // Test text method with empty document
 @Test
 public void testTextWithEmptyDocument() {
 Document document = new Document("");
 document.text("Test Text");
 assertEquals("Test Text", document.text());
 }

 // Test nodeName method with empty document
 @Test
 public void testNodeNameWithEmptyDocument() {
 Document document = new Document("");
 assertEquals("#document", document.nodeName());
 }

 // Test clone method with empty document
 @Test
 public void testCloneWithEmptyDocument() {
 Document document = new Document("");
 Document clonedDocument = document.clone();
 assertNotNull(clonedDocument);
 }

 // Test escapeMode property with base escape mode
 @Test
 public void testEscapeModeWithBaseEscapeMode() {
 Document document = new Document("");
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 }

 // Test escapeMode property with extended escape mode
 @Test
 public void testEscapeModeWithExtendedEscapeMode() {
 Document document = new Document("");
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.escapeMode(Entities.EscapeMode.extended);
 document.outputSettings(outputSettings);
 assertEquals(Entities.EscapeMode.extended, document.outputSettings().escapeMode());
 }

 // Test charset property with UTF-8 charset
 @Test
 public void testCharsetWithUTF8Charset() {
 Document document = new Document("");
 OutputSettings outputSettings = document.outputSettings();
 assertEquals("UTF-8", outputSettings.charset().name());
 }

 // Test charset property with different charset
 @Test
 public void testCharsetWithDifferentCharset() {
 Document document = new Document("");
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.charset("ISO-8859-1");
 document.outputSettings(outputSettings);
 assertEquals("ISO-8859-1", document.outputSettings().charset().name());
 }

 // Test indentAmount property with default indent amount
 @Test
 public void testIndentAmountWithDefaultIndentAmount() {
 Document document = new Document("");
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(1, outputSettings.indentAmount());
 }

 // Test indentAmount property with custom indent amount
 @Test
 public void testIndentAmountWithCustomIndentAmount() {
 Document document = new Document("");
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.indentAmount(2);
 document.outputSettings(outputSettings);
 assertEquals(2, document.outputSettings().indentAmount());
 }
}