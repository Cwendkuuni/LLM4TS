package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributeSet;
import org.jsoup.nodes.attributes.NamedAttribute;
import org.jsoup.nodes.attributes.QuirksMode;
import org.jsoup.nodes.attributes.Syntax;
import org.jsoup.helper.StringUtil;

import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document();
 }

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType doctype = new DocumentType("html", "HTML", "html", doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(DocumentType.NAME));
 assertNull(doctype.attr(DocumentType.PUBLIC_ID));
 assertNull(doctype.attr(DocumentType.SYSTEM_ID));
 assertNull(doctype.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithPublicId() {
 DocumentType doctype = new DocumentType("html", "HTML PUBLIC", "publicId", doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(DocumentType.NAME));
 assertEquals("publicId", doctype.attr(DocumentType.PUBLIC_ID));
 assertNull(doctype.attr(DocumentType.SYSTEM_ID));
 assertNull(doctype.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithSystemId() {
 DocumentType doctype = new DocumentType("html", "HTML SYSTEM", null, "systemId", doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(DocumentType.NAME));
 assertNull(doctype.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", doctype.attr(DocumentType.SYSTEM_ID));
 assertNotNull(doctype.baseUri());
 }

 @Test
 public void testDocumentTypeConstructorWithPublicIdAndSystemId() {
 DocumentType doctype = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "publicId", "systemId", doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(DocumentType.NAME));
 assertEquals("publicId", doctype.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", doctype.attr(DocumentType.SYSTEM_ID));
 assertNotNull(doctype.baseUri());
 }

 @Test
 public void testHasAttribute() {
 DocumentType doctype = new DocumentType("html", "HTML", "html", doc.baseUri());

 assertTrue(doctype.has(DocumentType.NAME));
 assertFalse(doctype.has("someInvalidAttribute"));
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 DocumentType doctype = new DocumentType("html", "HTML SYSTEM", null, "systemId", doc.baseUri());

 Appendable out = new StringBuilder();
 doctype.outerHtmlHead(out, 0, doc.outputSettings());

 StringBuilder expected = new StringBuilder("<!DOCTYPE html SYSTEM \"systemId\">");
 assertEquals(expected.toString(), out.toString());
 }

 @Test
 public void testOuterHtmlHeadWithQuirksMode() throws IOException {
 DocumentType doctype = new DocumentType("html", "HTML", "html", doc.baseUri());
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.syntax(Syntax.html);
 outputSettings.quirksMode(QuirksMode.SYMANTEC);

 Appendable out = new StringBuilder();
 doctype.outerHtmlHead(out, 0, outputSettings);

 StringBuilder expected = new StringBuilder("<!doctype html");
 assertEquals(expected.toString(), out.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 DocumentType doctype = new DocumentType("html", "HTML", "html", doc.baseUri());

 Appendable out = new StringBuilder();
 doctype.outerHtmlTail(out, 0, doc.outputSettings());

 assertTrue(StringUtil.isBlank(out.toString()));
 }

 @Test
 public void testCreateNewElementWithDocumentType() {
 Element element = new Element("html");
 element.documentType(new DocumentType("html", "HTML", "html", doc.baseUri()));

 assertEquals("#doctype", element.childNodes().get(0).nodeName());
 }

 @Test
 public void testCreateNewElementWithDocumentTypeAndAttributeSet() {
 AttributeSet attrs = new AttributeSet();
 attrs.put(new NamedAttribute("class", "someClass"));

 Element element = new Element("html");
 element.attributes(attrs);
 element.documentType(new DocumentType("html", "HTML", "html", doc.baseUri()));

 assertEquals("#doctype", element.childNodes().get(0).nodeName());
 assertEquals("someClass", element.classAttr());
 }
}