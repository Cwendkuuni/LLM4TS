package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 // Test constructor
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals("baseUri", documentType.baseUri().toString());
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 }

 // Test outerHtmlHead method (happy path)
 @Test
 public void testOuterHtmlHeadHappyPath() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 1, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method (publicId is blank)
 @Test
 public void testOuterHtmlHeadPublicIdBlank() {
 DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 1, null);
 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method (systemId is blank)
 @Test
 public void testOuterHtmlHeadSystemIdBlank() {
 DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 1, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"\">";
 assertEquals(expected, accum.toString());
 }

 // Test outerHtmlHead method (name is blank)
 @Test
 public void testOuterHtmlHeadNameBlank() {
 DocumentType documentType = new DocumentType("", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 1, null);
 assertNull(accum.toString());
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 1, null);
 assertEquals("", accum.toString());
 }

 // Test constructor with empty String for name, publicId, systemId and baseUri
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyStrings() {
 DocumentType documentType = new DocumentType("", "", "", "");
 }

 // Test constructor with null name
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullName() {
 DocumentType documentType = new DocumentType(null, "publicId", "systemId", "baseUri");
 }

 // Test constructor with null publicId
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullPublicId() {
 DocumentType documentType = new DocumentType("name", null, "systemId", "baseUri");
 }

 // Test constructor with null systemId
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullSystemId() {
 DocumentType documentType = new DocumentType("name", "publicId", null, "baseUri");
 }

 // Test constructor with null baseUri
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBaseUri() {
 DocumentType documentType = new DocumentType("name", "publicId", "systemId", null);
 }
}