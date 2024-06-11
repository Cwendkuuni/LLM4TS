package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.jsoup.helper.ElementHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

 private Document createDocumentWithBaseUri(String baseUri) {
 return new Document(baseUri);
 }

 @Test
 public void testCreateDocument() {
 Document doc = createDocumentWithBaseUri("");
 assertTrue(doc instanceof Document);
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("");
 assertTrue(doc instanceof Document);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadAccess() {
 Document doc = createDocumentWithBaseUri("");
 Element head = doc.head();
 assertNotNull(head);
 assertSame(doc, head.parent());
 }

 @Test
 public void testBodyAccess() {
 Document doc = createDocumentWithBaseUri("");
 Element body = doc.body();
 assertNotNull(body);
 assertSame(doc, body.parent());
 }

 @Test
 public void testTitleAccess() {
 Document doc = createDocumentWithBaseUri("");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleNullParameter() {
 Document doc = createDocumentWithBaseUri("");
 doc.title(null);
 }

 @Test
 public void testTitleSetTextNode() {
 Document doc = createDocumentWithBaseUri("");
 doc.title("");
 Element titleEl = doc.head().appendElement("title");
 titleEl.text("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testTitleSetElement() {
 Document doc = createDocumentWithBaseUri("");
 doc.title("");
 Element titleEl = doc.createElement("title");
 titleEl.text("Test Title");
 doc.head().appendChild(titleEl);
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocumentWithBaseUri("");
 Element p = doc.createElement("p");
 assertTrue(p instanceof Element);
 }

 @Test
 public void testNormalise() {
 Document doc = createDocumentWithBaseUri("");
 doc.text("Test <p>Text</p>");
 doc.normalise();
 Element body = doc.body();
 assertEquals(2, body.childNodes().size());
 Node pNode = body.childNodes().get(0);
 assertTrue(pNode instanceof TextNode);
 assertTrue(pNode.toString().equals("Test "));
 Element pElement = (Element) body.childNodes().get(1);
 assertEquals("p", pElement.nodeName());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = createDocumentWithBaseUri("");
 doc.appendElement("html").appendElement("head").appendElement("body");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertEquals("html", html.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document doc = createDocumentWithBaseUri("");
 doc.text("<html><head></head><body><p>Test</p></body></html>");
 assertEquals(doc.outerHtml(), doc.toString());
 }

 @Test
 public void testText() {
 Document doc = createDocumentWithBaseUri("");
 doc.text("<html><head></head><body><p>Test</p></body></html>");
 assertEquals("Test", doc.text());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testOutputSettingsGetters() {
 Document doc = createDocumentWithBaseUri("");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals(StandardCharsets.UTF_8, settings.charset());
 }

 @Test
 public void testOutputSettingsSetters() {
 Document doc = createDocumentWithBaseUri("");
 OutputSettings settings = doc.outputSettings();
 settings.escapeMode(Entities.EscapeMode.extended);
 settings.charset("UTF-16");
 assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
 assertEquals("UTF-16", settings.charset().name());
 }

 @Test
 public void testPrettyPrint() {
 Document doc = createDocumentWithBaseUri("");
 OutputSettings settings = doc.outputSettings();
 settings.prettyPrint(false);
 assertEquals(false, settings.prettyPrint());
 }

 @Test
 public void testIndentAmount() {
 Document doc = createDocumentWithBaseUri("");
 OutputSettings settings = doc.outputSettings();
 settings.indentAmount(4);
 assertEquals(4, settings.indentAmount());
 }

}