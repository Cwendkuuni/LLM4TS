package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadMethod() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test (expected = NullPointerException.class)
 public void testHeadMethodWhenNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testBodyMethod() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test (expected = NullPointerException.class)
 public void testBodyMethodWhenNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testTitleMethodGet() {
 Document doc = new Document("baseUri");
 doc.title("testTitle");
 String title = doc.title();
 assertEquals("testTitle", title);
 }

 @Test
 public void testTitleMethodSet() {
 Document doc = new Document("baseUri");
 doc.title("testTitle");
 doc.title("newTitle");
 String title = doc.title();
 assertEquals("newTitle", title);
 }

 @Test (expected = NullPointerException.class)
 public void testTitleMethodWhenNullBaseUri() {
 Document doc = new Document(null);
 doc.title("title");
 }

 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("tagName");
 assertNotNull(element);
 }

 @Test (expected = NullPointerException.class)
 public void testCreateElementMethodWhenNullBaseUri() {
 Document doc = new Document(null);
 doc.createElement("tagName");
 }

 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("baseUri");
 doc.appendElement("p").text("Text before normalise");
 doc.appendElement("html").appendElement("body").text("Text after normalise");
 doc.normalise();
 Element body = doc.body();
 assertNotNull(body);
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Text after normalise ", textNode.text());
 }

 @Test
 public void testFindFirstElementByTagNameMethod() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = html.appendElement("head");
 Element body = html.appendElement("body");
 Element title = head.appendElement("title");
 Element p = body.appendElement("p");
 Element found = doc.findFirstElementByTagName("body", doc);
 assertNotNull(found);
 assertEquals("body", found.tagName());
 found = doc.findFirstElementByTagName("title", doc);
 assertNotNull(found);
 assertEquals("title", found.tagName());
 found = doc.findFirstElementByTagName("p", doc);
 assertNotNull(found);
 assertEquals("p", found.tagName());
 found = doc.findFirstElementByTagName("head", doc);
 assertNotNull(found);
 assertEquals("head", found.tagName());
 found = doc.findFirstElementByTagName("html", doc);
 assertNotNull(found);
 assertEquals("html", found.tagName());
 found = doc.findFirstElementByTagName("unknown", doc);
 assertNull(found);
 }

 @Test (expected = NullPointerException.class)
 public void testFindFirstElementByTagNameMethodWhenNullBaseUri() {
 Document doc = new Document(null);
 doc.findFirstElementByTagName("body", doc);
 }

}