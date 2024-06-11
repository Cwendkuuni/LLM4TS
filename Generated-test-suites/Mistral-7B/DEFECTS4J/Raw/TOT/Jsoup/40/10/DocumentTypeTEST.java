package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document();
 }

 /**
 * Test creating a new DocumentType with valid parameters.
 */
 @Test
 public void testValidDocumentTypeCreation() {
 String name = "html";
 String publicId = "{-//W3C//DTD XHTML 1.0 Strict//EN}";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com/";
 DocumentType html = new DocumentType(name, publicId, systemId, baseUri);

 // Basic checks
 assertEquals("#doctype", html.nodeName());
 assertEquals("html", html.attr("name"));
 assertEquals(publicId, html.attr("publicId"));
 assertEquals(systemId, html.attr("systemId"));
 assertEquals(baseUri, html.baseUri());
 }

 /**
 * Test creating a new DocumentType with an empty name.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyNameDocumentTypeCreation() {
 String name = "";
 String publicId = "test";
 String systemId = "test";
 String baseUri = "test";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 fail("Expected an IllegalArgumentException but did not receive one.");
 }

 /**
 * Test creating a new DocumentType with an outerHTML representation.
 */
 @Test
 public void testDocumentTypeOuterHtml() {
 DocumentType html = new DocumentType("html", "", "", "");

 StringBuilder accum = new StringBuilder();
 html.outerHtmlHead(accum, 0, null);
 html.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 /**
 * Test creating a new DocumentType with attributes.
 */
 @Test
 public void testDocumentTypeWithAttributes() {
 String name = "xhtml";
 String publicId = "test";
 String systemId = "test";
 String baseUri = "test";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 docType.attr("xmlns", "http://www.w3.org/1999/xhtml");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("xhtml", docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 assertEquals("http://www.w3.org/1999/xhtml", docType.attr("xmlns"));
 }

 /**
 * Test creating a new DocumentType with null attributes.
 */
 @Test
 public void testDocumentTypeWithNullAttributes() {
 String name = "test";
 String publicId = "test";
 String systemId = "test";
 String baseUri = "test";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
 docType.attr(null, null);

 // Basic checks
 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 // Check if the null attributes were properly discarded
 assertNull(docType.attributes());
 }
}