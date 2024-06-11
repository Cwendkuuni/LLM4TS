package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Before;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DocumentTypeTest {

 private DocumentType docType;

 @Before
 public void setUp() {
 docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 }

 // Test constructor
 @Test
 public void testDocumentTypeConstructor() {
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("publicId", docType.attr("publicId"));
 assertEquals("systemId", docType.attr("systemId"));
 assertEquals("baseUri", docType.baseUri().toString());
 }

 // Test nodeName method
 @Test
 public void testNodeNameMethod() {
 assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method
 @Test
 public void testOuterHtmlHeadMethod() {
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html", accum.toString());
 if (!docType.attr("publicId").isEmpty()) {
 assertEquals(" PUBLIC \"publicId\"", accum.toString().substring(accum.toString().lastIndexOf(" ")));
 }
 if (!docType.attr("systemId").isEmpty()) {
 assertEquals(" \"systemId\"", accum.toString().substring(accum.toString().lastIndexOf(" ")+1));
 }
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTailMethod() {
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 docType.outerHtmlTail(accum, 0, out);

 assertEquals("", accum.toString());
 }

 // Test empty constructor
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyConstructor() {
 docType = new DocumentType();
 }

 // Test null name
 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 docType = new DocumentType(null, "publicId", "systemId", "baseUri");
 }

 // Test null publicId
 @Test
 public void testNullPublicId() {
 docType = new DocumentType("name", null, "systemId", "baseUri");
 assertNull(docType.attr("publicId"));
 }

 // Test null systemId
 @Test
 public void testNullSystemId() {
 docType = new DocumentType("name", "publicId", null, "baseUri");
 assertNull(docType.attr("systemId"));
 }

 // Test null baseUri
 @Test
 public void testNullBaseUri() {
 docType = new DocumentType("name", "publicId", "systemId", null);
 assertThrows(IllegalArgumentException.class, () -> docType.attr("baseUri", "uri"));
 }

 // Test empty publicId
 @Test
 public void testEmptyPublicId() {
 docType = new DocumentType("name", "", "systemId", "baseUri");
 assertEquals("", docType.attr("publicId"));
 }

 // Test empty systemId
 @Test
 public void testEmptySystemId() {
 docType = new DocumentType("name", "publicId", "", "baseUri");
 assertEquals("", docType.attr("systemId"));
 }
}