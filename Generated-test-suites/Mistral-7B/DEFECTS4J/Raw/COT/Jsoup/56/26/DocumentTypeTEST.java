package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.ElementHelper;
import org.jsoup.helper.OutputSettings;
import org.jsoup.helper.JsoupHelper;

import org.junit.Test;
import org.junit.Before;

public class DocumentTypeTest {

 private DocumentType documentType;
 private Document document;

 @Before
 public void setUp() {
 document = JsoupHelper.parse("<html/>", "UTF-8");
 documentType = new DocumentType("doctypeName", "publicId", "systemId", document.baseUri());
 document.appendChild(documentType);
 }

 // Test constructor method
 @Test
 public void testDocumentTypeConstructor() {
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("doctypeName", documentType.attr(NAME));
 assertEquals("publicId", documentType.attr(PUBLIC_ID));
 assertEquals("systemId", documentType.attr(SYSTEM_ID));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 assertEquals("#doctype", documentType.nodeName());
 }

 // Test outerHtmlHead method
 @Test
 public void testOuterHtmlHead() throws Exception {
 OutputSettings out = new OutputSettings();
 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlHead(sb, 0, out);

 if (out.syntax() == OutputSettings.Syntax.html && !documentType.has(DocumentType.PUBLIC_ID) && !documentType.has(DocumentType.SYSTEM_ID)) {
 String expectedHtml5 = "<!doctype html>\n";
 String actualHtml5 = sb.toString();
 assertEquals(expectedHtml5, actualHtml5);
 } else {
 String expectedDoctype = "<!DOCTYPE " + documentType.attr(DocumentType.NAME) + ">";
 String actualDoctype = sb.toString();
 assertEquals(expectedDoctype, actualDoctype);
 }
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() throws Exception {
 StringBuilder sb = new StringBuilder();
 documentType.outerHtmlTail(sb, 0, new OutputSettings());
 assertNull(sb.toString());
 }

 // Test has method
 @Test
 public void testHasMethod() {
 assertFalse(documentType.has("nonExistentAttribute"));
 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 // Test edge cases and exception handling

 // Test empty constructor
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyConstructor() {
 new DocumentType("", "", "", "");
 }

 // Test empty name
 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 new DocumentType("", "", "", document.baseUri());
 }

 // Test null baseUri
 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 new DocumentType("name", "", "", null);
 }

 // Test null document
 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 new DocumentType("name", "", "", null);
 }

 // Test duplicate attributes
 @Test
 public void testDuplicateAttributes() {
 documentType.attr(DocumentType.NAME, "newName");
 documentType.attr(DocumentType.NAME, "anotherName");
 assertEquals("anotherName", documentType.attr(DocumentType.NAME));
 }
}