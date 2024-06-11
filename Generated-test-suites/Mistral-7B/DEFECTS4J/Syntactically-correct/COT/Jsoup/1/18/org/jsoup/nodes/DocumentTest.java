package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 assertNotNull(doc.getElementsByTag("html"));
 assertNotNull(doc.getElementsByTag("head"));
 assertNotNull(doc.getElementsByTag("body"));
 }

 @Test
 public void testHeadMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testHeadMethodWhenNoHead() {
 Document doc = Jsoup.parse("<html><body></body></html>");
 doc.head();
 }

 @Test
 public void testBodyMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testBodyMethodWhenNoBody() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head></html>");
 doc.body();
 }

 @Test
 public void testTitleMethodGet() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 String title = doc.title();
 assertEquals("Test", title);
 }

 @Test
 public void testTitleMethodSet() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 doc.title("New Title");
 String title = doc.title();
 assertEquals("New Title", title);
 }

 @Test
 public void testCreateElementMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 assertEquals("a", newElement.tagName());
 }

 @Test
 public void testNormaliseMethod() {
 Document doc = Jsoup.parse("<html><head></head><body><p>Text<div>Inside div</div>Text</p></body></html>");
 Document normalisedDoc = doc.normalise();
 assertNotNull(normalisedDoc);
 List<Element> bodyChildren = normalisedDoc.body().children();
 assertEquals(1, bodyChildren.size());
 Element pElement = bodyChildren.get(0);
 List<Node> pChildren = pElement.childNodes();
 assertEquals(2, pChildren.size());
 assertEquals("Text", pChildren.get(0).text());
 assertEquals("Inside div", pChildren.get(1).text());
 }

 @Test
 public void testOuterHtmlMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 String outerHtml = doc.outerHtml();
 assertEquals("<html><head><title>Test</title></head><body></body></html>", outerHtml);
 }

 @Test
 public void testTextMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Text</p></body></html>");
 doc.text("New Text");
 String bodyText = doc.body().text();
 assertEquals("New Text", bodyText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTitleMethodSetNull() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 doc.title(null);
 }

 @Test
 public void testNormaliseMethodWithEmptyDocument() {
 Document doc = Jsoup.parse("<html></html>");
 doc.normalise();
 List<Element> elements = doc.getElementsByTag("*");
 assertEquals(0, elements.size());
 }
}