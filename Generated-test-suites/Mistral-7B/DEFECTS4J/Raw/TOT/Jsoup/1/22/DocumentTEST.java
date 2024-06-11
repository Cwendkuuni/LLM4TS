package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document createdDoc = new Document("http://example.com");
 assertEquals(Tag.HTML, createdDoc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.getElementsByTag("head").first();
 Element body = doc.body();

 assertEquals(Tag.HTML, doc.tagName());
 assertEquals(Tag.HEAD, head.tagName());
 assertEquals(Tag.BODY, body.tagName());
 }

 @Test
 public void testGetHeadAndBody() {
 Element head = doc.head();
 Element body = doc.body();

 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testGetEmptyTitle() {
 assertEquals("", doc.title());
 }

 @Test
 public void testSetTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("a");
 assertEquals(Tag.A, newElement.tagName());
 }

 @Test
 public void testNormalise() {
 doc.title("Test Title");
 doc.body().appendText("Test text");
 Element p = doc.createElement("p");
 doc.body().appendChild(p);
 p.appendText("Another test text");
 doc.normalise();

 Element body = doc.body();
 assertEquals("Test Title\nTest text\n<p>\nAnother test text\n", doc.outerHtml());
 assertEquals("Test text", body.text());
 }

 @Test
 public void testSetText() {
 doc.text("Test text");
 assertEquals("Test text", doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document.createShell(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 doc.title(null);
 }
}