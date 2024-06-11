package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

 private Document createDoc() {
 return new Document();
 }

 @Test
 public void testDocumentTypeConstructor() {
 String name = "html";
 String publicId = " -//W3C//DTD HTML 4.01//EN";
 String systemId = "http://www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://www.example.com";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testEmptyConstructor() {
 DocumentType docType = new DocumentType();
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", "", "", "");

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", " -//W3C//DTD HTML 4.01//EN", "", "");

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithSystemId() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "");

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\"", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNamePublicIdAndSystemId() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", " -//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullName() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType(null, "", "", "");
 }

 @Test(expected = NullPointerException.class)
 public void testNullPublicId() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", null, "", "");
 }

 @Test(expected = NullPointerException.class)
 public void testNullSystemId() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", "", null, "");
 }

 @Test
 public void testOuterHtmlTail() {
 Document doc = createDoc();
 DocumentType docType = new DocumentType("html", "", "", "");

 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertTrue(accum.toString().isEmpty());
 }
}