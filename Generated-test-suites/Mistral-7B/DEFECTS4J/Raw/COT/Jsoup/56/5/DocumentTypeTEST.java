package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DocumentTypeTest {

 private static final String NAME = "html";
 private static final String PUBLIC_ID = "publicId";
 private static final String SYSTEM_ID = "systemId";
 private static final String BASE_URI = "http://example.com";

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, SYSTEM_ID, BASE_URI);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(NAME, documentType.attr(DocumentType.NAME));
 assertEquals(PUBLIC_ID, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(SYSTEM_ID, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(BASE_URI, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, "", "", BASE_URI);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(BASE_URI, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdOnly() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, "", BASE_URI);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(NAME, documentType.attr(DocumentType.NAME));
 assertEquals(PUBLIC_ID, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(BASE_URI, documentType.baseUri());
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithSystemIdOnly() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, "", SYSTEM_ID, BASE_URI);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(NAME, documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(SYSTEM_ID, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(BASE_URI, documentType.baseUri());
 }

 @Test
 public void testHasAttribute() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, SYSTEM_ID, BASE_URI);
 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasEmptyAttribute() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, "", "", BASE_URI);
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, SYSTEM_ID, BASE_URI);
 Appendable appendable = new StringBuilder();
 documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
 String expected = "<!doctype " + NAME + " PUBLIC \" " + PUBLIC_ID + "\"";
 String actual = appendable.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, "", SYSTEM_ID, BASE_URI);
 Appendable appendable = new StringBuilder();
 documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
 String expected = "<!doctype " + NAME;
 String actual = appendable.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithEmptySystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, "", BASE_URI);
 Appendable appendable = new StringBuilder();
 documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
 String expected = "<!doctype " + NAME + " PUBLIC \" " + PUBLIC_ID + "\"";
 String actual = appendable.toString();
 assertEquals(expected, actual);
 }

 @Test (expected = IOException.class)
 public void testOuterHtmlHeadWithInvalidSyntax() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, SYSTEM_ID, BASE_URI);
 Appendable appendable = new StringBuilder();
 documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxXml());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(NAME, PUBLIC_ID, SYSTEM_ID, BASE_URI);
 Appendable appendable = new StringBuilder();
 documentType.outerHtmlTail(appendable, 0, Document.OutputSettings.syntaxHtml5());
 assertEquals("", appendable.toString());
 }

 @Test
 public void testParseDocumentType() throws IOException {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Document document = parser.parse(html);
 Node node = document.selectFirst("#doctype");
 DocumentType documentType = (DocumentType) node;
 assertEquals(NAME, documentType.attr(DocumentType.NAME));
 assertEquals(PUBLIC_ID, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(SYSTEM_ID, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(BASE_URI, documentType.baseUri());
 }

 @Test
 public void testParseDocumentTypeWithEmptyPublicId() throws IOException {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html SYSTEM>";
 Document document = parser.parse(html);
 Node node = document.selectFirst("#doctype");
 DocumentType documentType = (DocumentType) node;
 assertEquals(NAME, documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 @Test
 public void testParseDocumentTypeWithEmptySystemId() throws IOException {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
 Document document = parser.parse(html);
 Node node = document.selectFirst("#doctype");
 DocumentType documentType = (DocumentType) node;
 assertEquals(NAME, documentType.attr(DocumentType.NAME));
 assertEquals(PUBLIC_ID, documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(document.baseUri(), documentType.baseUri());
 }
}