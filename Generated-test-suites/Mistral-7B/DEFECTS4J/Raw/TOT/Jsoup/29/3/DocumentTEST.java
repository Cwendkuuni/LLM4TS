package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * Test for Document class
 */
public class DocumentTest {

 @Test
 public void testEmptyDocument() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 assertEquals("baseUri", doc.baseUri());
 assertNull(doc.head());
 assertNull(doc.body());
 assertNull(doc.title());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 assertEquals("#html", html.nodeName());
 assertSame(doc, html.parent());

 Element head = html.appendElement("head");
 Element body = html.appendElement("body");

 assertEquals(3, doc.childNodes().size());
 assertEquals("#document", doc.nodeName());
 assertSame(doc, body.parent());
 }

 @Test
 public void testAccessorToDocumentHead() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 Element title = head.createElement("title").text("Test Title");
 doc.title(title.text());
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testAccessorToDocumentBody() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 Element p = body.createElement("p").text("Test Paragraph");
 doc.title("Test Document");
 assertEquals("Test Document", doc.title());
 assertEquals("Test Paragraph", body.text());
 }

 @Test
 public void testGetDocumentTitle() {
 Document doc = new Document("baseUri");
 Element title = doc.createElement("title").text("Test Title");
 doc.head().appendChild(title);
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetDocumentTitle() {
 Document doc = new Document("baseUri");
 assertEquals("", doc.title());
 Element title = doc.createElement("title").text("Test Title");
 doc.title(title.text());
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElementWithBaseUri() {
 Document doc = new Document("baseUri");
 Element link = doc.createElement("a").attr("href", "/test");
 assertEquals("/test", link.absUrl(""));
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title 2");
 doc.appendElement("html").appendElement("body").appendElement("p").text("Test Paragraph 1");
 doc.appendElement("html").appendElement("body").appendElement("p").text("Test Paragraph 2");

 doc.normalise();

 assertEquals(1, doc.childNodes().size());
 assertEquals("#document", doc.nodeName());
 assertEquals("Test Title", doc.title());
 assertEquals("Test Paragraph 1Test Paragraph 2", doc.body().text());
 }

 @Test
 public void testCreateTextNode() {
 Document doc = new Document("baseUri");
 TextNode text = doc.createTextNode("Test Text");
 assertEquals("Test Text", text.text());
 }

 @Test
 public void testCreateTextNodeWithEmptyString() {
 Document doc = new Document("baseUri");
 TextNode text = doc.createTextNode("");
 assertEquals("", text.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateElementWithNullTagName() {
 Document doc = new Document("baseUri");
 doc.createElement(null);
 }
}