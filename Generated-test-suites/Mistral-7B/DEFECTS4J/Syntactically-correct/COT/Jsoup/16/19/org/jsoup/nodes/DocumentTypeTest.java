package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 // Test constructor
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertNotNull(docType);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("publicId", docType.attr("publicId"));
 assertEquals("systemId", docType.attr("systemId"));
 assertEquals("baseUri", docType.baseUri());
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType docType = new DocumentType("html", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method with valid arguments
 @Test
 public void testOuterHtmlHeadValidArguments() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder("");
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, depth, out);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method with empty publicId and systemId
 @Test
 public void testOuterHtmlHeadEmptyArguments() {
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder("");
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, depth, out);
 String expected = "<!DOCTYPE html>\n";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method with null accumulator
 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadNullAccumulator() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 StringBuilder accum = null;
 docType.outerHtmlHead(accum, depth, out);
 }

 // Test outerHtmlHead method with null out
 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadNullOut() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder("");
 int depth = 0;
 Document.OutputSettings out = null;
 docType.outerHtmlHead(accum, depth, out);
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder("");
 int depth = 0;
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlTail(accum, depth, out);
 assertNull(accum.toString());
 }
}