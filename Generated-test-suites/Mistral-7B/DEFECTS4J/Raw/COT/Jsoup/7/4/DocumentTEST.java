package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document createDocument() {
 return new Document("baseUri");
 }

 @Test
 public void testDocumentConstructor() {
 Document doc = createDocument();
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.nodeName());
 Element head = html.getFirstElementChild();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testHeadMethod() {
 Document doc = createDocument();
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 }

 @Test
 public void testBodyMethod() {
 Document doc = createDocument();
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testTitleMethod() {
 Document doc = createDocument();
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitleMethod() {
 Document doc = createDocument();
 doc.title("Test Title 1");
 doc.title("Test Title 2");
 assertEquals("Test Title 2", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateElementWithNullTagName() {
 Document doc = createDocument();
 try {
 doc.createElement(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocument();
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = createDocument();
 doc.text("Hello World");
 doc.appendElement("p").text("Test");
 Element pElement = doc.getElementsByTag("p").first();
 TextNode textNode = (TextNode) pElement.childNodes().get(0);
 assertEquals("Test", textNode.text());
 doc.normalise();
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 TextNode newTextNode = (TextNode) body.childNodes().get(0);
 assertEquals("Hello WorldTest", newTextNode.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = createDocument();
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element title = html.appendElement("title");
 assertEquals("title", title.nodeName());
 Element foundTitle = doc.findFirstElementByTagName("title", html);
 assertSame(title, foundTitle);
 }

 @Test
 public void testOuterHtml() {
 Document doc = createDocument();
 doc.title("Test Title");
 assertEquals("<html><head></head><body><title>Test Title</title></body></html>", doc.outerHtml());
 }

 @Test
 public void testTextMethod() {
 Document doc = createDocument();
 doc.body().text("Hello World");
 assertEquals("Hello World", doc.text());
 }

 @Test
 public void testSetTextMethod() {
 Document doc = createDocument();
 doc.body().text("Hello World");
 doc.setText("Test");
 assertEquals("Test", doc.text());
 }

 @Test
 public void testGetNodeName() {
 Document doc = createDocument();
 assertEquals("#document", doc.nodeName());
 }

 @Test // Edge cases
 public void testCreateDocumentWithEmptyStringBaseUri() {
 Document doc = new Document("");
 assertNotNull(doc);
 }

 @Test // Exception handling
 public void testCreateDocumentWithNullBaseUri() {
 try {
 Document doc = new Document(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }
}