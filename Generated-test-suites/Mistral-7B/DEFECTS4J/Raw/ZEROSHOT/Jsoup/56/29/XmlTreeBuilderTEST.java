package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlTreeBuilderParsesEmptyXmlDocument() {
 String xmlInput = "<xml/>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "baseUri");

 Element rootElement = xmlDoc.rootElement();
 assertEquals("xml", rootElement.tagName());
 Assert.assertEquals(0, rootElement.childNodes().size());
 }

 @Test
 public void testXmlTreeBuilderParsesSimpleXmlDocument() {
 String xmlInput = "<xml><tag attr='value'><subtag/></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "baseUri");

 Element rootElement = xmlDoc.rootElement();
 Element tagElement = rootElement.child(0);
 assertEquals("tag", tagElement.tagName());
 assertEquals("value", tagElement.attr("attr"));
 Element subtagElement = tagElement.child(0);
 assertEquals("subtag", subtagElement.tagName());
 Assert.assertEquals(0, subtagElement.childNodes().size());
 }

 @Test
 public void testXmlTreeBuilderParsesXmlDocumentWithComments() {
 String xmlInput = "<xml><tag><!-- comment --></tag></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "baseUri");

 Element rootElement = xmlDoc.rootElement();
 Element tagElement = rootElement.child(0);
 Comment comment = tagElement.child(0);
 assertEquals("comment", comment.data());
 }

 @Test
 public void testXmlTreeBuilderParsesXmlDocumentWithDoctype() {
 String xmlInput = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">\n<xml></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "baseUri");

 DocumentType doctype = xmlDoc.doctype();
 assertEquals("xml", doctype.name());
 assertEquals("-//W3C//DTD XML 1.0//EN", doctype.publicIdentifier());
 assertEquals("xml.dtd", doctype.systemIdentifier());
 Element rootElement = xmlDoc.rootElement();
 Assert.assertEquals(0, rootElement.childNodes().size());
 }

 @Test
 public void testXmlTreeBuilderParsesXmlDocumentWithSelfClosingTags() {
 String xmlInput = "<xml><tag/></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "baseUri");

 Element rootElement = xmlDoc.rootElement();
 Element tagElement = rootElement.child(0);
 assertTrue(tagElement.isSelfClosing());
 Assert.assertEquals(0, tagElement.childNodes().size());
 }

 @Test
 public void testXmlTreeBuilderParsesXmlFragment() {
 String xmlFragment = "<tag attr='value'><subtag/></tag>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element tagElement = (Element) parsedNodes.get(0);
 assertEquals("tag", tagElement.tagName());
 Element subtagElement = (Element) tagElement.child(0);
 assertEquals("subtag", subtagElement.tagName());
 Assert.assertEquals(0, subtagElement.childNodes().size());
 }

 @Test(expected = RuntimeException.class)
 public void testXmlTreeBuilderThrowsExceptionForUnknownTag() {
 String xmlInput = "<unknownTag/>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "baseUri");
 // This test relies on the Validate.fail() method in XmlTreeBuilder
 // which throws a RuntimeException when the unexpected token type is encountered
 }
}