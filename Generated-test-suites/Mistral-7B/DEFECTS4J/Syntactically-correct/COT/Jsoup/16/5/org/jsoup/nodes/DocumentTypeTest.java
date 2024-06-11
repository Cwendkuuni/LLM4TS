package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document();
 }

 // Test constructor
 @Test
 public void testDocumentTypeConstruction() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("publicId", docType.attr("publicId"));
 assertEquals("systemId", docType.attr("systemId"));
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType docType = new DocumentType("html", "", "", document.baseUri());
 assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method for normal case
 @Test
 public void testOuterHtmlHeadNormalCase() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("<!DOCTYPE html");
 expectedOutput.append(" PUBLIC \"publicId\"");
 expectedOutput.append('>');

 Element docTypeElement = document.parse("<!DOCTYPE html");
 docTypeElement.outerHtml(expectedOutput, 0, null);
 assertEquals(expectedOutput.toString(), docTypeElement.outerHtml());
 }

 // Test outerHtmlHead method for publicId is blank
 @Test
 public void testOuterHtmlHeadBlankPublicId() {
 DocumentType docType = new DocumentType("html", "", "systemId", document.baseUri());
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("<!DOCTYPE html");
 expectedOutput.append('>');

 Element docTypeElement = document.parse("<!DOCTYPE html");
 docTypeElement.outerHtml(expectedOutput, 0, null);
 assertEquals(expectedOutput.toString(), docTypeElement.outerHtml());
 }

 // Test outerHtmlHead method for systemId is blank
 @Test
 public void testOuterHtmlHeadBlankSystemId() {
 DocumentType docType = new DocumentType("html", "publicId", "", document.baseUri());
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("<!DOCTYPE html");
 expectedOutput.append(" PUBLIC \"publicId\"");
 expectedOutput.append('>');

 Element docTypeElement = document.parse("<!DOCTYPE html");
 docTypeElement.outerHtml(expectedOutput, 0, null);
 assertEquals(expectedOutput.toString(), docTypeElement.outerHtml());
 }

 // Test outerHtmlHead method for baseUri is blank
 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadNullBaseUri() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 }

 // Test outerHtmlTail method for normal case
 @Test
 public void testOuterHtmlTailNormalCase() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder expectedOutput = new StringBuilder();

 Element docTypeElement = document.parse("<!DOCTYPE html");
 docTypeElement.outerHtmlTail(expectedOutput, 0, null);
 assertEquals("", expectedOutput.toString());
 }
}