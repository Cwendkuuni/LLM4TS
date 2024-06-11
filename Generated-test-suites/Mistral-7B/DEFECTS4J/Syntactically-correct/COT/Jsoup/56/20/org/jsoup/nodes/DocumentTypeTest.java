package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import static org.jsoup.nodes.Document.OutputSettings.*;

public class DocumentTypeTest {

 private String name = "html";
 private String publicId = "publicId";
 private String systemId = "systemId";
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
 public void testEmptyDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType(name, null, null, baseUri);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendChild(documentType);
 Appendable accumulator = new StringBuffer();
 document.outputSettings(html5());
 document.writeExternal(accumulator);
 String expected = "<!doctype html>";
 String actual = accumulator.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendChild(documentType);
 Appendable accumulator = new StringBuffer();
 document.outputSettings(html5());
 document.writeExternal(accumulator);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" ";
 String actual = accumulator.toString();
 assertTrue(actual.contains(expected));
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendChild(documentType);
 Appendable accumulator = new StringBuffer();
 document.outputSettings(html5());
 document.writeExternal(accumulator);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\"";
 String actual = accumulator.toString();
 assertTrue(actual.contains(expected));
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadWithInvalidSettings() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendChild(documentType);
 Appendable accumulator = new StringBuffer();
 document.outputSettings(xml());
 document.writeExternal(accumulator);
 fail("IOException should be thrown");
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 document.appendChild(documentType);
 document.outputSettings(html5());
 document.writeExternal(System.out);
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 DocumentType documentTypeEmpty = new DocumentType(name, null, null, baseUri);
 assertFalse(documentTypeEmpty.has(DocumentType.PUBLIC_ID));
 assertFalse(documentTypeEmpty.has(DocumentType.SYSTEM_ID));
 }
}