package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 /**
 * Test constructor with valid baseUri.
 */
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertTrue(doc instanceof Document);
 assertEquals("http://example.com", doc.baseUri());
 }

 /**
 * Test createShell method with valid baseUri.
 */
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertTrue(doc instanceof Document);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 /**
 * Test head method.
 */
 @Test
 public void testHeadMethod() {
 Element head = document.head();
 assertNotNull(head);
 }

 /**
 * Test body method.
 */
 @Test
 public void testBodyMethod() {
 Element body = document.body();
 assertNotNull(body);
 }

 /**
 * Test title method.
 */
 @Test
 public void testTitleMethod() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 /**
 * Test title method with null title.
 */
 @Test(expected = NullPointerException.class)
 public void testTitleMethodWithNullTitle() {
 document.title(null);
 }

 /**
 * Test createElement method with valid tagName.
 */
 @Test
 public void testCreateElement() {
 Element element = document.createElement("a");
 assertTrue(element instanceof Element);
 }

 /**
 * Test normalise method.
 */
 @Test
 public void testNormalise() {
 String textBeforeNormalise = document.text();
 document.title("Test Title");
 document.appendText("Test text");
 document.normalise();
 assertEquals("Test Title\nTest text", document.text());
 }

 /**
 * Test findFirstElementByTagName method with valid tag and non-null node.
 */
 @Test
 public void testFindFirstElementByTagNameWithValidTagAndNonNullNode() {
 Element head = document.head();
 Element html = document.findFirstElementByTagName("html", head);
 assertNotNull(html);
 }

 /**
 * Test findFirstElementByTagName method with valid tag and null node.
 */
 @Test
 public void testFindFirstElementByTagNameWithValidTagAndNullNode() {
 assertNull(document.findFirstElementByTagName("html", null));
 }

 /**
 * Test findFirstElementByTagName method with invalid tag.
 */
 @Test
 public void testFindFirstElementByTagNameWithInvalidTag() {
 assertNull(document.findFirstElementByTagName("z", null));
 }

 /**
 * Test outerHtml method.
 */
 @Test
 public void testOuterHtml() {
 document.appendText("Test text");
 assertEquals("<html><head></head><body>Test text</body></html>", document.outerHtml());
 }

 /**
 * Test text method with null text.
 */
 @Test(expected = NullPointerException.class)
 public void testTextMethodWithNullText() {
 document.text(null);
 }

 /**
 * Test nodeName method.
 */
 @Test
 public void testNodeNameMethod() {
 assertEquals("#document", document.nodeName());
 }

 /**
 * Test escapeMode property.
 */
 @Test
 public void testOutputSettingsEscapeMode() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 OutputSettings outputSettingsWithExtendedEscapeMode = outputSettings.escapeMode(Entities.EscapeMode.extended);
 assertEquals(Entities.EscapeMode.extended, outputSettingsWithExtendedEscapeMode.escapeMode());
 }

 /**
 * Test charset property.
 */
 @Test
 public void testOutputSettingsCharset() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals("UTF-8", outputSettings.charset().name());
 OutputSettings outputSettingsWithCustomCharset = outputSettings.charset("ISO-8859-1");
 assertEquals("ISO-8859-1", outputSettingsWithCustomCharset.charset().name());
 }

 /**
 * Test charsetEncoder property.
 */
 @Test
 public void testOutputSettingsCharsetEncoder() {
 OutputSettings outputSettings = document.outputSettings();
 CharsetEncoder charsetEncoder = outputSettings.encoder();
 assertNotNull(charsetEncoder);
 }

 /**
 * Test prettyPrint property.
 */
 @Test
 public void testOutputSettingsPrettyPrint() {
 OutputSettings outputSettings = document.outputSettings();
 assertTrue(outputSettings.prettyPrint());
 OutputSettings outputSettingsWithPrettyPrintDisabled = outputSettings.prettyPrint(false);
 assertFalse(outputSettingsWithPrettyPrintDisabled.prettyPrint());
 }

 /**
 * Test indentAmount property.
 */
 @Test
 public void testOutputSettingsIndentAmount() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(1, outputSettings.indentAmount());
 OutputSettings outputSettingsWithCustomIndentAmount = outputSettings.indentAmount(4);
 assertEquals(4, outputSettingsWithCustomIndentAmount.indentAmount());
 }

 /**
 * Test normaliseTextNodes method with empty list.
 */
 @Test
 public void testNormaliseTextNodesWithEmptyList() {
 Element element = new Element("div");
 List<Node> emptyList = new ArrayList<>();
 normaliseTextNodes(element, emptyList);
 assertEquals(0, element.childNodes().size());
 }

 /**
 * Test normaliseTextNodes method with non-empty list.
 */
 @Test
 public void testNormaliseTextNodesWithNonEmptyList() {
 Element element = new Element("div");
 TextNode textNode1 = new TextNode("Test text 1");
 TextNode textNode2 = new TextNode("Test text 2");
 List<Node> list = new ArrayList<>();
 list.add(textNode1);
 list.add(textNode2);
 normaliseTextNodes(element, list);
 assertEquals(1, element.childNodes().size());
 Element textElement = (Element) element.childNodes().get(0);
 assertEquals(2, textElement.childNodes().size());
 assertEquals("Test text 1Test text 2", textElement.text());
 }

 /**
 * Test findFirstElementByTagName method with empty document.
 */
 @Test
 public void testFindFirstElementByTagNameWithEmptyDocument() {
 assertNull(document.findFirstElementByTagName("html", null));
 }

 /**
 * Test findFirstElementByTagName method with invalid tag and empty document.
 */
 @Test
 public void testFindFirstElementByTagNameWithInvalidTagAndEmptyDocument() {
 assertNull(document.findFirstElementByTagName("z", null));
 }

 /**
 * Test findFirstElementByTagName method with null tag and non-empty document.
 */
 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameWithNullTagAndNonEmptyDocument() {
 document.appendElement("html");
 document.appendElement("head");
 document.appendElement("body");
 document.appendElement("a");
 assertNull(document.findFirstElementByTagName(null, null));
 }

 /**
 * Test findFirstElementByTagName method with null tag and empty document.
 */
 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameWithNullTagAndEmptyDocument() {
 assertNull(document.findFirstElementByTagName(null, null));
 }

 /**
 * Test findFirstElementByTagName method with invalid tag and non-empty document.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testFindFirstElementByTagNameWithInvalidTagAndNonEmptyDocument() {
 document.appendElement("html");
 document.appendElement("head");
 document.appendElement("body");
 document.appendElement("a");
 assertNull(document.findFirstElementByTagName("z", null));
 }

 /**
 * Test normaliseTextNodes method with null list.
 */
 @Test(expected = NullPointerException.class)
 public void testNormaliseTextNodesWithNullList() {
 Element element = new Element("div");
 normaliseTextNodes(element, null);
 fail("NormaliseTextNodes method should throw NullPointerException when list is null");
 }
}