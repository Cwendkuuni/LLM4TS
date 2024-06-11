package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;

/**
 * Test cases for Document class.
 */
public class DocumentTest {

 @Test
 public void testCreateEmptyDocument() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 assertTrue(doc.body() == null);
 assertTrue(doc.head() == null);
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.nodeName());
 Element head = html.getFirstElementChild();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 Element body = html.getLastElementChild();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testAccessHeadAndBodyElements() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 Element body = doc.body();
 assertTrue(head == doc.head());
 assertTrue(body == doc.body());
 }

 @Test
 public void testTitleGetAndSet() {
 Document doc = new Document("baseUri");
 assertEquals("", doc.title());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElementWithBaseUri() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("a");
 assertEquals("a", element.nodeName());
 assertEquals(doc.baseUri(), element.baseUri());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 doc.appendText("Test Text");
 doc.body().appendElement("p").text("Test Paragraph");
 doc.appendElement("html").text("Test HTML");
 doc.appendElement("head").text("Test Head");
 doc.appendElement("body").text("Test Body");
 doc.appendElement("head").text("Test Head 2");
 doc.appendElement("body").text("Test Body 2");
 doc.normalise();
 assertEquals(5, doc.childNodes().size());
 Element html = doc.getFirstElementChild();
 assertEquals("html", html.nodeName());
 Element head = html.getFirstElementChild();
 assertEquals("head", head.nodeName());
 Element body = head.getLastElementChild();
 assertEquals("body", body.nodeName());
 assertEquals("Test Title", body.getElementsByTag("title").text());
 assertEquals("Test TextTest Paragraph", body.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 fail("Expected IllegalArgumentException but was not thrown.");
 }

 @Test
 public void testCloneDocument() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 Document clonedDoc = doc.clone();
 assertEquals("Test Title", clonedDoc.title());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("baseUri");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings clonedSettings = outputSettings.clone();
 assertEquals(Documents.OutputSettings.base, outputSettings.escapeMode());
 assertEquals(Documents.OutputSettings.base, clonedSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 assertEquals("UTF-8", clonedSettings.charset().name());
 }
}