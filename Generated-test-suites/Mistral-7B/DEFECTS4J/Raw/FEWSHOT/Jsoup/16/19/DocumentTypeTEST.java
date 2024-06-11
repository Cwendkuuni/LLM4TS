package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert. assertNotSame;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_VERSION", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_VERSION", docType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
 assertEquals(null, docType.attr("baseUri"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.attr("baseUri"));
 }

 @Test
 public void testOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_VERSION", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

 docType.outerHtmlHead(accum, 1, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_VERSION\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemId() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_VERSION_1", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

 docType.outerHtmlHead(accum, 1, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_VERSION_1\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithoutPublicId() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/strict.dtd", null);

 docType.outerHtmlHead(accum, 1, null);

 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNullBaseUri() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);

 docType.outerHtmlHead(accum, 1, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 StringBuilder accum = new StringBuilder();
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);

 docType.outerHtmlTail(accum, 1, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testEquals() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", null, null, null);
 DocumentType docType2 = new DocumentType("html", null, null, null);

 assertEquals(docType1, docType2);
 }

 @Test
 public void testNotEquals() {
 Document doc = new Document();
 DocumentType docType1 = new DocumentType("html", null, null, null);
 DocumentType docType2 = new DocumentType("html", null, null, null);
 DocumentType docType3 = new DocumentType("html", null, null, null);
 docType3.attr("name", "html5");

 assertNotSame(docType1, docType2);
 assertNotSame(docType1, docType3);
 }

 @Test
 public void testNullDocumentType() {
 assertNull(new DocumentType(null, null, null, null));
 }

 @Test
 public void testNullName() {
 try {
 new DocumentType(null, null, null, null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testNullPublicId() {
 DocumentType docType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("publicId"));
 }

 @Test
 public void testNullSystemId() {
 DocumentType docType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("systemId"));
 }

 @Test
 public void testNullBaseUri() {
 DocumentType docType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("baseUri"));
 }
}