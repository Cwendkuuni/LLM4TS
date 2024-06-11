package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

 // Test for constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 Element html = doc.select("html").first();
 assertNotNull(html);
 Element head = html.getElementsByTag("head").first();
 assertNotNull(head);
 Element body = html.getElementsByTag("body").first();
 assertNotNull(body);
 }

 // Test for head method
 @Test
 public void testHeadMethod() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNull(head);

 doc = Document.createShell("http://example.com");
 head = doc.head();
 assertNotNull(head);
 }

 // Test for body method
 @Test
 public void testBodyMethod() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNull(body);

 doc = Document.createShell("http://example.com");
 body = doc.body();
 assertNotNull(body);
 }

 // Test for title method
 @Test
 public void testTitleMethod() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());

 doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 // Test for title method with null title
 @Test(expected = NullPointerException.class)
 public void testTitleMethodNull() {
 Document doc = Document.createShell("http://example.com");
 doc.title(null);
 }

 // Test for createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 // Test for normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("p").text("Test");
 doc.head().appendElement("title").text("Test Title");
 doc.appendElement("div").text("Test Div");

 doc.normalise();
 List<Element> bodyElements = doc.body().getElementsByTag("p");
 assertEquals(1, bodyElements.size());
 assertEquals("Test", bodyElements.get(0).text());

 List<Element> headElements = doc.head().getElementsByTag("title");
 assertEquals(1, headElements.size());
 assertEquals("Test Title", headElements.get(0).text());

 List<Element> divElements = doc.getElementsByTag("div");
 assertEquals(0, divElements.size());
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("p").text("Test");
 doc.head().appendElement("title").text("Test Title");
 doc.appendElement("div").text("Test Div");

 StringBuilder expected = new StringBuilder();
 expected.append("<html><head><title>Test Title</title></head><body>");
 expected.append("<p>Test</p></body></html>");

 assertEquals(expected.toString(), doc.outerHtml());
 }

 // Test for text method
 @Test
 public void testTextMethod() {
 Document doc = new Document("http://example.com");
 doc.body().appendElement("p").text("Test");

 assertEquals("Test", doc.text().toString());
 }

 // Test for nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }
}