package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document class
 */
public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitleElement() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 String title = doc.title();
 assertEquals("Test", title);
 }

 @Test
 public void testSetTitleElement() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = Jsoup.parse("<html><head></head><body><p>Test</p><h1>Test</h1></body></html>");
 doc.normalise();
 List<Element> bodyElements = doc.body().children();
 assertEquals(2, bodyElements.size());
 assertNotNull(bodyElements.get(0).text());
 assertNotNull(bodyElements.get(1).nodeName());
 assertEquals("h1", bodyElements.get(1).nodeName());
 }

 @Test
 public void testSetText() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 @Test
 public void testValidateBaseUri() {
 Document doc = new Document(null);
 try {
 Validate.notNull(doc.baseUri());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}