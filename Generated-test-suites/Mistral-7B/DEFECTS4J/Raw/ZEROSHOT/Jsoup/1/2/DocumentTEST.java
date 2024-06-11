package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert. assertNull;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Unit tests for the Document class.
 */
public class DocumentTest {

 @Test
 public void testCreateEmptyDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("", doc.title());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("#text", p.nodeName());
 assertEquals("", p.text());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("http://example.com");
 doc.head().appendElement("p").text("Test 1");
 doc.body().appendElement("p").text("Test 2");
 doc.body().appendElement("p").appendElement("Test 3");
 doc.title("Test Title");

 doc.normalise();

 Element body = doc.body();
 List<Element> pElements = body.select("p");
 assertEquals(2, pElements.size());
 assertEquals("Test 1", pElements.get(0).text());
 assertEquals("Test 2", pElements.get(1).text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = Document.createShell("http://example.com");
 doc.head().appendElement("p").text("Test 1");
 doc.body().appendElement("p").text("Test 2");
 doc.title("Test Title");

 assertEquals("<html><head><title>Test Title</title></head><body><p>Test 2</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = Document.createShell("http://example.com");
 doc.body().text("Test Text");
 assertEquals("Test Text", doc.text());
 }

}