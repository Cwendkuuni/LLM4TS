package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

 private Document createDocument() {
 return new Document("baseUrl");
 }

 @Test
 public void testDocumentConstruction() {
 Document doc = createDocument();
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUrl");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.getFirstElementByTagName("head");
 assertNotNull(head);
 Element body = html.getFirstElementByTagName("body");
 assertNotNull(body);
 }

 @Test
 public void testHeadAccessors() {
 Document doc = createDocument();
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("#head", head.tagName());
 }

 @Test
 public void testBodyAccessors() {
 Document doc = createDocument();
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("#body", body.tagName());
 }

 @Test
 public void testTitleAccessors() {
 Document doc = createDocument();
 String initialTitle = doc.title();
 assertNull(initialTitle);
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocument();
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = createDocument();
 Element title = doc.createElement("title").text("Test Title");
 doc.appendChild(title);
 doc.appendText(" Test Text ");
 doc.normalise();
 Element body = doc.body();
 TextNode titleNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test TitleTest Text", titleNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameWithNullNode() {
 Document doc = createDocument();
 assertNull(doc.findFirstElementByTagName("html", null));
 }

 @Test
 public void testFindFirstElementByTagNameWithEmptyDocument() {
 Document doc = createDocument();
 assertNull(doc.findFirstElementByTagName("html", doc));
 }

 @Test
 public void testFindFirstElementByTagNameWithDocumentContainingHtml() {
 Document doc = createDocument();
 Element html = doc.appendElement("html");
 assertNotNull(doc.findFirstElementByTagName("html", doc));
 }

 @Test
 public void testFindFirstElementByTagNameWithDocumentContainingDuplicateHtml() {
 Document doc = createDocument();
 Element html1 = doc.appendElement("html");
 Element html2 = doc.appendElement("html");
 assertNull(doc.findFirstElementByTagName("html", doc));
 }

 @Test
 public void testFindFirstElementByTagNameWithDocumentContainingDuplicateHtmlMovedToFirst() {
 Document doc = createDocument();
 Element html1 = doc.appendElement("html");
 Element html2 = doc.appendElement("html");
 doc.normalise();
 assertNotNull(doc.findFirstElementByTagName("html", doc));
 }

 @Test
 public void testClone() {
 Document doc = createDocument();
 Document clonedDoc = doc.clone();
 assertEquals(doc.outputSettings(), clonedDoc.outputSettings());
 }

}