package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testParseXmlWithSimpleStructure() {
 String xml = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = doc.rootElement();
 List<Node> children = root.children();
 assertEquals("root", root.tagName());
 assertEquals(2, children.size());
 assertEquals("child1", children.get(0).tagName());
 assertEquals("Hello", children.get(0).text());
 Element child2 = (Element) children.get(1);
 assertEquals("child2", child2.tagName());
 List<Node> grandChildren = child2.children();
 assertEquals("grandChild", grandChildren.get(0).tagName());
 assertEquals("World", grandChildren.get(0).text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<img src='image.jpg' />";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 Element img = doc.getElementsByTag("img").first();
 assertEquals("img", img.tagName());
 assertTrue(img.hasAttr("src"));
 assertFalse(img.hasChildren());
 }

 @Test
 public void testParseXmlWithComment() {
 String xml = "<!-- Comment -->";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 List<Node> comments = doc.getAllElements().filter(Node::isInstanceOf).map(node -> (Element) node).collect(Collectors.toList());
 assertEquals(1, comments.size());
 assertEquals("Comment", comments.get(0).tagName());
 }

 @Test
 public void testParseXmlWithCDataSection() {
 String xml = "<![CDATA[Hello World]]>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 List<Node> cDataNodes = doc.getAllNodes().filter(Node::isInstanceOf).map(node -> (CDataNode) node).collect(Collectors.toList());
 assertEquals(1, cDataNodes.size());
 assertEquals("Hello World", cDataNodes.get(0).text());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 DocumentType doctypeNode = doc.doctype();
 assertEquals("html", doctypeNode.tagName());
 assertNotNull(doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testParseXmlWithError() {
 String xml = "<invalid>"; // missing tag name
 Document doc = xmlTreeBuilder.parse(new StringReader(xml));
 assertNull(doc.rootElement());
 }
}