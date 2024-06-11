package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import static org.junit.Assert.*;

public class DocumentTest {

// Test case for Document constructor
@Test
public void testDocumentConstructor() {
Document doc = new Document("http://example.com");
assertNotNull(doc);
assertEquals("http://example.com", doc.baseUri());
}

// Test case for createShell method
@Test
public void testCreateShell() {
Document doc = Document.createShell("http://example.com");
assertNotNull(doc);
assertNotNull(doc.head());
assertNotNull(doc.body());
assertEquals("http://example.com", doc.baseUri());
}

// Test case for head method
@Test
public void testHeadMethod() {
Document doc = new Document("http://example.com");
Element head = doc.head();
assertNotNull(head);
assertEquals("head", head.tagName());
}

// Test case for body method
@Test
public void testBodyMethod() {
Document doc = new Document("http://example.com");
Element body = doc.body();
assertNotNull(body);
assertEquals("body", body.tagName());
}

// Test case for title method - get
@Test
public void testTitleMethodGet() {
Document doc = new Document("http://example.com");
doc.title("Test Title");
String title = doc.title();
assertEquals("Test Title", title);
}

// Test case for title method - set
@Test
public void testTitleMethodSet() {
Document doc = new Document("http://example.com");
doc.title("Test Title");
doc.title("New Title");
String title = doc.title();
assertEquals("New Title", title);
}

// Test case for createElement method
@Test
public void testCreateElementMethod() {
Document doc = new Document("http://example.com");
Element element = doc.createElement("p");
assertNotNull(element);
assertEquals("p", element.tagName());
}

// Test case for normalise method
@Test
public void testNormaliseMethod() {
Document doc = new Document("http://example.com");
doc.text("Test <b>Text</b> in <head>");
assertEquals(1, doc.head().childNodes().size());
doc.normalise();
assertEquals(0, doc.head().childNodes().size());
assertEquals(1, doc.body().childNodes().size());
}

// Test case for findFirstElementByTagName method
@Test
public void testFindFirstElementByTagNameMethod() {
Document doc = new Document("http://example.com");
Element html = doc.appendElement("html");
Element head = doc.appendElement("head", html);
Element body = doc.appendElement("body", html);
Element title = doc.appendElement("title", html);

Element foundTitle = doc.findFirstElementByTagName("title", doc);
assertEquals(title, foundTitle);
}

// Test case for outerHtml method
@Test
public void testOuterHtmlMethod() {
Document doc = new Document("http://example.com");
Element html = doc.appendElement("html");
Element head = doc.appendElement("head", html);
Element title = doc.appendElement("title", head);
title.text("Test Title");

String htmlCode = doc.outerHtml();
String expectedHtmlCode = "<html><head><title>Test Title</title></head><body></body></html>";
assertEquals(expectedHtmlCode, htmlCode);
}

// Test case for text method
@Test
public void testTextMethod() {
Document doc = new Document("http://example.com");
doc.body().text("Test Text");
String text = doc.text();
assertEquals("Test Text", text);
}

// Test case for nodeName method
@Test
public void testNodeNameMethod() {
Document doc = new Document("http://example.com");
assertEquals("#document", doc.nodeName());
}

// Test case for clone method
@Test
public void testCloneMethod() {
Document doc = new Document("http://example.com");
Document clone = doc.clone();
assertNotSame(doc, clone);
assertEquals(doc.outputSettings(), clone.outputSettings());
}

// Test case for OutputSettings escapeMode property
@Test
public void testOutputSettingsEscapeMode() {
Document doc = new Document("http://example.com");
OutputSettings settings = new OutputSettings();
settings.escapeMode(OutputSettings.Entities.EscapeMode.extended);
doc.outputSettings(settings);
String htmlCode = doc.html();
String expectedHtmlCode = "<!doctype html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Document</title>\n</head>\n<body>\n</body>\n";
// Replace special characters with their corresponding entities
expectedHtmlCode = expectedHtmlCode.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
assertEquals(expectedHtmlCode, htmlCode);
}

// Test case for OutputSettings charset property
@Test
public void testOutputSettingsCharset() {
Document doc = new Document("http://example.com");
OutputSettings settings = new OutputSettings();
settings.charset("UTF-16");
doc.outputSettings(settings);
String htmlCode = doc.html();
// Verify the charset is set correctly in the meta tag
assertTrue(htmlCode.contains("charset=\"UTF-16\""));
}

// Test case for OutputSettings prettyPrint property
@Test
public void testOutputSettingsPrettyPrint() {
Document doc = new Document("http://example.com");
OutputSettings settings = new OutputSettings();
settings.prettyPrint(false);
doc.outputSettings(settings);
String htmlCode = doc.html();
// Verify the pretty print is disabled and the output looks like the input
assertFalse(htmlCode.contains("\n"));
assertFalse(htmlCode.contains("  "));
}

// Test case for QuirksMode property
@Test
public void testQuirksMode() {
Document doc = new Document("http://example.com");
doc.quirksMode(QuirksMode.quirks);
String htmlCode = doc.html();
// Verify the quirks mode is set correctly in the doctype declaration
assertTrue(htmlCode.contains("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">"));
}
}