package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * DocumentTest.java - Test class for Document.java
 */
public class DocumentTest {

 @Test
 public void testDocumentEmptyConstruction() {
 Document document = new Document("baseUri");
 assertNotNull(document);
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testDocumentCreateShell() {
 Document document = Document.createShell("baseUri");
 assertNotNull(document);
 Element html = document.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testDocumentHead() {
 Document document = new Document("baseUri");
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testDocumentBody() {
 Document document = new Document("baseUri");
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testDocumentTitle() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @Test (expected = NullPointerException.class)
 public void testDocumentTitleNullInput() {
 Document document = new Document("baseUri");
 document.title(null);
 }

 @Test
 public void testDocumentCreateElement() {
 Document document = new Document("baseUri");
 Element element = document.createElement("tagName");
 assertNotNull(element);
 assertEquals("tagName", element.tagName());
 }

 @Test
 public void testDocumentNormalise() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().appendText("Test Body");
 document.normalise();
 Element title = document.head().getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 Element body = document.body();
 assertEquals("Test Body", body.text());
 }

 @Test
 public void testDocumentNormaliseTextNodes() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().appendText("Test Body ");
 document.body().appendText(" Test Body ");
 document.normalise();
 Element title = document.head().getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 Element body = document.body();
 assertEquals("Test Body Test Body", body.text());
 }

 @Test
 public void testDocumentNormaliseStructure() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().appendText("Test Body 1");
 Element head2 = document.appendChild("html").appendElement("head").appendElement("head");
 Element body2 = document.createElement("body").setText("Test Body 2");
 document.getElementById("head").appendChild(body2);
 document.normalise();
 Element title = document.head().getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 Element body = document.body();
 assertEquals("Test Body 1", body.text());
 }

 @Test
 public void testDocumentOuterHtml() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().appendText("Test Body");
 String html = document.outerHtml();
 assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", html);
 }

 @Test
 public void testDocumentText() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().appendText("Test Body");
 document.text("Test Text");
 String text = document.text();
 assertEquals("Test TextTest BodyTest Title", text);
 }

 @Test
 public void testDocumentNodeName() {
 Document document = new Document("baseUri");
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testDocumentClone() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 document.body().appendText("Test Body");
 Document clone = document.clone();
 assertEquals("Test Title", clone.title());
 assertEquals("Test Body", clone.body().text());
 }

 @Test
 public void testDocumentOutputSettingsGetters() {
 Document document = new Document("baseUri");
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 assertFalse(outputSettings.prettyPrint());
 assertFalse(outputSettings.indentAmount() > 0);
 }

 @Test
 public void testDocumentOutputSettingsSetters() {
 Document document = new Document("baseUri");
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.escapeMode(Entities.EscapeMode.extended);
 outputSettings.charset("UTF-16");
 outputSettings.prettyPrint(true);
 outputSettings.indentAmount(2);
 document.outputSettings(outputSettings);
 OutputSettings settings = document.outputSettings();
 assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
 assertEquals("UTF-16", settings.charset().name());
 assertTrue(settings.prettyPrint());
 assertEquals(2, settings.indentAmount());
 }

 @Test (expected = NullPointerException.class)
 public void testDocumentOutputSettingsNullInput() {
 Document document = new Document("baseUri");
 document.outputSettings(null);
 }

 @Test
 public void testDocumentQuirksModeGettersAndSetters() {
 Document document = new Document("baseUri");
 assertEquals(QuirksMode.noQuirks, document.quirksMode());
 document.quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, document.quirksMode());
 document.quirksMode(QuirksMode.limitedQuirks);
 assertEquals(QuirksMode.limitedQuirks, document.quirksMode());
 }
}