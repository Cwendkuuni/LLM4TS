package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<root><element attr=\"value\">Content</element></root>";
 Document doc = builder.parse(new StringReader(xmlString));

 Element root = doc.rootElement();
 Element element = root.child(0);

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseWithBaseUri() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><element attr=\"value\">Content</element></root>";
 String baseUri = "http://example.com/";
 Document doc = builder.parse(new StringReader(xmlString), baseUri);

 Element root = doc.rootElement();
 Element element = root.child(0);

 assertEquals("root", root.tagName());
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 assertEquals("http://example.com/", element.baseUri().toString());
 }

 @Test
 public void testInsertComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<!-- This is a comment --><root><element attr=\"value\">Content</element></root>";
 Document doc = builder.parse(new StringReader(xmlString));

 Element root = doc.rootElement();
 List<Node> comments = root.childNodes();

 assertEquals(1, comments.size());
 assertTrue(comments.get(0) instanceof Comment);
 assertEquals("This is a comment", comments.get(0).text());
 }

 @Test
 public void testInsertCData() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<root><![CDATA[This is CData]]></root>";
 Document doc = builder.parse(new StringReader(xmlString));

 Element root = doc.rootElement();
 List<Node> cdataNodes = root.childNodes();

 assertEquals(1, cdataNodes.size());
 assertTrue(cdataNodes.get(0) instanceof CDataNode);
 assertEquals("This is CData", cdataNodes.get(0).text());
 }

 @Test
 public void testInsertTextNode() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<root>Content</root>";
 Document doc = builder.parse(new StringReader(xmlString));

 Element root = doc.rootElement();
 List<Node> textNodes = root.childNodes();

 assertEquals(1, textNodes.size());
 assertTrue(textNodes.get(0) instanceof TextNode);
 assertEquals("Content", textNodes.get(0).text());
 }

 @Test
 public void testInsertDocumentType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<!DOCTYPE root [<!ATTLIST element attr CDATA #REQUIRED >]><root></root>";
 Document doc = builder.parse(new StringReader(xmlString));

 DocumentType documentType = doc.documentType();
 assertEquals("root", documentType.nodeName());
 assertEquals("[<!ATTLIST element attr CDATA #REQUIRED >]", documentType.internalSubset());
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlFragment = "<element attr=\"value\">Content</element>";
 Element context = new Element("root");
 Document doc = builder.parse(xmlFragment, context, null);

 Element parsedElement = (Element) doc.childNodes().get(0);
 assertEquals("element", parsedElement.tagName());
 assertEquals("value", parsedElement.attribute("attr"));
 assertEquals("Content", parsedElement.text());
 assertEquals(context, parsedElement.parent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXml() {
 XmlTreeBuilder builder = new XmlTreeBuilder();

 String xmlString = "<root>This is not valid xml";
 try {
 Document doc = builder.parse(new StringReader(xmlString));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}