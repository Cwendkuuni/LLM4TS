package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 private String baseUri = "http://example.com";

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML", baseUri);
 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testDocumentTypeCreationWithNoAttributes() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testDocumentTypeCreationWithAllAttributes() {
 DocumentType docType = new DocumentType("html", "PUBLIC -//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", baseUri);
 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.getAttribute(DocumentType.NAME));
 assertEquals("PUBLIC -//W3C//DTD HTML 4.01//EN", docType.getAttribute(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.getAttribute(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeGetNodeName() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testOuterHtmlHeadHtml5() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(OutputSettings.Syntax.html);
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!doctype html>", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithAllAttributes() {
 DocumentType docType = new DocumentType("html", "PUBLIC -//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", baseUri);
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(OutputSettings.Syntax.html);
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNoSyntaxSpecified() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlTail(accum, 0, out);
 assertNull(accum.toString());
 }

 @Test
 public void testHasAttributeWithNullAttribute() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 assertFalse(docType.has(null));
 }

 @Test
 public void testHasAttributeWithBlankAttributeValue() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 docType.attr(DocumentType.NAME, "");
 assertFalse(docType.has(DocumentType.NAME));
 }

 @Test
 public void testHasAttributeWithNonBlankAttributeValue() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 docType.attr(DocumentType.NAME, "html");
 assertTrue(docType.has(DocumentType.NAME));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateDocumentTypeWithNullName() {
 new DocumentType(null, null, null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateDocumentTypeWithNullPublicId() {
 new DocumentType("html", null, null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateDocumentTypeWithNullSystemId() {
 new DocumentType("html", null, null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithEmptyPublicId() {
 new DocumentType("html", "", null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithEmptySystemId() {
 new DocumentType("html", null, "", baseUri);
 }

 @Test
 public void testParseDocumentType() throws IOException {
 String htmlDocument = "<!DOCTYPE html>";
 Node parsedNode = Parser.parseDocument(htmlDocument);
 assertEquals(DocumentType.class, parsedNode.getClass());
 DocumentType docType = (DocumentType) parsedNode;
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testParseDocumentTypeWithAllAttributes() throws IOException {
 String htmlDocument = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 Node parsedNode = Parser.parseDocument(htmlDocument);
 assertEquals(DocumentType.class, parsedNode.getClass());
 DocumentType docType = (DocumentType) parsedNode;
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.getAttribute(DocumentType.NAME));
 assertEquals("PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", docType.getAttribute(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.getAttribute(DocumentType.SYSTEM_ID));
 }

 @Test(expected = IOException.class)
 public void testParseDocumentTypeWithNoDocumentType() throws IOException {
 Parser.parseDocument("<html><body></body></html>");
 assertFalse(Parser.parseDocument("<html><body></body></html>").getFirstChild().getClass().isAssignableFrom(DocumentType.class));
 }
}