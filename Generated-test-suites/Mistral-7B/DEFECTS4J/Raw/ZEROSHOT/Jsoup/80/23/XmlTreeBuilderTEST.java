package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element attr=\"value\"><subElement attr2=\"value2\"/></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");

 assertEquals("root", doc.tagName());
 Element root = doc.rootElement();
 assertEquals(1, root.childNodes().size());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals(1, element.childNodes().size());
 Element subElement = element.child(0);
 assertEquals("subElement", subElement.tagName());
 assertEquals("value2", subElement.attr("attr2"));
 assertNull(subElement.attr("attr"));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<element attr=\"value\"><subElement attr2=\"value2\"/></element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 assertEquals(1, nodes.size());
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals(1, element.childNodes().size());
 Element subElement = (Element) element.child(0);
 assertEquals("subElement", subElement.tagName());
 assertEquals("value2", subElement.attr("attr2"));
 assertNull(subElement.attr("attr"));
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidXml() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root<element attr=\"value\"></root>";
 try {
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 } catch (IOException e) {
 throw new RuntimeException("Unexpected IOException", e);
 }
 }

 @Test
 public void testXmlDeclaration() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element attr=\"value\"><subElement attr2=\"value2\"/></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.childNodes().get(0);
 assertEquals("xml", xmlDeclaration.tagName());
 assertEquals("1.0", xmlDeclaration.xmlVersion());
 assertEquals("UTF-8", xmlDeclaration.encoding());
 }

 @Test
 public void testEmptyDocument() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(""), "baseUri");
 assertNotNull(doc);
 assertEquals(0, doc.childNodes().size());
 }
}