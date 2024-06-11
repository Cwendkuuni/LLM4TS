package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class DocumentTypeTest {

 private static final String DOCTYPE_NAME = "html";
 private static final String DOCTYPE_PUBLIC_ID = "publicId";
 private static final String DOCTYPE_SYSTEM_ID = "systemId";
 private static final String DOCTYPE_BASE_URI = "baseUri";

 @Test
 public void testDocumentTypeConstruction() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(DOCTYPE_NAME, documentType.attr(DocumentType.NAME));
 assertEquals(DOCTYPE_PUBLIC_ID, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(DOCTYPE_SYSTEM_ID, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(DOCTYPE_BASE_URI, documentType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 document.insertBefore(0, documentType);
 document.setBaseUri(DOCTYPE_BASE_URI);

 Appendable accumulator = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 document.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!doctype " + DOCTYPE_NAME + " PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadEmpty() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, null, null, DOCTYPE_BASE_URI);
 document.insertBefore(0, documentType);
 document.setBaseUri(DOCTYPE_BASE_URI);

 Appendable accumulator = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 document.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!doctype " + DOCTYPE_NAME + ">";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadNoName() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(null, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 document.insertBefore(0, documentType);
 document.setBaseUri(DOCTYPE_BASE_URI);

 Appendable accumulator = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 document.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!DOCTYPE>";
 assertEquals(expected, accumulator.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadWithoutBaseUri() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, null);
 document.insertBefore(0, documentType);
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 document.outerHtmlHead(null, 0, document.outputSettings());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 Document document = new Document();
 document.insertBefore(0, documentType);
 document.setBaseUri(DOCTYPE_BASE_URI);

 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasNoAttribute() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, null, null, DOCTYPE_BASE_URI);

 assertFalse(documentType.has("noAttribute"));
 }

 @Test
 public void testAttrs() {
 Document document = new Document();
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 document.insertBefore(0, documentType);
 document.setBaseUri(DOCTYPE_BASE_URI);

 Attributes attrs = documentType.attrs();
 assertEquals(DocumentType.NAME, attrs.get(DocumentType.NAME));
 assertEquals(DOCTYPE_PUBLIC_ID, attrs.get(DocumentType.PUBLIC_ID));
 assertEquals(DOCTYPE_SYSTEM_ID, attrs.get(DocumentType.SYSTEM_ID));
 }
}