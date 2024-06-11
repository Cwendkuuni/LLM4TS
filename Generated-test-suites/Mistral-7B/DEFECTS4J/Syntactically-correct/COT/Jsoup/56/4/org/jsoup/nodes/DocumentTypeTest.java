package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

 private String name = "html";
 private String publicId = "publicId";
 private String systemId = "systemId";
 private String baseUri = "baseUri";

 @Test
 public void testDocumentTypeConstruction() {
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr(DocumentType.NAME));
 assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeConstruction() {
 DocumentType documentType = new DocumentType(name, "", "", baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendDocument(documentType);

 Appendable accumulator = new StringBuffer();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
 documentType.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!doctype " + name + (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"\"" + publicId + '\"') + (StringUtil.isBlank(systemId) ? "" : " \"\"\"" + systemId + '\"') + ">\n";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendDocument(documentType);

 Appendable accumulator = new StringBuffer();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
 documentType.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!DOCTYPE " + name + " PUBLIC \"\"\"" + publicId + "\"\" SYSTEM \"\"\"" + systemId + "\">\n";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithOnlyPublicId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, "", baseUri);
 document.appendDocument(documentType);

 Appendable accumulator = new StringBuffer();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
 documentType.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!DOCTYPE " + name + " PUBLIC \"\"\"" + publicId + "\">\n";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithOnlySystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, "", systemId, baseUri);
 document.appendDocument(documentType);

 Appendable accumulator = new StringBuffer();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
 documentType.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!DOCTYPE " + name + " SYSTEM \"\"\"" + systemId + "\">\n";
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNoSyntax() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendDocument(documentType);

 Appendable accumulator = new StringBuffer();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 documentType.outerHtmlHead(accumulator, 0, document.outputSettings());

 String expected = "<!DOCTYPE " + name + (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"\"" + publicId + '\"') + (StringUtil.isBlank(systemId) ? "" : " SYSTEM \"\"\"" + systemId + '\"') + ">\n";
 assertEquals(expected, accumulator.toString());
 }

 @Test (expected = IOException.class)
 public void testOuterHtmlHeadWithNullAccumulator() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendDocument(documentType);

 documentType.outerHtmlHead(null, 0, document.outputSettings());
 fail("IOException should be thrown when accumulator is null.");
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendDocument(documentType);

 Appendable accumulator = new StringBuffer();
 documentType.outerHtmlTail(accumulator, 0, document.outputSettings());

 assertEquals("", accumulator.toString());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasNoAttribute() {
 DocumentType documentType = new DocumentType("", "", "", baseUri);

 assertFalse(documentType.has(DocumentType.NAME));
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }
}