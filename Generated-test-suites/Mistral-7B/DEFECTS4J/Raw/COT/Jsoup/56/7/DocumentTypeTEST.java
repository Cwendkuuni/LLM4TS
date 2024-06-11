package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {

 private String baseUri = "http://test.com";

 @Test
 public void testDocumentTypeConstruction() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", baseUri);
 Document doc = new Document(baseUri);
 doc.appendChild(docType);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeConstruction() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 Document doc = new Document(baseUri);
 doc.appendChild(docType);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameConstruction() {
 DocumentType docType = new DocumentType(null, "publicID", "systemID", baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUriConstruction() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document doc = new Document(baseUri);
 DocumentType docType = new DocumentType("html", "publicID", "systemID", baseUri);
 doc.appendChild(docType);

 Appendable appendable = new StringBuilder();
 docType.outerHtmlHead(appendable, 0, Document.OutputSettings.xml());

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>", appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyAttributes() throws IOException {
 Document doc = new Document(baseUri);
 DocumentType docType = new DocumentType("html", null, null, baseUri);
 doc.appendChild(docType);

 Appendable appendable = new StringBuilder();
 docType.outerHtmlHead(appendable, 0, Document.OutputSettings.xml());

 assertEquals("<!DOCTYPE html>", appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithOnlyPublicId() throws IOException {
 Document doc = new Document(baseUri);
 DocumentType docType = new DocumentType("html", "publicID", null, baseUri);
 doc.appendChild(docType);

 Appendable appendable = new StringBuilder();
 docType.outerHtmlHead(appendable, 0, Document.OutputSettings.xml());

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" >", appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithOnlySystemId() throws IOException {
 Document doc = new Document(baseUri);
 DocumentType docType = new DocumentType("html", null, "systemID", baseUri);
 doc.appendChild(docType);

 Appendable appendable = new StringBuilder();
 docType.outerHtmlHead(appendable, 0, Document.OutputSettings.xml());

 assertEquals("<!DOCTYPE html SYSTEM \"systemID\">", appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithBothPublicIdAndSystemId() throws IOException {
 Document doc = new Document(baseUri);
 DocumentType docType = new DocumentType("html", "publicID", "systemID", baseUri);
 doc.appendChild(docType);

 Appendable appendable = new StringBuilder();
 docType.outerHtmlHead(appendable, 0, Document.OutputSettings.xml());

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>", appendable.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadWithInvalidSyntax() throws IOException {
 Document doc = new Document(baseUri);
 DocumentType docType = new DocumentType("html", "publicID", "systemID", baseUri);
 doc.appendChild(docType);

 Appendable appendable = new StringBuilder();
 Assert.assertThrows(IllegalArgumentException.class, () -> docType.outerHtmlHead(appendable, 0, Document.OutputSettings.xhtml()));
 }

 @Test
 public void testHasAttribute() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", baseUri);

 assertTrue(docType.has(DocumentType.NAME));
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertTrue(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasNoAttribute() {
 DocumentType docType = new DocumentType("html", null, null, baseUri);

 assertFalse(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }
}