package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

 @Test
 public void testCreateEmptyDocument() {
 Document document = new Document("http://example.com");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 assertNull(document.title());
 }

 @Test
 public void testCreateShellDocument() {
 Document document = Document.createShell("http://example.com");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 assertNull(document.title());
 }

 @Test
 public void testGetTitle() {
 Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitle() {
 Document document = Jsoup.parse("<html><head><title></title></head><body></body></html>", "http://example.com");
 document.title("New Title");
 assertEquals("New Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Document document = Jsoup.parse("<html><head><title></title></head><body></body></html>", "http://example.com");
 Element element = document.createElement("div");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test</p></head></body></html>", "http://example.com");
 document.normalise();
 Element titleElement = document.head().getElementsByTag("title").first();
 assertNotNull(titleElement);
 assertEquals("Test Title", titleElement.text());
 Element bodyElement = document.body();
 assertEquals(1, bodyElement.childNodes().size());
 assertEquals("Test", bodyElement.childNodes().get(0).text());
 }

 @Test
 public void testNormaliseWithMultipleElements() {
 Document document = Jsoup.parse("<html><head><title></title></head><body><p>Test 1</p><p>Test 2</p></body><body><p>Test 3</p></body></html>", "http://example.com");
 document.normalise();
 Element titleElement = document.head().getElementsByTag("title").first();
 assertNotNull(titleElement);
 assertEquals(0, document.body().childNodes().size());
 Element bodyElement = document.getElementsByTag("body").first();
 assertEquals(1, bodyElement.childNodes().size());
 assertEquals("Test 1Test 2", bodyElement.childNodes().get(0).text());
 }

 @Test
 public void testHtml() {
 Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", "http://example.com");
 assertEquals("<html>\n<head>\n<title>Test Title</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>", document.html());
 }

 @Test
 public void testPrettyPrinting() {
 Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test 1</p><p>Test 2</p></body></html>", "http://example.com");
 document.outputSettings(new Document.OutputSettings().prettyPrint(false));
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test 1</p><p>Test 2</p></body></html>", document.html());
 document.outputSettings(new Document.OutputSettings().prettyPrint(true));
 assertEquals("<html>\n<head>\n<title>Test Title</title>\n</head>\n<body>\n<p>Test 1</p>\n<p>Test 2</p>\n</body>\n</html>", document.html());
 }

 @Test
 public void testCharset() {
 Document document = Jsoup.parse("<html><head><meta charset=\"UTF-8\"></head><body></body></html>", "http://example.com");
 assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
 }

 @Test
 public void testClone() {
 Document document = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>", "http://example.com");
 Document clonedDocument = document.clone();
 assertEquals("<html>\n<head>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>", clonedDocument.html());
 }
}