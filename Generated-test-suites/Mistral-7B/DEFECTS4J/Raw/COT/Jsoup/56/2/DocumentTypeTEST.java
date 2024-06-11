package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {

 private String baseUri = "http://example.com";

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "HTML", "HTML5", baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertSame(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "PUBLIC", "publicID", "systemID", baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));
 assertSame(baseUri, documentType.baseUri());
 }

 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", null, null, baseUri);
 assertEquals("#doctype", documentType.nodeName());

 documentType = new DocumentType("html", "HTML", null, baseUri);
 assertEquals("#doctype", documentType.nodeName());

 documentType = new DocumentType("html", null, "systemID", baseUri);
 assertEquals("#doctype", documentType.nodeName());

 documentType = new DocumentType("html", "HTML", "systemID", baseUri);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType("html", null, null, baseUri);
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 assertFalse(documentType.has(DocumentType.NAME));

 documentType = new DocumentType("html", "HTML", null, baseUri);
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 assertFalse(documentType.has(DocumentType.NAME));

 documentType = new DocumentType("html", null, "systemID", baseUri);
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 assertFalse(documentType.has(DocumentType.NAME));

 documentType = new DocumentType("html", "HTML", "systemID", baseUri);
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 assertTrue(documentType.has(DocumentType.NAME));

 documentType = new DocumentType("html", "HTML", null, baseUri);
 documentType.attr(DocumentType.PUBLIC_ID, "publicID");
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));

 documentType = new DocumentType("html", "HTML", null, baseUri);
 documentType.attr(DocumentType.SYSTEM_ID, "systemID");
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeNullBaseUri() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptyName() {
 DocumentType documentType = new DocumentType(null, null, null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptyPublicId() {
 DocumentType documentType = new DocumentType("html", "PUBLIC", null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptySystemId() {
 DocumentType documentType = new DocumentType("html", null, "systemID", baseUri);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, baseUri);

 Appendable accum = new StringBuffer();
 document.setOutputSettings(Document.OutputSettings.prettyPrint());
 documentType.outerHtmlHead(accum, 0, document.outputSettings());

 String expected = "<!doctype html>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", null, baseUri);

 Appendable accum = new StringBuffer();
 document.setOutputSettings(Document.OutputSettings.prettyPrint());
 documentType.outerHtmlHead(accum, 0, document.outputSettings());

 String expected = "<!DOCTYPE html PUBLIC \"HTML\" ";
 expected += "\"-//W3C//DTD HTML 5//EN\" ";
 expected += "\"http://www.w3.org/TR/html5/document-html.dtd\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemID", baseUri);

 Appendable accum = new StringBuffer();
 document.setOutputSettings(Document.OutputSettings.prettyPrint());
 documentType.outerHtmlHead(accum, 0, document.outputSettings());

 String expected = "<!DOCTYPE html SYSTEM \"systemID\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNamePublicIdAndSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "HTML", "systemID", baseUri);

 Appendable accum = new StringBuffer();
 document.setOutputSettings(Document.OutputSettings.prettyPrint());
 documentType.outerHtmlHead(accum, 0, document.outputSettings());

 String expected = "<!DOCTYPE html PUBLIC \"HTML\" ";
 expected += "\"-//W3C//DTD HTML 5//EN\" ";
 expected += "\"http://www.w3.org/TR/html5/document-html.dtd\" SYSTEM \"systemID\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", null, null, baseUri);

 Appendable accum = new StringBuffer();
 documentType.outerHtmlTail(accum, 0, document.outputSettings());

 assertTrue(accum.toString().isEmpty());
 }
}