package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test cases for Document class
 */
public class DocumentTest {

 private String baseUri = "http://example.com";

 @Test
 public void testDocumentConstruction() {
 Document doc = new Document(baseUri);
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell(baseUri);
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testEmptyTitle() {
 Document doc = new Document(baseUri);
 assertEquals("", doc.title());
 }

 @Test
 public void testTitleAccessAndModify() {
 Document doc = new Document(baseUri);
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleNull() {
 Document doc = new Document(baseUri);
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document(baseUri);
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.nodeName());
 }

 @Test
 public void testDocumentNormalisation() {
 Document doc = new Document(baseUri);
 doc.head().appendElement("title").text("Test Title");
 doc.appendElement("div").text("Test Text");
 doc.appendElement("body").appendElement("p").text("Test Paragraph");
 doc.normalise();
 Element body = doc.body();
 assertEquals(3, body.childNodes().size());
 assertNotNull(body.getElementsByTag("title").first());
 assertNotNull(body.getElementsByTag("p").first());
 assertNotNull(body.getElementsByTag("p").last());
 }

 @Test
 public void testEmptyDocumentNormalisation() {
 Document doc = new Document(baseUri);
 doc.normalise();
 assertEquals(3, doc.childNodes().size());
 assertNotNull(doc.getElementsByTag("html").first());
 assertNotNull(doc.getElementsByTag("head").first());
 assertNotNull(doc.getElementsByTag("body").first());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUriConstruction() {
 Document doc = new Document(null);
 }

 @Test
 public void testDocumentToString() {
 Document doc = new Document(baseUri);
 doc.head().appendElement("title").text("Test Title");
 doc.appendElement("body").appendElement("p").text("Test Paragraph");
 String html = doc.outerHtml();
 assertTrue(html.contains("<html>"));
 assertTrue(html.contains("<head>"));
 assertTrue(html.contains("<title>Test Title</title>"));
 assertTrue(html.contains("<body>"));
 assertTrue(html.contains("Test Paragraph"));
 }

 @Test
 public void testDocumentText() {
 Document doc = new Document(baseUri);
 doc.appendElement("body").appendElement("p").text("Test Paragraph");
 doc.text("Test Text");
 assertEquals("Test Text", doc.text().trim());
 }

 @Test
 public void testDocumentTextWithChildren() {
 Document doc = new Document(baseUri);
 doc.appendElement("body").appendElement("p").text("Test Paragraph");
 doc.text("Test Text <b>Test Bold</b>");
 assertEquals("Test Text Test Bold", doc.text().trim());
 }

 @Test
 public void testDocumentEmptyText() {
 Document doc = new Document(baseUri);
 doc.text("");
 assertEquals("", doc.text().trim());
 }
}