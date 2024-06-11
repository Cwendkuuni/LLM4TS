package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertNotNull(doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testHeadAccessor() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(head);
 assertNull(doc.title());
 }

 @Test
 public void testBodyAccessor() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testTitleGetter() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testTitleSetter() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 doc.title(null);
 assertNull(doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("a");
 assertNotNull(element);
 assertEquals("a", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendText("Test text in root.");
 Element root = doc.elementAt(0);
 Element title = doc.createElement("title").text("Test Title");
 doc.head().appendChild(title);
 doc.normalise();
 Element body = doc.body();
 TextNode textNode = (TextNode) body.child(0);
 assertEquals("Test Title", title.text());
 assertEquals("Test text in root.", textNode.text());
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = new Document("baseUri");
 doc.appendText("Test text in root.");
 doc.appendText("Test text in root.");
 doc.normalise();
 Element body = doc.body();
 TextNode textNode1 = (TextNode) body.child(0);
 TextNode textNode2 = (TextNode) body.child(1);
 assertEquals("Test text in root.", textNode1.text());
 assertEquals("Test text in root.", textNode2.text());
 }

 @Test
 public void testNormaliseStructure() {
 Document doc = new Document("baseUri");
 Element title1 = doc.createElement("title").text("Test Title 1");
 Element title2 = doc.createElement("title").text("Test Title 2");
 doc.head().appendChild(title1);
 doc.head().appendChild(title2);
 doc.normalise();
 Element body = doc.body();
 TextNode textNode1 = (TextNode) body.child(0);
 TextNode textNode2 = (TextNode) body.child(1);
 assertEquals("Test Title 1", title1.text());
 assertEquals("Test Title 2", title2.text());
 assertEquals("", textNode1.text());
 assertEquals("", textNode2.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head", html);
 Element body = doc.appendElement("body", html);
 Element title = doc.createElement("title").text("Test Title").appendTo(head);
 Element a = doc.createElement("a").appendTo(body);

 Element titleFromHead = doc.findFirstElementByTagName("title", doc);
 assertEquals(title, titleFromHead);

 Element aFromBody = doc.findFirstElementByTagName("a", doc);
 assertEquals(a, aFromBody);

 Element nonExistent = doc.findFirstElementByTagName("nonExistent", doc);
 assertNull(nonExistent);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 doc.appendChild(doc.createElement("html"));
 doc.head().appendChild(doc.createElement("title").text("Test Title"));
 doc.body().appendChild(doc.createElement("a").text("Test Link"));
 assertEquals("<html><head><title>Test Title</title></head><body><a>Test Link</a></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("baseUri");
 doc.body().appendText("Test text in body.");
 assertEquals("Test text in body.", doc.text());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testClone() {
 Document doc = new Document("baseUri");
 Document clone = doc.clone();
 assertEquals(doc, clone);
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("baseUri");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings newOutputSettings = new OutputSettings();
 newOutputSettings.charset("UTF-16");
 doc.outputSettings(newOutputSettings);
 assertEquals(newOutputSettings, doc.outputSettings());
 }

 @Test
 public void testQuirksMode() {
 Document doc = new Document("baseUri");
 assertEquals(QuirksMode.noQuirks, doc.quirksMode());
 Document docQuirks = new Document("baseUri").quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, docQuirks.quirksMode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Document doc = new Document("baseUri");
 assertThrows(NullPointerException.class, () -> doc.createElement(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharset() {
 Document doc = new Document("baseUri");
 assertThrows(IllegalArgumentException.class, () -> doc.outputSettings().charset("invalidCharset"));
 }
}