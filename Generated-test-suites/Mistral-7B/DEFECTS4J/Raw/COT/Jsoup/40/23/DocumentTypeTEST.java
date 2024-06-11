package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 // Test constructor
 @Test
 public void testDocumentTypeConstruction() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 Assert.assertEquals("#doctype", docType.nodeName());
 Assert.assertEquals("html", docType.attr("name"));
 Assert.assertEquals("publicId", docType.attr("publicId"));
 Assert.assertEquals("systemId", docType.attr("systemId"));
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType docType = new DocumentType("html", "", "", null);
 Assert.assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method - normal case
 @Test
 public void testOuterHtmlHeadNormalCase() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 Assert.assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method - publicId is blank
 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadBlankPublicId() {
 DocumentType docType = new DocumentType("html", "", "systemId", null);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 }

 // Test outerHtmlHead method - systemId is blank
 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadBlankSystemId() {
 DocumentType docType = new DocumentType("html", "publicId", "", null);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 }

 // Test outerHtmlHead method - baseUri is not null
 @Test
 public void testOuterHtmlHeadWithBaseUri() {
 String baseUri = "http://example.com/";
 DocumentType docType = new DocumentType("html", "publicId", "systemId", baseUri);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\" >";
 Assert.assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method - name is empty
 @Test
 public void testOuterHtmlHeadEmptyName() {
 DocumentType docType = new DocumentType("", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 Assert.assertEquals("", accum.toString());
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);
 Assert.assertEquals("", accum.toString());
 }
}