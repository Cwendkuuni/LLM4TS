package org.jsoup.nodes.test;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);

 String expected = "";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeWithEmptyAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("", documentType.attr("publicId"));
 assertEquals("", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeWithOnlyPublicId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", null, document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeWithOnlySystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeWithAllAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());
 }

 @Test
 public void testDocumentTypeWithQuirksMode() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 documentType.attr("xml:xml", "true");
 documentType.attr("![IGNORE]","");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());

 Element quirksModeElement = new Element("html");
 quirksModeElement.attr("xml:lang", "en");
 document.appendElement(quirksModeElement);
 documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 documentType.attr("xml:xml", "true");
 documentType.outerHtmlHead(new StringBuilder(), 0, null);
 String output = documentType.outerHtml();
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\" [ <!ENTITY % ignored SYSTEM \"\"> ]>\n";
 assertEquals(expected, output);
 }

 @Test
 public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("", documentType.attr("publicId"));
 assertEquals("", documentType.attr("systemId"));
 assertEquals(document, documentType.parent());
 }
}