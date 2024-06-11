package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Node;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Converter.Imports;
import org.jsoup.nodes.Document.OutputSettings;

import java.io.IOException;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML", " html", "http://www.w3.org/TR/html4/strict.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("HTML", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals(" html", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType docType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithOnlyName() {
 DocumentType docType = new DocumentType("html", null, null, null);
 docType.attr(DocumentType.NAME, "html5");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html5", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 DocumentType docType = new DocumentType("html", "PUBLIC HTML", " html", "http://www.w3.org/TR/html4/strict.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("PUBLIC HTML", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals(" html", docType.attr(DocumentType.SYSTEM_ID));
 assertNull(docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 DocumentType docType = new DocumentType("html", null, "SYSTEM html", "http://www.w3.org/TR/html4/strict.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithQuirksMode() {
 DocumentType docType = new DocumentType("html", null, null, "about:legacy-compat");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("about:legacy-compat", docType.attr("baseUri"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC HTML", " html", "http://www.w3.org/TR/html4/strict.dtd");
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlHead(out, 0, OutputSettings.html5());
 assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC HTML\" \" html\">\n", out.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithEmptyAttributes() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlHead(out, 0, OutputSettings.html5());
 assertEquals("<!doctype html>\n", out.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithHtml5() throws IOException {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlHead(out, 0, OutputSettings.html());
 assertEquals("<!doctype html>\n", out.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 // test with no tail
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlTail(out, 0, OutputSettings.html5());
 assertEquals("", out.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTailWithEmptyAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlTail(out, 0, OutputSettings.html5());
 assertEquals("", out.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTailWithHtml5() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlTail(out, 0, OutputSettings.html());
 assertEquals("", out.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTailWithQuirksMode() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, "about:legacy-compat");
 doc.appendChild(docType);
 Element html = doc.createElement("html");
 doc.appendChild(html);
 doc.appendChild(new TextNode("\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n"));
 Attributes htmlAttrs = html.attributes();
 htmlAttrs.put("xmlns", "http://www.w3.org/1999/xhtml");
 doc.appendChild(html);

 Appendable out = new StringBuffer();
 docType.outerHtmlTail(out, 0, OutputSettings.html5());
 assertEquals("", out.toString());
 }

 @Test
 public void testHasAttribute() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 assertEquals(true, docType.has(DocumentType.NAME));
 assertEquals(true, docType.has(DocumentType.PUBLIC_ID));
 assertEquals(true, docType.has(DocumentType.SYSTEM_ID));
 assertEquals(false, docType.has("test"));
 }
}