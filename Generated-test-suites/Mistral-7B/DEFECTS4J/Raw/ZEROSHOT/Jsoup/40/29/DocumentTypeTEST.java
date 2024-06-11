package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTMLTP", "URL", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTMLTP", docType.attr("publicId"));
 assertEquals("URL", docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeToString() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTMLTP", "URL", doc.baseUri());

 assertEquals("<!DOCTYPE html PUBLIC \"HTMLTP\" \"URL\">", docType.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTMLTP", "URL", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"HTMLTP\" \"URL\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTMLTP", "URL", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertNull(accum.toString());
 }

 @Test
 public void testDocumentTypeWithAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("#foo", "HTMLTP", "URL", doc.baseUri());
 docType.attr("foo", "bar");
 docType.attr("baz", "qux");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("#foo", docType.attr("name"));
 assertEquals("HTMLTP", docType.attr("publicId"));
 assertEquals("URL", docType.attr("systemId"));
 assertEquals("bar", docType.attr("foo"));
 assertEquals("qux", docType.attr("baz"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeParsing() {
 Document doc = new Document();
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 Document parsedDoc = Parser.parseBodyFragment(html, doc);
 Node node = parsedDoc.body();

 assertEquals(DocumentType.class, node.getClass());
 DocumentType docType = (DocumentType) node;

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNoAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithQuirksMode() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());
 docType.attr("xml:xml", "true");
 docType.attr("xmlns", "http://www.w3.org/1999/xhtml");

 assertEquals("#doctype", docType.nodeName());
 assertSame(docType, doc.getElementsByTag("![CDATA[1<html xmlns=\"http://www.w3.org/1999/xhtml\" %]>]>", 0));
 }

 @Test
 public void testDocumentTypeWithEmptyAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, doc.baseUri());
 docType.attr("", "");

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithInvalidPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "Invalid:PublicId", null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithInvalidSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "Invalid:SystemId", doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNullArguments() {
 Document doc = new Document();
 DocumentType docType = new DocumentType(null, null, null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithEmptyName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType(StringUtils.EMPTY, null, null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithWhitespaceName() {
 Document doc = new Document();
 DocumentType docType = new DocumentType(" XML ", null, null, doc.baseUri());

 assertEquals("#doctype", docType.nodeName());
 assertEquals(" XML ", docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertEquals(doc.baseUri(), docType.baseUri());
 }
}