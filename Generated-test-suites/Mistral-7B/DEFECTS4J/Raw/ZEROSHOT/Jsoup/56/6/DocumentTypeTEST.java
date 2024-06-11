package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 DocumentType docType = new DocumentType("html", "HTML", null, null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 DocumentType docType = new DocumentType("html", "PUBLIC -1234567890123456789012345678901234567890", null, null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("PUBLIC -1234567890123456789012345678901234567890", docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 DocumentType docType = new DocumentType("html", null, "http://example.com/test.dtd", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://example.com/test.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "PUBLIC -1234567890123456789012345678901234567890", "http://example.com/test.dtd", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("PUBLIC -1234567890123456789012345678901234567890", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://example.com/test.dtd", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 DocumentType docType = new DocumentType("html", "PUBLIC -1234567890123456789012345678901234567890", "http://example.com/test.dtd", null);

 assertTrue(docType.has(DocumentType.NAME));
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertTrue(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithoutAttributes() {
 DocumentType docType = new DocumentType("html", null, null, null);

 assertTrue(docType.has(DocumentType.NAME));
 assertFalse(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testParseDocumentType() throws Exception {
 Parser parser = new Parser();
 String html = "<!DOCTYPE html>";
 String htmlWithPublicId = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
 String htmlWithSystemId = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 String htmlWithName = "<!DOCTYPE name>";
 String htmlWithNameAndPublicId = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" name=\"myDOCTYPE\">";
 String htmlWithNameAndSystemId = "<!DOCTYPE html SYSTEM \"about:legacy-compat\" name=\"myDOCTYPE\">";

 Document doc = parser.parseBodyFragment(html);
 Node node = doc.selectFirst("#doctype");
 assertEquals(DocumentType.class, node.getClass());

 Document docWithPublicId = parser.parseBodyFragment(htmlWithPublicId);
 Node nodeWithPublicId = docWithPublicId.selectFirst("#doctype");
 assertEquals(DocumentType.class, nodeWithPublicId.getClass());
 assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", nodeWithPublicId.attr("publicId"));
 assertEquals(null, nodeWithPublicId.attr("systemId"));

 Document docWithSystemId = parser.parseBodyFragment(htmlWithSystemId);
 Node nodeWithSystemId = docWithSystemId.selectFirst("#doctype");
 assertEquals(DocumentType.class, nodeWithSystemId.getClass());
 assertEquals(null, nodeWithSystemId.attr("publicId"));
 assertEquals("about:legacy-compat", nodeWithSystemId.attr("systemId"));

 Document docWithName = parser.parseBodyFragment(htmlWithName);
 Node nodeWithName = docWithName.selectFirst("#doctype");
 assertEquals(DocumentType.class, nodeWithName.getClass());
 assertEquals(null, nodeWithName.attr("publicId"));
 assertEquals(null, nodeWithName.attr("systemId"));
 assertEquals("name", nodeWithName.attr(DocumentType.NAME));

 Document docWithNameAndPublicId = parser.parseBodyFragment(htmlWithNameAndPublicId);
 Node nodeWithNameAndPublicId = docWithNameAndPublicId.selectFirst("#doctype");
 assertEquals(DocumentType.class, nodeWithNameAndPublicId.getClass());
 assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", nodeWithNameAndPublicId.attr("publicId"));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", nodeWithNameAndPublicId.attr("systemId"));
 assertEquals("myDOCTYPE", nodeWithNameAndPublicId.attr(DocumentType.NAME));

 Document docWithNameAndSystemId = parser.parseBodyFragment(htmlWithNameAndSystemId);
 Node nodeWithNameAndSystemId = docWithNameAndSystemId.selectFirst("#doctype");
 assertEquals(DocumentType.class, nodeWithNameAndSystemId.getClass());
 assertEquals(null, nodeWithNameAndSystemId.attr("publicId"));
 assertEquals("about:legacy-compat", nodeWithNameAndSystemId.attr("systemId"));
 assertEquals("myDOCTYPE", nodeWithNameAndSystemId.attr(DocumentType.NAME));
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String expected = "<!doctype html>";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1234567890123456789012345678901234567890", null, null);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String expected = "<!DOCTYPE html PUBLIC \"-1234567890123456789012345678901234567890\" \"\">";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithSystemId() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "http://example.com/test.dtd", null);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String expected = "<!DOCTYPE html SYSTEM \"http://example.com/test.dtd\">";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithName() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 Element nameElement = new Element("html");
 nameElement.attr("name", "myDOCTYPE");
 doc.appendChild(nameElement);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String expected = "<!DOCTYPE html>\n<html name=\"myDOCTYPE\"></html>";
 String actual = sb.toString();
 assertEquals(expected.replaceAll("\n", ""), actual.replaceAll("\n", ""));
 }

 @Test
 public void testOuterHtmlHeadWithNamePublicId() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "PUBLIC -1234567890123456789012345678901234567890", null, null);
 Element nameElement = new Element("html");
 nameElement.attr("name", "myDOCTYPE");
 doc.appendChild(nameElement);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String expected = "<!DOCTYPE html PUBLIC \"-1234567890123456789012345678901234567890\" \"\">\n<html name=\"myDOCTYPE\"></html>";
 String actual = sb.toString();
 assertEquals(expected.replaceAll("\n", ""), actual.replaceAll("\n", ""));
 }

 @Test
 public void testOuterHtmlHeadWithNameSystemId() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, "http://example.com/test.dtd", null);
 Element nameElement = new Element("html");
 nameElement.attr("name", "myDOCTYPE");
 doc.appendChild(nameElement);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String expected = "<!DOCTYPE html SYSTEM \"http://example.com/test.dtd\">\n<html name=\"myDOCTYPE\"></html>";
 String actual = sb.toString();
 assertEquals(expected.replaceAll("\n", ""), actual.replaceAll("\n", ""));
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", null, null, null);
 doc.appendChild(docType);

 StringBuilder sb = new StringBuilder();
 doc.outerHtml(sb, null);
 String actual = sb.toString();
 assertEquals("<!doctype html>", actual.substring(0, actual.indexOf('>')));
 }
}