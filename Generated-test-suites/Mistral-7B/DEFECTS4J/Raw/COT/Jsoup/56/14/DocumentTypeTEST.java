package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document.OutputSettings;

import org.junit.Test;

public class DocumentTypeTest {

 // Test constructor method
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType("html", "html", "html", null);
 assertEquals("#doctype", docType.nodeName());
 Assert.assertEquals("html", docType.attr(DocumentType.NAME));
 Assert.assertNull(docType.attr(DocumentType.PUBLIC_ID));
 Assert.assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType docType = new DocumentType("html", "html", "html", null);
 assertEquals("#doctype", docType.nodeName());
 }

 // Test outerHtmlHead method for HTML5 doctype
 @Test
 public void testOuterHtmlHeadForHtml5Doctype() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);
 doc.outputSettings(OutputSettings.syntax(OutputSettings.Syntax.html5));
 String expected = "<!doctype html>";
 doc.select("head > #doctype").first().outerHtml();
 assertEquals(expected, doc.body().html());
 }

 // Test outerHtmlHead method for non-HTML5 doctype
 @Test
 public void testOuterHtmlHeadForNonHtml5Doctype() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "html", "html", null);
 doc.appendChild(docType);
 doc.outputSettings(OutputSettings.syntax(OutputSettings.Syntax.xml));
 String expected = "<!DOCTYPE html PUBLIC \"html\" \"html.dtd\">";
 doc.select("head > #doctype").first().outerHtml();
 assertEquals(expected, doc.body().html());
 }

 // Test outerHtmlHead method for empty doctype
 @Test
 public void testOuterHtmlHeadForEmptyDoctype() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType(null, null, null, null);
 doc.appendChild(docType);
 doc.outputSettings(OutputSettings.syntax(OutputSettings.Syntax.xml));
 String expected = "<!DOCTYPE>";
 doc.select("head > #doctype").first().outerHtml();
 assertEquals(expected, doc.body().html());
 }

 // Test has method for existent attribute
 @Test
 public void testHasForExistentAttribute() {
 DocumentType docType = new DocumentType("html", "html", "html", null);
 assertTrue(docType.has(DocumentType.NAME));
 }

 // Test has method for non-existent attribute
 @Test
 public void testHasForNonExistentAttribute() {
 DocumentType docType = new DocumentType("html", "html", "html", null);
 assertFalse(docType.has("nonExistentAttribute"));
 }

}