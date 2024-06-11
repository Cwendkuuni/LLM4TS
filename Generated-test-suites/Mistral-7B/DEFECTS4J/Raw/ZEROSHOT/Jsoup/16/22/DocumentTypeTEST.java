package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML PUBLIC", docType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
 assertEquals("http://example.com", docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html");

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 doc.setBaseUri("http://example.com");
 doc.append(docType);
 doc.append(new Element("html"));
 doc.outputSettings(new Document.OutputSettings().indent(true).prettyPrint(true));
 doc.append(new TextNode("Test content."));
 doc.outputSettings(new Document.OutputSettings().prettyPrint(true));
 doc.toString(accum, null);

 String expected = "<!DOCTYPE html PUBLIC \"HTML PUBLIC\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n<html>\n";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 doc.setBaseUri("http://example.com");
 doc.append(docType);
 doc.outputSettings(new Document.OutputSettings().indent(true).prettyPrint(true));
 doc.toString(accum, null);

 assertSame("", docType.outerHtmlTail(accum, 0, new Document.OutputSettings()));
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeOuterHtmlTailNullDocument() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
 StringBuilder accum = null;
 docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeOuterHtmlTailNegativeDepth() {
 DocumentType docType = new DocumentType("html", "HTML PUBLIC", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, -1, new Document.OutputSettings());
 }
}