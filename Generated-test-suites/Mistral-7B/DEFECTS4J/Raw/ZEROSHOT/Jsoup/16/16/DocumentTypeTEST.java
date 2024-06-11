package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", document.baseUri());
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", document.baseUri());
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.standardOutputSettings());
 assertEquals("<!DOCTYPE htmlPUBLIC \"publicID\" systemID>\n", accumulator.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", document.baseUri());
 StringBuilder accumulator = new StringBuilder();
 documentType.outerHtmlTail(accumulator, 0, Document.OutputSettings.standardOutputSettings());
 assertTrue(accumulator.toString().isEmpty());
 }

 @Test
 public void testDocumentTypeParser() {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html PUBLIC \"publicID\" systemID>";
 Document document = parser.parse(html);
 Element doctype = document.child(0);
 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr("name"));
 assertEquals("publicID", doctype.attr("publicId"));
 assertEquals("systemID", doctype.attr("systemId"));
 }

}