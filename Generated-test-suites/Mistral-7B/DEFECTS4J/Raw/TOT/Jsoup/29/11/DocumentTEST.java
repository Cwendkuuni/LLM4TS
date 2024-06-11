package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

/**
 * Tests for the Document class.
 */
public class DocumentTest {

 private Document document;
 private Document emptyDocument;

 @Before
 public void setUp() {
 document = new Document("http://example.com");
 emptyDocument = Document.createShell("http://example.com");
 }

 /**
  * Test creating a new document with a base URI.
  */
 @Test
 public void testCreateDocumentWithBaseUri() {
 assertEquals("http://example.com", document.baseUri());
 }

 /**
  * Test creating an empty document shell with a base URI.
  */
 @Test
 public void testCreateEmptyDocumentShell() {
 assertEquals("http://example.com", emptyDocument.baseUri());
 Element html = emptyDocument.head();
 Element head = html.head();
 Element body = html.body();
 assertTrue(html != null);
 assertTrue(head != null);
 assertTrue(body != null);
 }

 /**
  * Test accessing the document's head element.
  */
 @Test
 public void testAccessHeadElement() {
 Element head = document.head();
 assertTrue(head != null);
 }

 /**
  * Test accessing the document's body element.
  */
 @Test
 public void testAccessBodyElement() {
 Element body = document.body();
 assertTrue(body != null);
 }

 /**
  * Test getting the string contents of the document's title element.
  */
 @Test
 public void testTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 /**
  * Test setting the document's title element.
  */
 @Test
 public void testSetTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 /**
  * Test creating a new Element with a base URI.
  */
 @Test
 public void testCreateElementWithBaseUri() {
 Element element = document.createElement("div");
 assertEquals("http://example.com", element.baseUri());
 }

 /**
  * Test normalising a document.
  */
 @Test
 public void testNormaliseDocument() {
 document.title("Test Title");
 document.appendText("Test Text");
 Element title = document.getElementsByTag("title").first();
 Element textNode = document.getElementsByTag("textNode").first(); // should be null
 assertEquals("Test TitleTest Text", document.outerHtml());
 document.normalise();
 assertEquals("html<head><title>Test Title</title></head><body></body>", document.outerHtml());
 title = document.getElementsByTag("title").first();
 assertNotNull(title);
 textNode = document.getElementsByTag("textNode").first(); // should still be null
 }

 /**
  * Test getting the document's output settings.
  */
 @Test
 public void testGetOutputSettings() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 }

 /**
  * Test setting the document's output settings.
  */
 @Test
 public void testSetOutputSettings() {
 OutputSettings newOutputSettings = new OutputSettings();
 newOutputSettings.charset("UTF-16");
 document.outputSettings(newOutputSettings);
 assertEquals("UTF-16", document.outputSettings().charset().name());
 }
}