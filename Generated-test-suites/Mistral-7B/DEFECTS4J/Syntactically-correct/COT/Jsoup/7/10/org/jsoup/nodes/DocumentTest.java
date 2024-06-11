package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document("http://example.com").normalize();
 }

 // Test constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertEquals("http://example.com", doc.baseUri());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 // Test createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertEquals("http://example.com", doc.baseUri());
 Element html = doc.head();
 assertEquals("html", html.nodeName());
 Element head = html.firstChild();
 assertEquals("head", head.nodeName());
 Element body = html.lastChild();
 assertEquals("body", body.nodeName());
 }

 // Test head method
 @Test
 public void testHeadMethod() {
 Element head = document.head();
 assertSame(document, head.parent());
 assertNotNull(head);
 }

 // Test body method
 @Test
 public void testBodyMethod() {
 Element body = document.body();
 assertSame(document, body.parent());
 assertNull(body);
 }

 // Test title method - get
 @Test
 public void testTitleMethodGet() {
 String title = document.title();
 assertEquals("", title);
 }

 // Test title method - set
 @Test
 public void testTitleMethodSet() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 // Test createElement method
 @Test
 public void testCreateElementMethod() {
 Element element = document.createElement("div");
 assertEquals("div", element.nodeName());
 assertEquals("", element.text());
 assertNull(element.parent());
 }

 // Test normalise method
 @Test
 public void testNormaliseMethod() {
 document.text("Test Text");
 Element div = document.createElement("div");
 div.text("Div Text");
 document.body().appendChild(div);
 document.normalise();
 List<Node> bodyChildren = document.body().childNodes();
 assertEquals(2, bodyChildren.size());
 Node firstChild = bodyChildren.get(0);
 assertEquals(Text.class, firstChild.getClass());
 assertEquals("Test Text", firstChild.text());
 Node secondChild = bodyChildren.get(1);
 assertEquals("div", secondChild.nodeName());
 assertEquals("Div Text", secondChild.text());
 }

 // Test findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagNameMethod() {
 Element html = document.findFirstElementByTagName("html", document);
 assertEquals("html", html.nodeName());
 assertSame(document, html.parent());
 }

 // Test outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 document.title("Test Title");
 document.body().appendElement("p").text("Test Paragraph");
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>", document.outerHtml());
 }

 // Test text method
 @Test
 public void testTextMethod() {
 document.title("Test Title");
 document.body().appendElement("p").text("Test Paragraph");
 document.text("Test Text");
 assertEquals("Test TextTest Title<p>Test Paragraph</p>", document.outerHtml());
 }

 // Test escapeMode property
 @Test
 public void testEscapeModeProperty() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().escapeMode(Entities.EscapeMode.extended);
 assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
 }

 // Test charset property - get
 @Test
 public void testCharsetPropertyGet() {
 Document doc = new Document("http://example.com");
 assertEquals("UTF-8", doc.outputSettings().charset().name());
 }

 // Test charset property - set
 @Test
 public void testCharsetPropertySet() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().charset("ISO-8859-1");
 assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
 }

 // Test charsetEncoder property
 @Test
 public void testCharsetEncoderProperty() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().charset("ISO-8859-1");
 assertEquals("ISO-8859-1", doc.outputSettings().encoder().charset().name());
 }

 // Test prettyPrint property - get
 @Test
 public void testPrettyPrintPropertyGet() {
 Document doc = new Document("http://example.com");
 assertEquals(true, doc.outputSettings().prettyPrint());
 }

 // Test prettyPrint property - set
 @Test
 public void testPrettyPrintPropertySet() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().prettyPrint(false);
 assertEquals(false, doc.outputSettings().prettyPrint());
 }

 // Test indentAmount property - get
 @Test
 public void testIndentAmountPropertyGet() {
 Document doc = new Document("http://example.com");
 assertEquals(1, doc.outputSettings().indentAmount());
 }

 // Test indentAmount property - set
 @Test
 public void testIndentAmountPropertySet() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().indentAmount(2);
 assertEquals(2, doc.outputSettings().indentAmount());
 }

 // Test exception handling - null baseUri
 @Test(expected = IllegalArgumentException.class)
 public void testExceptionHandlingNullBaseUri() {
 new Document(null);
 }

 // Test exception handling - null tagName in createElement method
 @Test(expected = NullPointerException.class)
 public void testExceptionHandlingNullTagNameCreateElement() {
 document.createElement(null);
 }

 // Test exception handling - null tagName in findFirstElementByTagName method
 @Test(expected = NullPointerException.class)
 public void testExceptionHandlingNullTagNameFindFirstElementByTagName() {
 document.findFirstElementByTagName(null, document);
 }

}