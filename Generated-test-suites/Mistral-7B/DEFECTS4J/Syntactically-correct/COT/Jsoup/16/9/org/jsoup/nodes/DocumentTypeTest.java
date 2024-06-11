package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 // Test constructor method
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals("/baseUri", documentType.baseUri());
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 assertEquals("#doctype", documentType.nodeName());
 }

 // Test outerHtmlHead method for normal case
 @Test
 public void testOuterHtmlHeadNormalCase() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlHead(accum, 0, out);
 String expectedOutput = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
 assertEquals(expectedOutput, accum.toString());
 }

 // Test outerHtmlHead method for publicId is blank
 @Test
 public void testOuterHtmlHeadPublicIdBlank() {
 DocumentType documentType = new DocumentType("html", "", "systemId", "/baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlHead(accum, 0, out);
 String expectedOutput = "<!DOCTYPE html SYSTEM systemId>\n";
 assertEquals(expectedOutput, accum.toString());
 }

 // Test outerHtmlHead method for systemId is blank
 @Test
 public void testOuterHtmlHeadSystemIdBlank() {
 DocumentType documentType = new DocumentType("html", "publicId", "", "/baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlHead(accum, 0, out);
 String expectedOutput = "<!DOCTYPE html PUBLIC \"publicId\" \n";
 assertEquals(expectedOutput, accum.toString());
 }

 // Test outerHtmlHead method for publicId and systemId are both blank
 @Test
 public void testOuterHtmlHeadBothBlank() {
 DocumentType documentType = new DocumentType("html", "", "", "/baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlHead(accum, 0, out);
 String expectedOutput = "<!DOCTYPE html>\n";
 assertEquals(expectedOutput, accum.toString());
 }

 // Test outerHtmlHead method for null accumulator
 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadNullAccumulator() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlHead(null, 0, out);
 }

 // Test outerHtmlHead method for null out
 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadNullOut() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 }

 // Test outerHtmlTail method for empty method
 @Test
 public void testOuterHtmlTailEmptyMethod() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "/baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 documentType.outerHtmlTail(accum, 0, out);
 assertEquals("", accum.toString());
 }

}