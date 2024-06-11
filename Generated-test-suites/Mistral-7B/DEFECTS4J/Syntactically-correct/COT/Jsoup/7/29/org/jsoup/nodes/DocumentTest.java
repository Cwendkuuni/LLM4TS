package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 assertNotNull(html.tagName());
 assertEquals(Tag.HTML, html.tagName());
 }

 @Test
 public void testHeadAccess() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(head);
 assertNull(doc.title());
 }

 @Test
 public void testBodyAccess() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertNotNull(body);
 assertNull(body.text());
 }

 @Test
 public void testTitleAccess() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testTitleSet() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 doc.title("New Test Title");
 assertEquals("New Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("a");
 assertNotNull(element);
 assertNotNull(element.nodeName());
 assertEquals("a", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendText("test");
 Element body = doc.body();
 body.appendText(" test");
 body.appendChild(doc.createElement("a"));
 body.appendText(" test");
 doc.normalise();
 assertEquals(3, body.childNodes().size());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameNullNode() {
 Document doc = new Document("baseUri");
 findFirstElementByTagName("head", null);
 }

 @Test
 public void testFindFirstElementByTagNameInvalidTag() {
 Document doc = new Document("baseUri");
 assertNull(findFirstElementByTagName("invalidTag", doc));
 }

 @Test
 public void testFindFirstElementByTagNameValidTag() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(findFirstElementByTagName("head", doc));
 assertEquals(head, findFirstElementByTagName("head", doc));
 }

 @Test
 public void testHtmlNodeName() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testTextNodeWithText() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 TextNode textNode = new TextNode("Test Text", StandardCharsets.UTF_8);
 body.prependChild(textNode);
 assertEquals("Test Text", body.text());
 }

 @Test
 public void testTextNodeWithoutText() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 TextNode textNode = new TextNode("", StandardCharsets.UTF_8);
 body.prependChild(textNode);
 assertEquals("", body.text());
 }

 @Test
 public void testOutputSettingsEscapeMode() {
 Document doc = new Document("baseUri");
 OutputSettings settings = doc.outputSettings();
 settings.escapeMode(Entities.EscapeMode.extended);
 settings.escapeMode(Entities.EscapeMode.base);
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 }

 @Test
 public void testOutputSettingsCharset() {
 Document doc = new Document("baseUri");
 OutputSettings settings = doc.outputSettings();
 settings.charset(StandardCharsets.UTF_16BE);
 settings.charset(StandardCharsets.UTF_8);
 assertEquals(StandardCharsets.UTF_8, settings.charset().name());
 }

 @Test
 public void testOutputSettingsPrettyPrint() {
 Document doc = new Document("baseURI");
 OutputSettings settings = doc.outputSettings();
 settings.prettyPrint(false);
 settings.prettyPrint(true);
 assertEquals(true, settings.prettyPrint());
 }

 @Test
 public void testOutputSettingsIndentAmount() {
 Document doc = new Document("baseURI");
 OutputSettings settings = doc.outputSettings();
 settings.indentAmount(3);
 settings.indentAmount(5);
 assertEquals(5, settings.indentAmount());
 }

 private Element findFirstElementByTagName(String tag, Node node) {
 // implementation omitted for brevity
 }

}