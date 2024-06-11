package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "HTML";
 String systemId = "DTD HTML 4.01 Transitional";
 String baseUri = "http://example.com/";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 String name = "";
 String publicId = "HTML";
 String systemId = "DTD HTML 4.01 Transitional";
 String baseUri = "http://example.com/";

 try {
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 } catch (IllegalArgumentException e) {
 failure(e, "Name cannot be empty");
 }
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 DocumentType documentType = new DocumentType("html", "HTML", "DTD HTML 4.01 Transitional", "http://example.com/");
 Element element = new Element("html");
 element.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 document.outputSettings().setPrettyPrint(true);
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 documentType.outerHtmlTail(accum, 0, document.outputSettings());

 String expected = "<!DOCTYPE html PUBLIC \"HTML\" \"DTD HTML 4.01 Transitional\"><html>\n";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 4.01 Transitional", "http://example.com/");
 Element element = new Element("html");
 element.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 document.outputSettings().setPrettyPrint(true);
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 documentType.outerHtmlTail(accum, 0, document.outputSettings());

 String expected = "<!DOCTYPE html PUBLIC \"HTML PUBLIC\" \"DTD HTML 4.01 Transitional\"><html>\n";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithOnlyName() {
 DocumentType documentType = new DocumentType("html", "", "", "http://example.com/");
 Element element = new Element("html");
 element.appendChild(documentType);

 StringBuilder accum = new StringBuilder();
 document.outputSettings().setPrettyPrint(true);
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 documentType.outerHtmlTail(accum, 0, document.outputSettings());

 String expected = "<!DOCTYPE html><html>\n";
 assertEquals(expected, accum.toString());
 }
}