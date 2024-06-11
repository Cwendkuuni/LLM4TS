package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.junit.Test;

public class DocumentTest {

 private Document createDocumentWithBaseUri(String baseUri) {
 return new Document(baseUri);
 }

 @Test
 public void testDocumentConstructor() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertSame("#document", doc.nodeName());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadAccessors() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 Element head = doc.head();
 assertEquals("head", head.tagName());
 assertNull(head.previousSibling());
 assertNull(head.nextSibling());
 assertNull(doc.title());

 head.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testBodyAccessors() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 Element body = doc.body();
 assertEquals("body", body.tagName());
 assertNull(body.previousSibling());
 assertNull(body.nextSibling());
 assertNull(doc.title());
 }

 @Test
 public void testTitleAccessors() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 Element element = doc.createElement("div");
 assertEquals("div", element.tagName());
 assertEquals("http://example.com", element.baseUri());
 }

 @Test
 public void testNormalise() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 doc.head().appendText("Test Head Text");
 doc.body().appendText("Test Body Text");
 doc.appendText("Test Document Text");
 doc.normalise();
 assertNull(doc.head().previousSibling());
 assertNull(doc.head().nextSibling());
 assertNull(doc.body().previousSibling());
 assertNull(doc.body().nextSibling());
 assertEquals("Test Body TextTest Document Text", doc.body().text());
 }

 @Test
 public void testNormaliseStructure() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 Element head1 = doc.createElement("head");
 head1.appendText("Test Head Text 1");
 doc.head().appendChild(head1);
 Element head2 = doc.createElement("head");
 head2.appendText("Test Head Text 2");
 doc.head().appendChild(head2);
 doc.normalise();
 assertEquals("Test Head Text 1", doc.head().firstChild().text());
 assertNull(head2.parent());
 }

 @Test(expected = NullPointerException.class)
 public void testNormaliseTextNodesNullElement() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 doc.normaliseTextNodes(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormaliseTextNodesNegativeSizeToMove() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 Element element = doc.createElement("div");
 List<Node> toMove = new ArrayList<Node>();
 for (int i = 0; i < 10; i++) {
 Node node = doc.createElement("textNode");
 element.appendChild(node);
 toMove.add(node);
 }
 doc.normaliseTextNodes(element);
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 Element element = doc.createElement("div");
 for (int i = 0; i < 10; i++) {
 Node node = doc.createElement("textNode");
 node.setText("Test Text " + i);
 element.appendChild(node);
 }
 doc.normaliseTextNodes(element);
 assertEquals(10, element.childNodes().size());
 for (int i = 1; i < 10; i++) {
 Node node = element.childNodes().get(i);
 assertSame(element.childNodes().get(i - 1), node.previousSibling());
 assertSame(element.childNodes().get(i + 1), node.nextSibling());
 assertEquals("Test Text " + (i - 1), element.childNodes().get(i - 1).text());
 assertEquals("", element.childNodes().get(i).text());
 assertEquals("Test Text " + i, element.childNodes().get(i + 1).text());
 }
 }

 @Test
 public void testOuterHtml() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 doc.head().appendText("Test Head Text");
 doc.body().appendText("Test Body Text");
 assertEquals("<html><head>Test Head Text</head><body>Test Body Text</body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 doc.body().appendText("Test Body Text");
 assertEquals("Test Body Text", doc.text());
 }

 @Test
 public void testNodeName() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testClone() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 OutputSettings outputSettings = new OutputSettings();
 doc.outputSettings(outputSettings);
 Document clonedDoc = doc.clone();
 assertEquals(outputSettings, clonedDoc.outputSettings());
 }

 @Test
 public void testOutputSettings() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 OutputSettings outputSettings = new OutputSettings();
 OutputSettings clonedOutputSettings = doc.outputSettings().clone();
 assertEquals(outputSettings, clonedOutputSettings);
 }

 @Test
 public void testQuirksMode() {
 Document doc = createDocumentWithBaseUri("http://example.com");
 assertEquals(QuirksMode.noQuirks, doc.quirksMode());
 Document docWithQuirksMode = doc.quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, docWithQuirksMode.quirksMode());
 }
}