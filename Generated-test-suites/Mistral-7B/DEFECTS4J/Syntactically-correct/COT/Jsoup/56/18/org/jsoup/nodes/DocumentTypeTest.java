package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class DocumentTypeTest {

 private String name = "html";
 private String publicId = "publicID";
 private String systemId = "systemID";
 private String baseUri = "baseUri";

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr(DocumentType.NAME));
 assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithEmptyValues() {
 DocumentType documentType = new DocumentType(name, null, null, baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithOnlyName() {
 DocumentType documentType = new DocumentType(name, null, null, baseUri);
 documentType.attr(DocumentType.NAME, "newName");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("newName", documentType.attr(DocumentType.NAME));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithAllAttributes() {
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 documentType.attr(DocumentType.PUBLIC_ID, "newPublicId");
 documentType.attr(DocumentType.SYSTEM_ID, "newSystemId");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr(DocumentType.NAME));
 assertEquals("newPublicId", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("newSystemId", documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeConstructorWithNullName() {
 DocumentType documentType = new DocumentType(null, publicId, systemId, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeConstructorWithNullBaseUri() {
 DocumentType documentType = new DocumentType(name, publicId, systemId, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeConstructorWithBlankPublicId() {
 DocumentType documentType = new DocumentType(name, StringUtil.blank(), systemId, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeConstructorWithBlankSystemId() {
 DocumentType documentType = new DocumentType(name, publicId, StringUtil.blank(), baseUri);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendChild(documentType);
 StringBuilder stringBuilder = new StringBuilder();
 document.outerHtml(stringBuilder);
 String expected = "<!DOCTYPE " + name + (StringUtil.isBlank(publicId) && StringUtil.isBlank(systemId) ? "" : " PUBLIC \"" + publicId + "\"") + (StringUtil.isBlank(systemId) ? "" : " \"" + systemId + '">');
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testOuterHtmlHeadWithBlankName() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(null, publicId, systemId, baseUri);
 document.appendChild(documentType);
 StringBuilder stringBuilder = new StringBuilder();
 document.outerHtml(stringBuilder);
 String expected = "<!DOCTYPE>";
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testOuterHtmlHeadWithBlankPublicIdAndSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, null, null, baseUri);
 document.appendChild(documentType);
 StringBuilder stringBuilder = new StringBuilder();
 document.outerHtml(stringBuilder);
 String expected = "<!DOCTYPE " + name + ">";
 assertEquals(expected, stringBuilder.toString());
 }
}