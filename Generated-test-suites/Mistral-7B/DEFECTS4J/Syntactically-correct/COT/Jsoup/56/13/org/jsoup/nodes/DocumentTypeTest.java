package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 private static final String BASE_URI = "http://example.com";

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals(BASE_URI, docType.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorEmptyValues() {
 DocumentType docType = new DocumentType("html", "", "", BASE_URI);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertEquals(BASE_URI, docType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document doc = new Document(BASE_URI);
 DocumentType docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 Element doctypeElement = doc.createElement("!DOCTYPE");
 doctypeElement.appendChild(docType);
 doc.head().appendChild(doctypeElement);

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml5());
 String expected = "<!doctype html>";
 String actual = accum.toString();
 assertEquals(expected, actual);

 docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxXhtml11());
 expected = "<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">";
 actual = accum.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadEmptyValues() throws Exception {
 Document doc = new Document(BASE_URI);
 DocumentType docType = new DocumentType("html", "", "", BASE_URI);
 Element doctypeElement = doc.createElement("!DOCTYPE");
 doctypeElement.appendChild(docType);
 doc.head().appendChild(doctypeElement);

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml5());
 String expected = "<!doctype html>";
 String actual = accum.toString();
 assertEquals(expected, actual);

 docType = new DocumentType("html", "", "", BASE_URI);
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxXhtml11());
 expected = "<!DOCTYPE html>";
 actual = accum.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 Document doc = new Document(BASE_URI);
 DocumentType docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 Element doctypeElement = doc.createElement("!DOCTYPE");
 doctypeElement.appendChild(docType);
 doc.body().appendChild(doctypeElement);

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, Document.OutputSettings.syntaxHtml5());
 String expected = "";
 String actual = accum.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testOuterHtmlTailWithoutDoctypeElement() throws Exception {
 Document doc = new Document(BASE_URI);
 StringBuilder accum = new StringBuilder();
 DocumentType docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 docType.outerHtmlTail(accum, 0, Document.OutputSettings.syntaxHtml5());
 fail("IllegalStateException expected but not thrown");
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadWithNullAccumulator() throws IOException {
 Document doc = new Document(BASE_URI);
 DocumentType docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 docType.outerHtmlHead(null, 0, Document.OutputSettings.syntaxHtml5());
 fail("IOException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testHasAttributeWithNullAttribute() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", BASE_URI);
 assertFalse(docType.has(null));
 }

}