package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXMLWithRootElement() {
 String xmlString = "<root><child>Hello World</child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
 Element root = document.getRootElement();
 Element child = root.getChildren().get(0);
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseXMLWithEmptyElement() {
 String xmlString = "<element/>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
 Element element = document.getElementsByTag("element").get(0);
 assertTrue(element.isEmpty());
 }

 @Test
 public void testParseXMLWithAttributes() {
 String xmlString = "<element attribute1=\"value1\" attribute2='value2'/>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
 Element element = document.getElementsByTag("element").get(0);
 assertEquals("value1", element.attr("attribute1"));
 assertEquals("value2", element.attr("attribute2"));
 }

 @Test
 public void testParseXMLWithSelfClosingTag() {
 String xmlString = "<tag/>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
 Element tag = document.getElementsByTag("tag").get(0);
 assertTrue(tag.isSelfClosing());
 }

 @Test(expected = org.jsoup.parser.Parser.MalformedXmlException.class)
 public void testParseXMLWithInvalidXML() {
 String xmlString = "<tag<child>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
 fail("Expected Parser.MalformedXmlException");
 }

 @Test
 public void testParseXMLWithParsingFragment() {
 String xmlFragment = "<fragment><child>Hello World</child></fragment>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://localhost", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Element fragment = (Element) nodes.get(0);
 Element child = fragment.getChildren().get(0);
 assertEquals("Hello World", child.text());
 }
}