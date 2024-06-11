package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class DocumentTypeTest {

 private String name = "html";
 private String publicId = "private";
 private String systemId = null;
 private String baseUri = "http://example.com";

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr(DocumentType.NAME));
 assertEquals(publicId, docType.attr(DocumentType.PUBLIC_ID));
 assertEquals(systemId, docType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeConstructor() {
 DocumentType docType = new DocumentType(name, null, null, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 doc.appendChild(docType);
 Document.OutputSettings out = new Document.OutputSettings();

 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, out);

 String expected = "<!DOCTYPE " + name + (publicId != null ? " PUBLIC \"\"" + publicId + '\"' : "") + (systemId != null ? " \"\"" + systemId + '\"' : "") + '>';
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithHtml5Syntax() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 doc.appendChild(docType);
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);

 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, out);

 String expected = "<!doctype " + name + (publicId != null ? " PUBLIC \"\"" + publicId + '\"' : "") + (systemId != null ? " \"" + systemId + '\"' : "") + '>';
 assertEquals(expected.toLowerCase(), accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 doc.appendChild(docType);

 Appendable accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, new Document.OutputSettings());

 assertEquals("", accum.toString());
 }

 @Test(expected = IOException.class)
 public void testDocumentTypeOuterHtmlHeadWithNullOutSettings() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 doc.appendChild(docType);
 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 fail("Should throw IOException");
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertTrue(docType.has(DocumentType.NAME));
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeNullAttribute() {
 DocumentType docType = new DocumentType(name, null, null, baseUri);

 assertFalse(docType.has(DocumentType.NAME));
 assertFalse(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

}