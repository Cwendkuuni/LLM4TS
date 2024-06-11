package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
 doc.appendChild(docType);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertFalse(docType.attr(DocumentType.PUBLIC_ID).isSet());
 assertFalse(docType.attr(DocumentType.SYSTEM_ID).isSet());

 Attribute publicIdAttr = docType.attr(DocumentType.PUBLIC_ID);
 Attribute systemIdAttr = docType.attr(DocumentType.SYSTEM_ID);

 assertNull(publicIdAttr);
 assertNull(systemIdAttr);
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "public -//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
 doc.appendChild(docType);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertTrue(docType.attr(DocumentType.PUBLIC_ID).isSet());
 assertEquals("public -//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID).toString());
 assertFalse(docType.attr(DocumentType.SYSTEM_ID).isSet());
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, "http://www.w3.org/TR/html4/strict.dtd");
 doc.appendChild(docType);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertFalse(docType.attr(DocumentType.PUBLIC_ID).isSet());
 assertTrue(docType.attr(DocumentType.SYSTEM_ID).isSet());
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID).toString());
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "DTD HTML", "public -//Some Company//DTD HTML 4.0//EN", "http://www.example.com/dtds/html4.dtd");
 doc.appendChild(docType);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertTrue(docType.attr(DocumentType.PUBLIC_ID).isSet());
 assertEquals("public -//Some Company//DTD HTML 4.0//EN", docType.attr(DocumentType.PUBLIC_ID).toString());
 assertTrue(docType.attr(DocumentType.SYSTEM_ID).isSet());
 assertEquals("http://www.example.com/dtds/html4.dtd", docType.attr(DocumentType.SYSTEM_ID).toString());
 }

 @Test
 public void testDocumentTypeOuterHtml() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "public -//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
 doc.appendChild(docType);

 String html = docType.outerHtml();
 String expected = "<!DOCTYPE html PUBLIC \"public -//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 assertEquals(expected, html);
 }

 @Test
 public void testDocumentTypeOuterHtmlWithHtml5() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);

 String html = docType.outerHtml();
 String expected = "<!doctype html>";
 assertEquals(expected, html);
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);

 assertTrue(docType.has(DocumentType.NAME));
 assertFalse(docType.has("nonExistentAttribute"));
 }
}