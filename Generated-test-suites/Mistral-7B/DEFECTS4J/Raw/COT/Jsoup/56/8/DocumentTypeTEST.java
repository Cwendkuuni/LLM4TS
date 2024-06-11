package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DocumentTypeTest {

 private String baseUri = "http://example.com";

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "html", null, baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertNotNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNotNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithNulls() {
 DocumentType documentType = new DocumentType(null, null, null, baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testToString() {
 DocumentType documentType = new DocumentType("html", "html", null, baseUri);
 String str = documentType.toString();
 String expected = "<!DOCTYPE html>";
 assertEquals(expected, str);
 }

 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", "html", null, baseUri);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "html", null, baseUri);
 document.appendChild(documentType);
 StringBuilder accum = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 String expected = "<!doctype html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "html public", "public.dtd", baseUri);
 document.appendChild(documentType);
 StringBuilder accum = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 String expected = "<!DOCTYPE html PUBLIC \"public.dtd\"";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "html", "system.dtd", baseUri);
 document.appendChild(documentType);
 StringBuilder accum = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 String expected = "<!DOCTYPE html SYSTEM \"system.dtd\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicAndSystemId() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "html public system", "public.dtd system.dtd", baseUri);
 document.appendChild(documentType);
 StringBuilder accum = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 String expected = "<!DOCTYPE html PUBLIC \"public.dtd system.dtd\"";
 assertEquals(expected, accum.toString());
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadWithInvalidSyntax() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "html", null, baseUri);
 document.appendChild(documentType);
 StringBuilder accum = new StringBuilder();
 document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml)); // wrong syntax for html
 documentType.outerHtmlHead(accum, 0, document.outputSettings());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType("html", "html", null, baseUri);
 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 DocumentType documentType2 = new DocumentType("html", null, null, baseUri);
 assertFalse(documentType2.has(DocumentType.NAME));
 assertFalse(documentType2.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType2.has(DocumentType.SYSTEM_ID));
 }
}