package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 assertTrue(doc.head() == null);
 assertTrue(doc.body() == null);
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test (expected = AssertionError.class)
 public void testGetTitleNull() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title()); // should throw an AssertionError
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 doc.title("Test Title 2");
 assertEquals("Test Title 2", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element el = doc.createElement("a");
 assertNotNull(el);
 assertEquals("a", el.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<html>\nTest\n<body>\n<head>\n<title>Title</title>\n</head>\n</html>\n</root>");
 doc.normalise();
 Element html = doc.head();
 assertEquals(1, html.childNodes().size());
 Element head = html.childNodes().get(0);
 assertEquals("html", head.nodeName());
 Element title = head.childNodes().get(0);
 assertEquals("title", title.nodeName());
 assertEquals("Title", title.text());
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 settings.prettyPrint(false);
 settings.charset("UTF-16");
 doc.title("Test Title");
 assertEquals("", doc.outerHtml()); // tests that the outputSettings are applied
 }

 @Test
 public void testGetOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals(Charset.forName("UTF-8"), settings.charset());
 }
}