package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class DocumentTest {

 @Test
 public void testCreateEmptyDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertNull(doc.head());
 assertNull(doc.body());
 assertNull(doc.title());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("#document", doc.nodeName());
 assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 assertNull(doc.title());
 }

 @Test
 public void testGetHeadAndBody() {
 Document doc = Document.createShell("http://example.com");
 Element head = doc.head();
 Element body = doc.body();

 assertEquals("head", head.nodeName());
 assertEquals("body", body.nodeName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title(null);
 assertNull(doc.title());

 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 assertEquals("p", p.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("p").text("Test 1");
 doc.head().appendElement("p").text("Test 2");
 doc.normalise();

 List<Element> bodyElements = doc.body().select("p");
 List<Element> headElements = doc.select("head p");

 assertEquals(1, bodyElements.size());
 assertEquals("Test 1", bodyElements.get(0).text());
 assertEquals(0, headElements.size());
 }

 @Test
 public void testOuterHtml() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("p").text("Test 1");
 doc.head().appendElement("p").text("Test 2");

 String html = doc.outerHtml();
 String expected = "<html><head><title></title></head><body><p>Test 1</p></body></html>";
 assertTrue(html.equals(expected));
 }

 @Test
 public void testText() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendElement("p").text("Test 1");
 doc.text("Test 2");

 assertEquals("Test 2", doc.body().text());
 }
}