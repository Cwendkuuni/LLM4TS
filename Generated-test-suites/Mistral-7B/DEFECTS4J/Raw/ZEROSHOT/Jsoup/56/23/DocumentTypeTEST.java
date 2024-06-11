package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 private static Document createDocument(String docType, String html) {
 String entireHtml = docType + System.lineSeparator() + html;
 try {
 return Parser.parseBodyFragment(entireHtml, "").parse();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType dt = new DocumentType("html", null, null, null);
 assertEquals("#doctype", dt.nodeName());
 assertTrue(dt.has(DocumentType.NAME));
 assertFalse(dt.has(DocumentType.PUBLIC_ID));
 assertFalse(dt.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithAttributes() {
 DocumentType dt = new DocumentType("html", "publicID", "systemID", null);
 assertEquals("#doctype", dt.nodeName());
 assertTrue(dt.has(DocumentType.NAME));
 assertTrue(dt.has(DocumentType.PUBLIC_ID));
 assertTrue(dt.has(DocumentType.SYSTEM_ID));
 assertEquals("publicID", dt.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", dt.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType dt = new DocumentType("html", null, null, null);
 assertFalse(dt.has(DocumentType.PUBLIC_ID));
 assertFalse(dt.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeToHtml() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithPublicId() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html public \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithSystemId() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html SYSTEM \"about:legacy-compat\">\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithPublicIdAndSystemId() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithNoAttributes() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithQuirksMode() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n<!ENTITY % q \"-//W3C//DTD XHTML 1.0 Frameset//EN\" SYSTEM \"http://www.w3.org/DTD/xhtml1-frameset.dtd\">\r\n<html %q>\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.Mode.QUIRKS));
 assertEquals("<!doctype html><!ENTITY % q SYSTEM \"http://www.w3.org/DTD/xhtml1-frameset.dtd\"><!ENTITY % html SYSTEM \"\"><html %q>", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithNoAttributesAndQuirksMode() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.Mode.QUIRKS));
 assertEquals("<!doctype html>", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithOnlyPublicId() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html PUBLIC \"-\" \"-\">\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertNull(output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithOnlySystemId() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html SYSTEM \"-\">\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertNull(output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithEmptyPublicIdAndSystemId() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html PUBLIC \"\" \"\">\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertNull(output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithEmptyName() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE \r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertNull(output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithInvalidSyntax() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html <invalid>\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertNull(output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithNoInnerHtml() throws Exception {
 String html = "<html><head><title>Test</title></head></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n", html);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtml(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithInnerHtml() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n" + html, null);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtml(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html>", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithInnerHtmlAndAttributes() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"\r\n" + html, null);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtml(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", output.toString());
 }

 @Test
 public void testDocumentTypeToHtmlWithInnerHtmlQuirksMode() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
 Document document = createDocument("<!DOCTYPE html>\r\n<!ENTITY % q \"-//W3C//DTD XHTML 1.0 Frameset//EN\" SYSTEM \"http://www.w3.org/DTD/xhtml1-frameset.dtd\">\r\n<html %q>\r\n" + html, null);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);

 StringBuilder output = new StringBuilder();
 documentType.outerHtml(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, Document.OutputSettings.Mode.QUIRKS));
 assertEquals("<!DOCTYPE html><!ENTITY % q SYSTEM \"http://www.w3.org/DTD/xhtml1-frameset.dtd\"><!ENTITY % html SYSTEM \"\"><html %q>", output.toString());
 }
}