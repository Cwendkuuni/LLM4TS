package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTypeTest {

 // Test constructor
 @Test
 public void testDocumentTypeConstruction() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "publicId", "systemId", doc.baseUri());

 assertEquals("#doctype", dt.nodeName());
 assertEquals("html", dt.attr("name"));
 assertEquals("publicId", dt.attr("publicId"));
 assertEquals("systemId", dt.attr("systemId"));
 assertEquals(doc.baseUri(), dt.baseUri());
 }

 // Test nodeName method
 @Test
 public void testDocumentTypeNodeName() {
 DocumentType dt = new DocumentType("html", "publicId", "systemId", null);
 assertEquals("#doctype", dt.nodeName());
 }

 // Test outerHtmlHead method
 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 doc.outputSettings().setXhtml(false);
 dt.outerHtmlHead(accum, 0, doc.outputSettings());

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 // Test empty publicId and systemId
 @Test
 public void testDocumentTypeEmptyPublicIdAndSystemId() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "", "", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 doc.outputSettings().setXhtml(false);
 dt.outerHtmlHead(accum, 0, doc.outputSettings());

 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 // Test null baseUri
 @Test (expected = NullPointerException.class)
 public void testDocumentTypeNullBaseUri() {
 DocumentType dt = new DocumentType("html", "publicId", "systemId", null);
 }

 // Test invalid name
 @Test (expected = IllegalArgumentException.class)
 public void testDocumentTypeInvalidName() {
 Document doc = new Document();
 Validate.isNotNull(doc);
 Validate.isTrue(doc instanceof Document);
 DocumentType dt = new DocumentType(null, "publicId", "systemId", doc.baseUri());
 }

 // Test null attributes
 @Test
 public void testDocumentTypeNullAttributes() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 Attributes attrs = null;
 dt.attr(attrs, "name", "html5");
 }

 // Test setName with existing name
 @Test
 public void testDocumentTypeSetNameWithExistingName() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 dt.attr("name", "html");
 dt.attr("name", "html5");
 assertEquals("html5", dt.attr("name"));
 }

 // Test setPublicId with existing publicId
 @Test
 public void testDocumentTypeSetPublicIdWithExistingPublicId() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "publicId1", "systemId", doc.baseUri());
 dt.attr("publicId", "publicId");
 dt.attr("publicId", "publicId1");
 assertEquals("publicId1", dt.attr("publicId"));
 }

 // Test setSystemId with existing systemId
 @Test
 public void testDocumentTypeSetSystemIdWithExistingSystemId() {
 Document doc = new Document();
 DocumentType dt = new DocumentType("html", "publicId", "systemId1", doc.baseUri());
 dt.attr("systemId", "systemId");
 dt.attr("systemId", "systemId1");
 assertEquals("systemId1", dt.attr("systemId"));
 }

 // Test equals
 @Test
 public void testDocumentTypeEquals() {
 Document doc = new Document();
 DocumentType dt1 = new DocumentType("html", "publicId1", "systemId1", doc.baseUri());
 DocumentType dt2 = new DocumentType("html", "publicId1", "systemId1", doc.baseUri());
 DocumentType dt3 = new DocumentType("html", "publicId1", "systemId2", doc.baseUri());

 assertSame(dt1, dt1);
 assertSame(dt1, dt2); // equals method should check if objects are the same
 assertNotSame(dt1, dt3);
 }
}