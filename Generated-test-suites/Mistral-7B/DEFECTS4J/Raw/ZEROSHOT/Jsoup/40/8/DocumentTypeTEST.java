package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeWithOnlyName() {
 DocumentType documentType = new DocumentType("html", null, null, document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeWithOnlyPublicId() {
 DocumentType documentType = new DocumentType("html", "publicId", null, document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicId\" >";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeWithOnlySystemId() {
 DocumentType documentType = new DocumentType("html", null, "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html SYSTEM \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeParsing() throws Exception {
 Parser parser = new Parser.MalformedEncodingParser();
 Element element = parser.parse("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">");

 assertNotNull(element);
 assertEquals(DocumentType.class, element.getClass());
 DocumentType documentType = (DocumentType) element;

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 @Test
 public void testDocumentTypeParsingWithOnlyName() throws Exception {
 Parser parser = new Parser.MalformedEncodingParser();
 Element element = parser.parse("<!DOCTYPE html>");

 assertNotNull(element);
 assertEquals(DocumentType.class, element.getClass());
 DocumentType documentType = (DocumentType) element;

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals(null, documentType.attr("publicId"));
 assertEquals(null, documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 @Test
 public void testDocumentTypeParsingWithOnlyPublicId() throws Exception {
 Parser parser = new Parser.MalformedEncodingParser();
 Element element = parser.parse("<!DOCTYPE html PUBLIC \"publicId\">");

 assertNotNull(element);
 assertEquals(DocumentType.class, element.getClass());
 DocumentType documentType = (DocumentType) element;

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals(null, documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 @Test
 public void testDocumentTypeParsingWithOnlySystemId() throws Exception {
 Parser parser = new Parser.MalformedEncodingParser();
 Element element = parser.parse("<!DOCTYPE html SYSTEM \"systemId\">");

 assertNotNull(element);
 assertEquals(DocumentType.class, element.getClass());
 DocumentType documentType = (DocumentType) element;

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals(null, documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.baseUri());
 }
}