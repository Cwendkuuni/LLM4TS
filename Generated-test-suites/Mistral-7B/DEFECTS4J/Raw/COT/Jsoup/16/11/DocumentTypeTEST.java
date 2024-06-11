package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

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
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method for normal case
 @Test
 public void testOuterHtmlHeadNormalCase() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 // Test outerHtmlHead method for publicId case
 @Test
 public void testOuterHtmlHeadPublicIdCase() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>\n", accum.toString());
 }

 // Test outerHtmlHead method for systemId case
 @Test
 public void testOuterHtmlHeadSystemIdCase() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!DOCTYPE html SYSTEM systemID>\n", accum.toString());
 }

 // Test outerHtmlHead method for publicId and systemId cases
 @Test
 public void testOuterHtmlHeadPublicIdAndSystemIdCases() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>\n", accum.toString());
 }

 // Test outerHtmlHead method for empty publicId and systemId
 @Test
 public void testOuterHtmlHeadEmptyPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "", "", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 // Test outerHtmlHead method for null baseUri
 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadNullBaseUri() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, null);
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlTail(accum, 0, out);
 assertNull(accum.toString());
 }

 // Test attr method for name
 @Test
 public void testAttrName() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 assertEquals("html", docType.attr("name"));
 }

 // Test attr method for publicId
 @Test
 public void testAttrPublicId() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 assertEquals("publicID", docType.attr("publicId"));
 }

 // Test attr method for systemId
 @Test
 public void testAttrSystemId() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 assertEquals("systemID", docType.attr("systemId"));
 }

 // Test attr method for empty publicId and systemId
 @Test
 public void testAttrEmptyPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "", "", null);
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

 // Test attr method for null attribute name
 @Test(expected = NullPointerException.class)
 public void testAttrNullAttributeName() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 docType.attr(null, "value");
 }

 // Test attr method for null attribute value
 @Test
 public void testAttrNullAttributeValue() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 docType.attr("name", null);
 assertNull(docType.attr("name"));
 }
}