package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithNullName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType(null, "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertEquals("HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeToString() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN\" >", docType.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN\" >", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testDocumentTypeWithAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());
 docType.attr("foo", "bar");
 docType.attr("qux", "quux");

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN\" >", docType.toString());
 assertEquals(2, docType.attributeSize());

 Attribute foo = docType.attr("foo");
 Attribute qux = docType.attr("qux");

 assertEquals("bar", foo.getValue());
 assertEquals("quux", qux.getValue());
 }

 @Test
 public void testDocumentTypeWithCustomNodeName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());
 docType.name("customName");

 assertEquals("customName", docType.nodeName());
 }

 @Test
 public void testDocumentTypeWithEmptyStringPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "", "", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeWithNullName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType(null, "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeWithNullPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "", doc.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeWithNullSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", null, doc.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeWithNullBaseUri() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", null);
 }

 @Test
 public void testCreateDocumentTypeWithAttributesAndContent() {
 Document doc = new Document();
 Element root = new Element("html");
 TextNode textNode = new TextNode("");
 root.appendChild(textNode);
 doc.appendChild(root);
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN", "", doc.baseUri());
 docType.attr("foo", "bar");
 docType.attr("qux", "quux");
 docType.appendChild(root);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_-_W3C_org_DTD_HTML_4_01_transitional_EN\" ><html></html>", doc.toString());
 assertEquals(2, docType.attributeSize());

 Attribute foo = docType.attr("foo");
 Attribute qux = docType.attr("qux");

 assertEquals("bar", foo.getValue());
 assertEquals("quux", qux.getValue());
 }
}