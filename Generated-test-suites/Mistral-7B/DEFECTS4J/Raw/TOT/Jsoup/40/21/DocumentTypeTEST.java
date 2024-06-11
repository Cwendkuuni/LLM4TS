package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
 document.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 assertEquals("baseURI", documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html");
 document.appendChild(documentType);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals(null, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
 document.appendChild(documentType);

 StringBuilder stringBuilder = new StringBuilder();
 documentType.outerHtmlHead(stringBuilder, 0, null);
 documentType.outerHtmlTail(stringBuilder, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testDocumentTypeToStringWithEmptyAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html");
 document.appendChild(documentType);

 StringBuilder stringBuilder = new StringBuilder();
 documentType.outerHtmlHead(stringBuilder, 0, null);
 documentType.outerHtmlTail(stringBuilder, 0, null);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocumentTypeName() {
 Document document = new Document();
 String name = null;
 try {
 DocumentType documentType = new DocumentType(name, "publicID", "systemID", "baseURI");
 document.appendChild(documentType);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlankDocumentTypeName() {
 Document document = new Document();
 String name = "";
 try {
 DocumentType documentType = new DocumentType(name, "publicID", "systemID", "baseURI");
 document.appendChild(documentType);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPublicId() {
 Document document = new Document();
 String name = "html";
 String publicId = null;
 try {
 DocumentType documentType = new DocumentType(name, publicId, "systemID", "baseURI");
 document.appendChild(documentType);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlankPublicId() {
 Document document = new Document();
 String name = "html";
 String publicId = "";
 try {
 DocumentType documentType = new DocumentType(name, publicId, "systemID", "baseURI");
 document.appendChild(documentType);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSystemId() {
 Document document = new Document();
 String name = "html";
 String publicId = "publicID";
 String systemId = null;
 try {
 DocumentType documentType = new DocumentType(name, publicId, systemId, "baseURI");
 document.appendChild(documentType);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBlankSystemId() {
 Document document = new Document();
 String name = "html";
 String publicId = "publicID";
 String systemId = "";
 try {
 DocumentType documentType = new DocumentType(name, publicId, systemId, "baseURI");
 document.appendChild(documentType);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testDocumentTypeWithElementChild() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
 document.appendChild(documentType);
 Element element = new Element("html");
 document.appendChild(element);

 StringBuilder stringBuilder = new StringBuilder();
 documentType.outerHtmlHead(stringBuilder, 0, null);
 documentType.outerHtmlTail(stringBuilder, 0, null);
 documentType.outerHtml(stringBuilder, 0, null);
 element.outerHtml(stringBuilder, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">\n<html></html>";
 assertEquals(expected, stringBuilder.toString());
 }
}