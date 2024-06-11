package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.Helper.*;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document parsedDocument;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child>Hello World</child></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 assertNotNull(parsedDocument);
 assertEquals("root", parsedDocument.tagName());
 assertEquals(1, parsedDocument.childNodeSize());
 assertEquals("child", parsedDocument.child(0).tagName());
 assertEquals("Hello World", parsedDocument.child(0).text());
 }

 @Test
 public void testParseWithEmptyDocument() {
 String xmlString = "<root/>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 assertNotNull(parsedDocument);
 assertEquals("root", parsedDocument.tagName());
 assertEquals(0, parsedDocument.childNodeSize());
 }

 @Test
 public void testParseWithSelfClosingTag() {
 String xmlString = "<root><child/></root>";
 parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 assertNotNull(parsedDocument);
 assertEquals("root", parsedDocument.tagName());
 assertEquals(1, parsedDocument.childNodeSize());
 assertEquals("child", parsedDocument.child(0).tagName());
 assertEquals(0, parsedDocument.child(0).childNodeSize());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child1><child2>Hello World</child2></child1>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNotNull(parsedNodes);
 assertEquals(1, parsedNodes.size());
 Element rootElement = (Element) parsedNodes.get(0);
 assertEquals("child1", rootElement.tagName());
 assertEquals(1, rootElement.childNodeSize());
 assertEquals("child2", rootElement.child(0).tagName());
 assertEquals("Hello World", rootElement.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidInput() {
 xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
 }
}