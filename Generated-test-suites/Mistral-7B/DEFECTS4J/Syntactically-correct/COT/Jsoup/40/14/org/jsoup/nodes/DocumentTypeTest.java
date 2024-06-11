package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.runner.JUnitRunner;

@RunWith(JUnitRunner.class)
public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document();
 }

 // Test constructor
 @Test
 public void testDocumentTypeConstruction() {
 String name = "html";
 String publicId = "HTML_PRIVATE";
 String systemId = "//W3C//DTD HTML 4.01 Transitional//EN";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.attributes().get("baseUri"));
 }

 // Test empty construction
 @Test
 public void testEmptyDocumentTypeConstruction() {
 DocumentType documentType = new DocumentType("", "", "", "");
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertNull(documentType.attributes().get("baseUri"));
 }

 // Test invalid name
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNameConstruction() {
 String name = "";
 DocumentType documentType = new DocumentType(name, "publicId", "systemId", "");
 }

 // Test outerHtmlHead
 @Test
 public void testOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "HTML_PRIVATE", "//W3C//DTD HTML 4.01 Transitional//EN", "");
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PRIVATE\" //W3C//DTD HTML 4.01 Transitional//EN>\n", documentType.outerHtml());
 }

 // Test outerHtmlHead with name only
 @Test
 public void testOuterHtmlHeadWithNameOnly() {
 DocumentType documentType = new DocumentType("html", "", "", "");
 assertEquals("<!DOCTYPE html>\n", documentType.outerHtml());
 }

 // Test outerHtmlHead with publicId and systemId only
 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemIdOnly() {
 DocumentType documentType = new DocumentType("", "HTML_PRIVATE", "//W3C//DTD HTML 4.01 Transitional//EN", "");
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PRIVATE\" //W3C//DTD HTML 4.01 Transitional//EN>\n", documentType.outerHtml());
 }

 // Test outerHtmlHead with baseUri
 @Test
 public void testOuterHtmlHeadWithBaseUri() {
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType("html", "HTML_PRIVATE", "//W3C//DTD HTML 4.01 Transitional//EN", baseUri);
 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PRIVATE\" //W3C//DTD HTML 4.01 Transitional//EN>\n", documentType.outerHtml());
 assertEquals(baseUri, documentType.attributes().get("baseUri"));
 }

 // Test outerHtmlTail
 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "HTML_PRIVATE", "//W3C//DTD HTML 4.01 Transitional//EN", "");
 assertEquals("", documentType.outerHtmlTail());
 }
}