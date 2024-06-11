package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributeSet;
import org.jsoup.nodes.attributes.NamedAttribute;
import org.jsoup.nodes.attributes.QuirksMode;
import org.jsoup.nodes.util.DataMap;
import org.jsoup.nodes.util.OutputSettings;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test for DocumentType class
 */
public class DocumentTypeTest {

 private String baseUri = "http://www.example.com";

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "HTML", "HTML", baseUri);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 DocumentType documentType = new DocumentType("html", "", "", baseUri);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "", baseUri);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("PUBLIC ID", documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 DocumentType documentType = new DocumentType("html", "", "SYSTEM ID", baseUri);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("SYSTEM ID", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("PUBLIC ID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("SYSTEM ID", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithNameNull() {
 DocumentType documentType = new DocumentType(null, "PUBLIC ID", "SYSTEM ID", baseUri);
 assertNotNull(documentType);
 assertNull(documentType.attr(DocumentType.NAME));
 assertEquals("PUBLIC ID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("SYSTEM ID", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithNameEmpty() {
 DocumentType documentType = new DocumentType("", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertNotNull(documentType);
 assertNull(documentType.attr(DocumentType.NAME));
 assertEquals("PUBLIC ID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("SYSTEM ID", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreateWithBaseUriNull() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", null);
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreateWithBaseUriEmpty() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", "");
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 document.appendChild(documentType);
 assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", document.body().outerHtml());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 document.appendChild(documentType);
 String html = document.body().outerHtml();
 Document parsed = Parser.parseBodyFragment(html, baseUri);
 Element doctype = parsed.selectFirst("#doctype");
 assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", doctype.outerHtml());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 document.appendChild(documentType);
 String html = document.body().outerHtml();
 Document parsed = Parser.parseBodyFragment(html, baseUri);
 Element doctype = parsed.selectFirst("#doctype");
 assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", doctype.outerHtml());
 }

 @Test(expected = IOException.class)
 public void testDocumentTypeOuterHtmlHeadIOException() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 document.appendChild(documentType);
 document.outerHtmlHead(null, 0, OutputSettings.html4Strict());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasAttributeWithNullAttributeName() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertFalse(documentType.has(null));
 }

 @Test
 public void testHasAttributeWithEmptyAttributeName() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertFalse(documentType.has(""));
 }

 @Test
 public void testHasAttributeWithNonExistentAttribute() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertFalse(documentType.has("nonExistentAttribute"));
 }

 @Test
 public void testAttr() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertEquals("PUBLIC ID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("SYSTEM ID", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testAttrWithNullAttributeName() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertNull(documentType.attr(null));
 }

 @Test
 public void testAttrWithEmptyAttributeName() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertNull(documentType.attr(""));
 }

 @Test
 public void testAttrWithNonExistentAttribute() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 assertNull(documentType.attr("nonExistentAttribute"));
 }

 @Test
 public void testAttrWithAttributeSet() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 AttributeSet attributeSet = new AttributeSet();
 attributeSet.put("newAttribute", "newValue");
 documentType.attr(attributeSet);
 assertEquals("newValue", documentType.attr("newAttribute"));
 }

 @Test
 public void testAttrWithNamedAttribute() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 NamedAttribute namedAttribute = new NamedAttribute("newAttribute", "newValue");
 documentType.attr(namedAttribute);
 assertEquals("newValue", documentType.attr("newAttribute"));
 }

 @Test
 public void testAttrWithDataMap() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 DataMap dataMap = new DataMap();
 dataMap.put("newAttribute", "newValue");
 documentType.attr(dataMap);
 assertEquals("newValue", documentType.attr("newAttribute"));
 }

 @Test
 public void testAttrWithQuirksMode() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 QuirksMode quirksMode = QuirksMode.LAX;
 documentType.attr(quirksMode);
 assertEquals(quirksMode, documentType.attr(QuirksMode.class));
 }

 @Test
 public void testAttrWithMultipleAttributes() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 AttributeSet attributeSet = new AttributeSet();
 attributeSet.put("newAttribute1", "newValue1");
 attributeSet.put("newAttribute2", "newValue2");
 documentType.attr(attributeSet);
 assertEquals("newValue1 newValue2", documentType.attr("newAttribute"));
 }

 @Test
 public void testAttrWithAttributeQuirksMode() {
 DocumentType documentType = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", baseUri);
 QuirksMode quirksMode = QuirksMode.LAX;
 AttributeSet attributeSet = new AttributeSet();
 attributeSet.put(quirksMode, "");
 documentType.attr(attributeSet);
 assertEquals(quirksMode, documentType.attr(QuirksMode.class));
 }
}