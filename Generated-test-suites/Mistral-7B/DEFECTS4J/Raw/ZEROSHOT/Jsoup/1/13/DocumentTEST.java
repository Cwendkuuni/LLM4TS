package test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
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
 public void testDocumentCreation() {
 Document document = new Document("http://example.com");
 assertTrue(document instanceof Document);
 assertEquals("#root", document.tagName());
 assertNull(document.title());
 assertNull(document.head());
 assertNull(document.body());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 assertTrue(document instanceof Document);
 assertNotNull(document.title());
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testGetTitle() {
 Document document = Document.createShell("http://example.com");
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitle() {
 Document document = Document.createShell("http://example.com");
 document.title(null);
 assertNull(document.title());
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Document document = Document.createShell("http://example.com");
 Element p = document.createElement("p");
 assertTrue(p instanceof Element);
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 Document document = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
 document.normalise();
 Element body = document.body();
 assertEquals(1, body.childNodes().size());
 assertTrue(body.childNodes().get(0) instanceof TextNode);
 }

 @Test
 public void testTitleAndBodyText() {
 Document document = Document.createShell("http://example.com");
 document.title("Test Title");
 document.body().text("Test Body");
 assertEquals("Test Title", document.title());
 assertEquals("Test Body", document.body().text());
 }

 @Test
 public void testTrimmedTitle() {
 Document document = Document.createShell("http://example.com");
 document.title(" Test Title ");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testEmptyTitle() {
 Document document = Document.createShell("http://example.com");
 document.title("");
 assertEquals("", document.title());
 }

 @Test
 public void testNullTitle() {
 Document document = Document.createShell("http://example.com");
 document.title(null);
 assertNull(document.title());
 }

 @Test
 public void testTitleWithSpecialCharacters() {
 Document document = Document.createShell("http://example.com");
 document.title("Test Title with <special> characters");
 assertEquals("Test Title with <special> characters", document.title());
 }

 @Test
 public void testTitleLength() {
 Document document = Document.createShell("http://example.com");
 String title = StringUtils.repeat("A", 1000);
 document.title(title);
 assertEquals(title, document.title());
 }
}