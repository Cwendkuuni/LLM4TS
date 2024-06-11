package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {

 // Test constructor
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

 // Test constructor with null name
 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeConstructorNullName() {
 DocumentType docType = new DocumentType(null, "publicId", "systemId", null);
 }

 // Test nodeName method
 @Test
 public void testDocumentTypeNodeName() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method - basic functionality
 @Test
 public void testDocumentTypeOuterHtmlHeadBasicFunctionality() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, depth, out);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method - publicId and systemId are not blank
 @Test
 public void testDocumentTypeOuterHtmlHeadPublicIdSystemIdNotBlank() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, depth, out);

 String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method - edge case: empty publicId and systemId
 @Test
 public void testDocumentTypeOuterHtmlHeadEmptyPublicIdSystemId() {
 DocumentType docType = new DocumentType("html", "", "", null);
 StringBuilder accum = new StringBuilder();
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, depth, out);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method - exception handling: null accumulator
 @Test(expected = NullPointerException.class)
 public void testDocumentTypeOuterHtmlHeadNullAccumulator() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(null, 0, out);
 }

 // Test outerHtmlHead method - exception handling: null out
 @Test(expected = NullPointerException.class)
 public void testDocumentTypeOuterHtmlHeadNullOutputSettings() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 }

 // Test outerHtmlTail method - basic functionality
 @Test
 public void testDocumentTypeOuterHtmlTailBasicFunctionality() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlTail(accum, depth, out);

 assertEquals("", accum.toString());
 }
}