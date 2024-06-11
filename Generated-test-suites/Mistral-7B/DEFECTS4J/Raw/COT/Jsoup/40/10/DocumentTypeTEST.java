package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

 private static final String DOCTYPE_NAME = "html";
 private static final String DOCTYPE_PUBLIC_ID = "HTML_PUBLIC";
 private static final String DOCTYPE_SYSTEM_ID = "-//W3C//DTD HTML 4.01//EN";
 private static final String DOCTYPE_BASE_URI = "http://example.com/";

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(DOCTYPE_NAME, documentType.attr("name"));
 assertEquals(DOCTYPE_PUBLIC_ID, documentType.attr("publicId"));
 assertEquals(DOCTYPE_SYSTEM_ID, documentType.attr("systemId"));
 assertEquals(DOCTYPE_BASE_URI, documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyDocumentTypeName() {
 assertThrows(IllegalArgumentException.class, () -> new DocumentType("", DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocumentTypeName() {
 assertThrows(IllegalArgumentException.class, () -> new DocumentType(null, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 Document document = new Document();

 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE " + DOCTYPE_NAME, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicId() {
 StringBuilder accum = new StringBuilder();
 Document document = new Document();

 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE " + DOCTYPE_NAME + " PUBLIC \"HTML_PUBLIC\" \"" + DOCTYPE_SYSTEM_ID + '>', accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemId() {
 StringBuilder accum = new StringBuilder();
 Document document = new Document();

 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE " + DOCTYPE_NAME + " PUBLIC \"HTML_PUBLIC\" \"" + DOCTYPE_SYSTEM_ID + '>', accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithNamePublicIdSystemId() {
 StringBuilder accum = new StringBuilder();
 Document document = new Document();

 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE " + DOCTYPE_NAME + " PUBLIC \"HTML_PUBLIC\" \"" + DOCTYPE_SYSTEM_ID + '>', accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 StringBuilder accum = new StringBuilder();
 Document document = new Document();

 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 documentType.outerHtmlTail(accum, 0, null);

 assertNull(accum.toString());
 }

 @Test
 public void testDocumentTypeNodeName() {
 Node documentTypeNode = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);

 assertEquals("#doctype", documentTypeNode.nodeName());
 }

 @Test
 public void testDocumentTypeAttrName() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 Element attr = documentType.attr("name");

 assertEquals(DOCTYPE_NAME, attr.nodeValue());
 assertEquals("#text", attr.nodeName());
 }

 @Test
 public void testDocumentTypeAttrPublicId() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 Element attr = documentType.attr("publicId");

 assertEquals(DOCTYPE_PUBLIC_ID, attr.nodeValue());
 assertEquals("#text", attr.nodeName());
 }

 @Test
 public void testDocumentTypeAttrSystemId() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);
 Element attr = documentType.attr("systemId");

 assertEquals(DOCTYPE_SYSTEM_ID, attr.nodeValue());
 assertEquals("#text", attr.nodeName());
 }

 @Test
 public void testDocumentTypeBaseUri() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, DOCTYPE_BASE_URI);

 assertEquals(DOCTYPE_BASE_URI, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNullBaseUri() {
 DocumentType documentType = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, null);

 assertNull(documentType.baseUri());
 }
}