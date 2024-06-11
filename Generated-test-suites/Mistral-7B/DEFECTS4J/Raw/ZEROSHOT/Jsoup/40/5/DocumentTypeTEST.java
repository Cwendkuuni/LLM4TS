package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test for DocumentType class.
 */
public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(doc.baseUri(), documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType(null, "publicId", "systemId", doc.baseUri());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\">", accum.toString());
 }

 @Test
 public void testOuterHtmlWithNameOnly() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", null, null, doc.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlWithPublicIdOnly() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", null, doc.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
 }

 @Test
 public void testOuterHtmlWithSystemIdOnly() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", null, "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", accum.toString());
 }

 @Test
 public void testOuterHtmlWithNamePublicIdAndSystemId() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 documentType.outerHtmlTail(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\">", accum.toString());
 }

 @Test
 public void testCloneNode() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 DocumentType clone = (DocumentType) documentType.cloneNode(true);

 assertEquals("#doctype", clone.nodeName());
 assertEquals("html", clone.attr("name"));
 assertEquals("publicId", clone.attr("publicId"));
 assertEquals("systemId", clone.attr("systemId"));
 assertEquals(doc.baseUri(), clone.baseUri());
 }

 @Test
 public void testAddChild() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 Element element = new Element("html");
 documentType.addChild(0, element);
 Node child = documentType.child(0);

 assertEquals(Element.class, child.getClass());
 assertSame(element, child);
 }

 @Test
 public void testAddTextChild() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 TextNode textNode = new TextNode("text");
 documentType.addChild(0, textNode);
 Node child = documentType.child(0);

 assertEquals(Text.class, child.getClass());
 assertSame(textNode, child);
 }

 @Test
 public void testHasAttr() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 documentType.attr("name", "html5");

 assertEquals(true, documentType.hasAttr("name"));
 assertEquals(false, documentType.hasAttr("publicId"));
 }

 @Test
 public void testRemoveAttr() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 documentType.attr("name", "html5");
 documentType.removeAttr("name");

 assertEquals(false, documentType.hasAttr("name"));
 }

 @Test
 public void testAttr() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 Attribute attribute = new Attribute("name", "html5");
 documentType.attr(attribute);

 assertEquals("html5", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 }

 @Test
 public void testAttrWithExistingAttr() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", doc.baseUri());
 documentType.attr("name", "html5");
 documentType.attr("name", "html6");

 assertEquals("html6", documentType.attr("name"));
 }
}