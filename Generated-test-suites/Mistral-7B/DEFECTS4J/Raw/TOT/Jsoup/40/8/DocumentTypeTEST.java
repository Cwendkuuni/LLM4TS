package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 // Test case for constructor
 @Test
 public void testDocumentTypeConstructor() {
 Document document = new Document();
 DocumentType docType = new DocumentType("html", "publicId1", "systemId1", document.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("publicId1", docType.attr("publicId"));
 assertEquals("systemId1", docType.attr("systemId"));
 }

 // Test case for outerHtmlHead method
 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType docType = new DocumentType("html", "publicId1", "systemId1", document.baseUri());
 StringBuilder accum = new StringBuilder();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
 docType.outerHtmlHead(accum, 0, document.outputSettings());

 assertEquals("<!DOCTYPE html PUBLIC \"publicId1\" \"systemId1\">", accum.toString());
 }

 // Test case for outerHtmlHead with empty attributes
 @Test
 public void testDocumentTypeOuterHtmlHeadEmptyAttributes() {
 Document document = new Document();
 DocumentType docType = new DocumentType("html", "", "", document.baseUri());
 StringBuilder accum = new StringBuilder();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
 docType.outerHtmlHead(accum, 0, document.outputSettings());

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 // Test case for outerHtmlHead with only name attribute
 @Test
 public void testDocumentTypeOuterHtmlHeadNameAttribute() {
 Document document = new Document();
 DocumentType docType = new DocumentType("html", "publicId1", "", document.baseUri());
 StringBuilder accum = new StringBuilder();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
 docType.outerHtmlHead(accum, 0, document.outputSettings());

 assertEquals("<!DOCTYPE html PUBLIC \"\" \"publicId1\">", accum.toString());
 }

 // Test case for outerHtmlTail method
 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType docType = new DocumentType("html", "publicId1", "systemId1", document.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, document.outputSettings());

 assertEquals("", accum.toString());
 }

 // Test case for nodeName method
 @Test
 public void testDocumentTypeNodeName() {
 Document document = new Document();
 DocumentType docType = new DocumentType("html", "publicId1", "systemId1", document.baseUri());

 assertEquals("#doctype", docType.nodeName());
 }

 // Test case for error when publicId and systemId are null
 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeNullPublicIdAndSystemId() {
 Document document = new Document();
 document.baseUri("https://example.com");
 new DocumentType("html", null, null, document.baseUri());
 }

 // Test case for error when publicId is empty
 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptyPublicId() {
 Document document = new Document();
 document.baseUri("https://example.com");
 new DocumentType("html", "", null, document.baseUri());
 }

 // Test case for error when systemId is empty
 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptySystemId() {
 Document document = new Document();
 document.baseUri("https://example.com");
 new DocumentType("html", null, "", document.baseUri());
 }
}