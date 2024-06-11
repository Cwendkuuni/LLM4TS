package org.jsoup.nodes.test;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 */
public class DocumentTest {

 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadMethod() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testBodyMethod() {
 Document doc = new Document("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testTitleMethod() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 assertEquals("a", newElement.tagName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }

 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("<html><head></head><body><p>hello<head><p>world</p></html>");
 doc.normalise();
 assertEquals("<html><head></head><body><p>hello</p><p>world</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testFindFirstElementByTagNameMethod() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>hello</p></body></html>");
 Element title = doc.findFirstElementByTagName("title", doc);
 assertEquals("title", title.tagName());
 assertEquals("Test", title.text());
 }

 @Test
 public void testNullNodeInFindFirstElementByTagNameMethod() {
 Document doc = new Document("<html></html>");
 assertNull(doc.findFirstElementByTagName("title", null));
 }

 @Test
 public void testTitleAndTextMethod() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>hello</p></body></html>");
 doc.title("New Title");
 doc.text("New Text");
 assertEquals("New Title", doc.title());
 assertEquals("New Text", doc.outerHtml());
 }

 @Test
 public void testCloneMethod() {
 Document originalDoc = new Document("<html><head><title>Test</title></head><body><p>Original</p></body></html>");
 Document clonedDoc = originalDoc.clone();
 assertEquals(originalDoc, clonedDoc);
 assertTrue(originalDoc != clonedDoc);
 }

 @Test
 public void testOutputSettingsMethod() {
 Document doc = new Document("<html></html>");
 OutputSettings outputSettings = new OutputSettings();
 doc.outputSettings(outputSettings);
 OutputSettings outputSettingsCopy = doc.outputSettings();
 assertEquals(outputSettings, outputSettingsCopy);
 }

 @Test
 public void testQuirksModeMethod() {
 Document doc = new Document("<html></html>");
 doc.quirksMode(Document.QuirksMode.quirks);
 assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
 }
}