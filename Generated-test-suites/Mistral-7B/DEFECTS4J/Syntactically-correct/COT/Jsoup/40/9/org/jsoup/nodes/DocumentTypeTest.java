package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 // Test constructor
 @Test
 public void testDocumentTypeConstructor() {
 String name = "html";
 String publicId = " -//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://www.example.com/";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
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
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 // Test outerHtmlHead method with empty name
 @Test (expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadEmptyName() {
 DocumentType docType = new DocumentType("", "", "", "");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);
 }

 // Test outerHtmlHead method with null accumulator
 @Test (expected = NullPointerException.class)
 public void testOuterHtmlHeadNullAccumulator() {
 DocumentType docType = new DocumentType("html", "", "", "");
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(null, 0, out);
 }

 // Test outerHtmlHead method with null out
 @Test
 public void testOuterHtmlHeadNullOut() {
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = null;
 docType.outerHtmlHead(accum, 0, out);
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlTail(accum, 0, out);

 assertEquals("", accum.toString());
 }

 // Test equals method
 @Test
 public void testEquals() {
 DocumentType docType1 = new DocumentType("html", "", "", "");
 DocumentType docType2 = new DocumentType("html", "", "", "");
 assertEquals(docType1, docType2);
 }

 // Test equals method with different name
 @Test
 public void testEqualsDifferentName() {
 DocumentType docType1 = new DocumentType("html", "", "", "");
 DocumentType docType2 = new DocumentType("head", "", "", "");
 assertNotSame(docType1, docType2);
 }

 // Test equals method with same object
 @Test
 public void testEqualsSameObject() {
 DocumentType docType = new DocumentType("html", "", "", "");
 assertEquals(docType, docType);
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 DocumentType docType = new DocumentType("html", "", "", "");
 int hashCode = docType.hashCode();
 DocumentType docType2 = new DocumentType("html", "", "", "");
 assertEquals(hashCode, docType2.hashCode());
 }

 // Test toString method
 @Test
 public void testToString() {
 DocumentType docType = new DocumentType("html", "", "", "");
 String string = docType.toString();
 assertTrue(string.contains("#doctype"));
 assertTrue(string.contains("name"));
 assertTrue(string.contains("publicId"));
 assertTrue(string.contains("systemId"));
 assertTrue(string.contains("baseUri"));
 }

 // Test cloneNode method
 @Test
 public void testCloneNode() {
 DocumentType docType = new DocumentType("html", "", "", "");
 DocumentType clonedDocType = (DocumentType) docType.cloneNode(true);
 assertEquals(docType, clonedDocType);
 }

 // Test cloneNode method with deep cloning
 @Test
 public void testCloneNodeDeep() {
 DocumentType docType = new DocumentType("html", "", "", "");
 DocumentType clonedDocType = (DocumentType) docType.cloneNode(true);
 assertEquals(docType, clonedDocType);
 Document doc = document.parse("<html></html>");
 Element element = doc.selectFirst("html");
 element.appendChild(docType.cloneNode(false));
 assertEquals(docType, element.child(0));
 assertEquals(docType, clonedDocType.cloneNode(false));
 }

 // Test cloneNode method with shallow cloning
 @Test
 public void testCloneNodeShallow() {
 DocumentType docType = new DocumentType("html", "", "", "");
 DocumentType clonedDocType = (DocumentType) docType.cloneNode(false);
 assertNotSame(docType, clonedDocType);
 assertEquals(docType.attr("name"), clonedDocType.attr("name"));
 assertEquals(docType.attr("publicId"), clonedDocType.attr("publicId"));
 assertEquals(docType.attr("systemId"), clonedDocType.attr("systemId"));
 assertEquals(docType.baseUri(), clonedDocType.baseUri());
 }

 // Test cloneNode method with null parent
 @Test (expected = NullPointerException.class)
 public void testCloneNodeNullParent() {
 DocumentType docType = new DocumentType("html", "", "", "");
 docType.cloneNode(null);
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 DocumentType docType = new DocumentType("html", "", "", "");
 assertFalse(docType.isEmpty());
 }

 // Test toString method with quirks mode
 @Test
 public void testToStringQuirksMode() {
 DocumentType docType = new DocumentType("html", "DTD_HTML_1.0", "DTDID_HTML_1.0", "");
 assertEquals("<!DOCTYPE html PUBLIC \"DTD_HTML_1.0\" \"DTDID_HTML_1.0\">", docType.toString());
 }
}