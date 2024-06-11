package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.QuirksMode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document document = new Document("base-uri");
 assertNotNull(document);
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("base-uri");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testAccessHeadAndBody() {
 Document document = new Document("base-uri");
 Element head = document.head();
 Element body = document.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testAccessTitle() {
 Document document = new Document("base-uri");
 document.title("test title");
 String title = document.title();
 assertEquals("test title", title);
 }

 @Test
 public void testSetTitle() {
 Document document = new Document("base-uri");
 document.title(null);
 assertNull(document.title());
 document.title("test title");
 String title = document.title();
 assertEquals("test title", title);
 }

 @Test
 public void testCreateElement() {
 Document document = new Document("base-uri");
 Element element = document.createElement("p");
 assertNotNull(element);
 assertEquals("p", element.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 Document document = new Document("base-uri");
 document.body().appendText("text1 ");
 document.head().appendElement(new Element("title").text("test title"));
 document.head().appendElement(new Element("title").text("another title"));
 document.head().appendElement(new Element("meta").text("meta data"));
 document.appendText("text2 ");

 Document normalisedDocument = document.normalise();

 Element body = normalisedDocument.body();
 Element title = normalisedDocument.head().getElementsByTag("title").first();

 assertEquals(2, normalisedDocument.childNodes().size());
 assertEquals("text1 text2 ", body.text());
 assertEquals("test title", title.text());
 }

 @Test
 public void testNormaliseStructure() {
 Document document = new Document("base-uri");
 Element head = document.appendElement("head");
 Element title1 = head.appendElement("title").text("title1");
 Element title2 = head.appendElement("title").text("title2");
 Element body = document.appendElement("body");
 BodyAppender bodyAppender = new BodyAppender(body);
 bodyAppender.appendText("text1 ");
 bodyAppender.appendElement(new Element("p").text("p1"));
 bodyAppender.appendText("text2 ");
 bodyAppender.appendElement(new Element("p").text("p2"));

 Document normalisedDocument = document.normalise();

 assertEquals(3, normalisedDocument.childNodes().size());
 assertEquals("text1 text2 ", normalisedDocument.body().text());
 assertEquals(1, normalisedDocument.getElementsByTag("title").size());
 assertEquals("title1", normalisedDocument.getElementsByTag("title").first().text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document document = new Document("base-uri");
 Element html = document.appendElement("html");
 Element body = html.appendElement("body");
 Element title = html.appendElement("head").appendElement("title").text("title");

 Element found = document.findFirstElementByTagName("title", document);
 assertEquals("title", found.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document document = new Document("base-uri");
 document.body().appendText("test1");
 document.title("test title");
 String html = document.outerHtml();
 assertEquals("<html><head><title>test title</title></head><body>test1</body></html>", html);
 }

 @Test
 public void testTextMethod() {
 Document document = new Document("base-uri");
 document.body().appendText("test1");
 document.title("test title");
 document.body().text("test2");
 String text = document.text();
 assertEquals("test2", text);
 }

 @Test
 public void testNodeName() {
 Document document = new Document("base-uri");
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testClone() {
 Document document = new Document("base-uri");
 Document clonedDocument = document.clone();
 assertNotNull(clonedDocument);
 assertNotNull(clonedDocument.outputSettings());
 assertEquals(document.outputSettings(), clonedDocument.outputSettings());
 }

 @Test
 public void testOutputSettings() {
 Document document = new Document("base-uri");
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.prettyPrint(false);
 document.outputSettings(outputSettings);
 OutputSettings clonedOutputSettings = document.outputSettings();
 assertEquals(document.outputSettings(), clonedOutputSettings);
 }

 @Test
 public void testQuirksMode() {
 Document document = new Document("base-uri");
 document.quirksMode(Document.QuirksMode.noQuirks);
 assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
 document.quirksMode(Document.QuirksMode.quirks);
 assertEquals(Document.QuirksMode.quirks, document.quirksMode());
 document.quirksMode(Document.QuirksMode.limitedQuirks);
 assertEquals(Document.QuirksMode.limitedQuirks, document.quirksMode());
 }

}

class BodyAppender {
 private Element body;

 BodyAppender(Element body) {
 this.body = body;
 }

 BodyAppender appendText(String text) {
 Element newText = body.appendText(text);
 return this;
 }

 BodyAppender appendElement(Element element) {
 body.appendElement(element);
 return this;
 }
}