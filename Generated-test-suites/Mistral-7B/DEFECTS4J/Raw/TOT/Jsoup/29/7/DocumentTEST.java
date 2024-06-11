package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DocumentTest {

 @Test
 public void testEmptyDocumentCreation() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testShellDocumentCreation() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 Element html = doc.head();
 Element head = html.head();
 Element body = html.body();
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 assertEquals("", doc.title());
 }

 @Test
 public void testDocumentTitle() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testDocumentTitleNull() {
 Document doc = new Document("baseUri");
 assertThrows(IllegalArgumentException.class, () -> doc.title(null));
 assertEquals("", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element div = doc.createElement("div");
 assertNotNull(div);
 assertEquals("div", div.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 Element body = doc.appendElement("body");
 TextNode titleText = doc.createElement("title").text("Test Title");
 head.appendChild(titleText);
 TextNode bodyText = doc.createElement("p").text("Test Text");
 body.appendChild(bodyText);
 doc.normalise();
 assertEquals("Test Title", doc.title());
 assertEquals("Test Text", bodyText.text());
 }

 @Test
 public void testNormaliseDocumentEmpty() {
 Document doc = new Document("baseUri");
 doc.normalise();
 assertEquals("#document", doc.nodeName());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testNormaliseDocumentStructure() {
 Document doc = new Document("baseUri");
 Element head1 = doc.appendElement("head");
 Element head2 = doc.appendElement("head");
 doc.normalise();
 assertEquals(head1, doc.head());
 assertNull(head2);
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("baseUri");
 OutputSettings outputSettings = doc.outputSettings();
 OutputSettings newOutputSettings = outputSettings.charset("UTF-16").prettyPrint(false);
 doc.outputSettings(newOutputSettings);
 assertEquals(newOutputSettings, doc.outputSettings());
 }

 @Test
 public void testQuirksMode() {
 Document doc = new Document("baseUri");
 assertEquals(QuirksMode.noQuirks, doc.quirksMode());
 Document docQuirks = new Document("baseUri").quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, docQuirks.quirksMode());
 }
}