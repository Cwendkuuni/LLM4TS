package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.XmlDocument;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlParser;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "HTML", "html.dtd", "/path/to/baseUri");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("HTML", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("html.dtd", documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals("/path/to/baseUri", documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullAttributes() {
 DocumentType documentType = new DocumentType("html");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType documentType = new DocumentType("html", "", "", "");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", "html.dtd", "/path/to/baseUri");
 document.body().append(documentType);
 document.body().appendText("Test content.");

 String documentString = document.body().outerHtml();
 String expectedDocumentString = "<!DOCTYPE html PUBLIC \"HTML\" \"html.dtd\">\n<body>\nTest content.\n";

 assertEquals(expectedDocumentString, documentString);
 }

 @Test
 public void testDocumentTypeWithNullPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html");
 documentType.outerHtmlHead(null, 0, null);

 String expectedDocumentString = "<!doctype html>";

 assertEquals(expectedDocumentString, documentType.outerHtmlHead(null, 0, null).toString());
 }

 @Test
 public void testDocumentTypeWithPublicIdButNoSystemId() {
 DocumentType documentType = new DocumentType("html", "HTML", null, "/path/to/baseUri");
 documentType.outerHtmlHead(null, 0, null);

 String expectedDocumentString = "<!DOCTYPE html PUBLIC \"HTML\" \"/path/to/baseUri/html.dtd\">";

 assertEquals(expectedDocumentString, documentType.outerHtmlHead(null, 0, null).toString());
 }

 @Test
 public void testDocumentTypeWithSystemIdButNoPublicId() {
 DocumentType documentType = new DocumentType("html", null, "html.dtd", "/path/to/baseUri");
 documentType.outerHtmlHead(null, 0, null);

 String expectedDocumentString = "<!DOCTYPE html SYSTEM \"html.dtd\">";

 assertEquals(expectedDocumentString, documentType.outerHtmlHead(null, 0, null).toString());
 }

 @Test
 public void testDocumentTypeWithBothPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "HTML", "html.dtd", "/path/to/baseUri");
 documentType.outerHtmlHead(null, 0, null);

 String expectedDocumentString = "<!DOCTYPE html PUBLIC \"HTML\" \"html.dtd\">";

 assertEquals(expectedDocumentString, documentType.outerHtmlHead(null, 0, null).toString());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 DocumentType documentType = new DocumentType("html", "HTML", "html.dtd", "/path/to/baseUri");

 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeHasNoAttribute() {
 DocumentType documentType = new DocumentType("html");

 assertFalse(documentType.has(DocumentType.NAME));
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testCreateDocumentTypeFromXml() throws Exception {
 XmlDocument xmlDocument = XmlParser.parse("<!DOCTYPE html PUBLIC \"HTML\" \"html.dtd\">");
 Node documentTypeNode = xmlDocument.selectSingleNode("//!DOCTYPE");
 DocumentType documentType = (DocumentType) documentTypeNode.clone();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("HTML", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("html.dtd", documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals("/path/to/baseUri", documentType.baseUri());
 }

 @Test
 public void testCreateDocumentTypeFromHtml() throws Exception {
 String html = "<!DOCTYPE html PUBLIC \"HTML\" \"html.dtd\">\n<html>\n<body>\nTest content.\n</body>\n</html>";
 Document document = Parser.parse(html);
 Node documentTypeNode = document.selectFirst("//!DOCTYPE");
 DocumentType documentType = (DocumentType) documentTypeNode.clone();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testCreateDocumentTypeFromXmlWithEmptyAttributes() throws Exception {
 String xml = "<!DOCTYPE html>";
 Document document = XmlParser.parse(xml);
 Node documentTypeNode = document.selectFirst("//!DOCTYPE");
 DocumentType documentType = (DocumentType) documentTypeNode.clone();

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }
}