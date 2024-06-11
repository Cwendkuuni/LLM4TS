package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertEquals("head", html.tagName());
 html = doc.body();
 assertEquals("body", html.tagName());
 }

 @Test
 public void testHeadAccess() {
 document.title("Test Title");
 Element head = document.head();
 assertEquals("Test Title", head.text().trim());
 }

 @Test
 public void testHeadAccess_missingTitle() {
 document.title("");
 Element head = document.head();
 assertNull(head.text());
 }

 @Test
 public void testBodyAccess() {
 document.title("Test Title");
 Element body = document.body();
 assertEquals(0, body.childNodes().size());
 }

 @Test
 public void testTitleSetting() {
 document.title("Test Title");
 Element title = document.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testTitleSetting_existingTitle() {
 document.title("Original Title");
 document.title("Test Title");
 Element title = document.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testCreateElement() {
 Element p = document.createElement("p");
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 document.text("Test text");
 document.head().appendElement("meta").text("test meta");
 document.appendElement("h1").text("Test Heading");
 document.normalise();
 Element body = document.body();
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test text", textNode.text());
 Element meta = document.getElementsByTag("meta").first();
 assertEquals("test meta", meta.text().trim());
 Element heading = document.getElementsByTag("h1").first();
 assertEquals("Test Heading", heading.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 document.appendElement("html").appendElement("body").appendElement("div").text("Test text");
 Element div = document.findFirstElementByTagName("div", document);
 assertEquals("div", div.tagName());
 TextNode textNode = (TextNode) div.childNodes().get(0);
 assertEquals("Test text", textNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindFirstElementByTagName_invalidTagName() {
 document.appendElement("html").appendElement("body").appendElement("div").text("Test text");
 document.findFirstElementByTagName("invalidTag", document);
 }
}