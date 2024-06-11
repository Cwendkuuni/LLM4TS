package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author YourName
 */
public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 }

 @Test
 public void testHeadElement() {
 Document doc = Document.createShell("http://example.com");
 Element head = doc.head();
 assertEquals("head", head.tagName());
 }

 @Test
 public void testBodyElement() {
 Document doc = Document.createShell("http://example.com");
 Element body = doc.body();
 assertEquals("body", body.tagName());
 }

 @Test
 public void testTitleElement() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = Document.createShell("http://example.com");
 doc.body().appendText("Test 1 ");
 doc.head().appendText("Test 2 ");
 doc.appendText("Test 3 ");
 doc.normalise();
 List<Element> bodyElements = doc.body().children();
 assertEquals(2, bodyElements.size());
 Element pElement = bodyElements.get(0);
 assertEquals("Test 1", pElement.text());
 assertEquals("Test 3", bodyElements.get(1).text());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleWithNullTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentWithNullBaseUri() {
 try {
 Document doc = new Document(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateShellWithNullBaseUri() {
 try {
 Document.createShell(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}