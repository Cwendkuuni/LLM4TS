package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.test.TestBase;
import org.junit.Test;

public class DocumentTypeTest extends TestBase {

 private static final String DOCTYPE_NAME = "html";
 private static final String DOCTYPE_PUBLIC_ID = "[-//W3C//DTD HTML 4.01//EN]";
 private static final String DOCTYPE_SYSTEM_ID = "http://www.w3.org/TR/html4/strict.dtd";

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, doc.baseUri());

 assertNotNull(doctype);
 assertEquals("#doctype", doctype.nodeName());
 }

 @Test
 public void testDocumentTypeCreationWithoutIds() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(DOCTYPE_NAME, null, null, doc.baseUri());

 assertNotNull(doctype);
 assertEquals("#doctype", doctype.nodeName());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("testName", null, null, doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("testName", doctype.attr(NAME));
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, doc.baseUri());

 Element html = new Element("html");
 doc.appendChild(html);
 doc.insertBefore(doctype, html);

 StringBuilder sb = new StringBuilder();
 doctype.outerHtmlHead(sb, 0, null);

 assertEquals("<!DOCTYPE " + DOCTYPE_NAME + " PUBLIC \"" + DOCTYPE_PUBLIC_ID + "\" " + DOCTYPE_SYSTEM_ID + ">\n", sb.toString());
 }

 @Test
 public void testOuterHtmlHeadHtml5() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(DOCTYPE_NAME, null, null, doc.baseUri());

 Element html = new Element("html");
 doc.appendChild(html);
 doc.insertBefore(doctype, html);

 StringBuilder sb = new StringBuilder();
 doctype.outerHtmlHead(sb, 0, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html5));

 assertEquals("<!doctype html>\n", sb.toString());
 }

 @Test
 public void testOuterHtmlHeadEmpty() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(null, null, null, doc.baseUri());

 StringBuilder sb = new StringBuilder();
 doctype.outerHtmlHead(sb, 0, null);

 assertNull(sb.toString());
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadNullDocument() throws Exception {
 DocumentType doctype = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, null);

 StringBuilder sb = new StringBuilder();
 doctype.outerHtmlHead(sb, 0, null);
 }

 @Test
 public void testHasAttribute() {
 DocumentType doctype = new DocumentType(DOCTYPE_NAME, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, null);

 assertTrue(doctype.has(NAME));
 assertFalse(doctype.has("nonExistentAttribute"));
 }

 @Test
 public void testHasAttributeWithNullName() {
 DocumentType doctype = new DocumentType(null, DOCTYPE_PUBLIC_ID, DOCTYPE_SYSTEM_ID, null);

 assertFalse(doctype.has(NAME));
 }

}