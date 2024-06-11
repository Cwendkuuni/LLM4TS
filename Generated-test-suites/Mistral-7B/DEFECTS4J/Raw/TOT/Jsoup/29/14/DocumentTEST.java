package org.jsoup.nodes;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 assertEquals("http://example.com", doc.baseUri());
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleWithEmptyString() {
 document.title("");
 assertEquals("", document.title());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleWithNull() {
 document.title(null);
 }

 @Test
 public void testCreateElement() {
 Element element = document.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.nodeName());
 assertEquals("", element.outerHtml());
 }

 @Test
 public void testNormaliseDocument() {
 document.title("Test Title");
 document.appendText("Test Content");
 Element div = document.createElement("div");
 div.text("Div Content");
 document.body().appendElement(div);
 document.normalise();
 assertNotNull(document.head());
 assertNotNull(document.body());
 assertEquals("Test Title", document.title());
 assertEquals("Test Content\nDiv Content", document.text().trim());
 }

 @Test
 public void testNormaliseDocumentWithExistingTitle() {
 document.title("Test Title");
 document.appendText("Test Content");
 Element div = document.createElement("div");
 div.text("Div Content");
 document.body().appendElement(div);
 document.normalise();
 assertNotNull(document.head());
 assertNotNull(document.body());
 assertEquals("Test Title", document.title());
 assertEquals("Test ContentDiv Content", document.text().trim());
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = new OutputSettings();
 document.outputSettings(outputSettings);
 assertEquals(outputSettings, document.outputSettings());
 }

 @Test
 public void testOutputSettingsCharset() {
 OutputSettings outputSettings = new OutputSettings();
 document.outputSettings(outputSettings);
 document.outputSettings().charset("UTF-16");
 assertEquals("UTF-16", document.outputSettings().charset().name());
 }

 @Test
 public void testOutputSettingsPrettyPrint() {
 OutputSettings outputSettings = new OutputSettings();
 document.outputSettings(outputSettings);
 document.outputSettings().prettyPrint(false);
 assertEquals(false, document.outputSettings().prettyPrint());
 }

 @Test
 public void testOutputSettingsIndentAmount() {
 OutputSettings outputSettings = new OutputSettings();
 document.outputSettings(outputSettings);
 document.outputSettings().indentAmount(4);
 assertEquals(4, document.outputSettings().indentAmount());
 }

 @Test
 public void testOutputSettingsEscapeMode() {
 OutputSettings outputSettings = new OutputSettings();
 document.outputSettings(outputSettings);
 document.outputSettings().escapeMode(OutputSettings.Entities.EscapeMode.extended);
 assertEquals(OutputSettings.Entities.EscapeMode.extended, document.outputSettings().escapeMode());
 }

 @Test
 public void testQuirksMode() {
 document.quirksMode(QuirksMode.quirks);
 assertEquals(QuirksMode.quirks, document.quirksMode());
 }
}