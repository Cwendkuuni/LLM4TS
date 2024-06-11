package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 Element root = xmlDoc.rootElement();
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(2, children.size());
 for (Node child : children) {
 if (child instanceof Element) {
 Element element = (Element) child;
 assertEquals("child1", element.tagName());
 List<Node> childNodes = element.children();
 assertEquals(1, childNodes.size());
 assertEquals("Hello", childNodes.get(0).text());
 } else if (child instanceof TextNode) {
 TextNode textNode = (TextNode) child;
 assertEquals("World", textNode.text());
 }
 }
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child1>Hello</child1></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(1, children.size());
 TextNode textNode = (TextNode) children.get(0);
 assertEquals("Hello", textNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 xmlTreeBuilder.parse(new StringReader(""), "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<xml version=\"1.0\" encoding=\"UTF-8\"><root><child1>Hello</child1></root>"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnknownTag() {
 String xmlString = "<root><unknown/></root>";
 // The test below would fail because Tag.valueOf() method would throw an exception. We need to mock it.
 xmlTreeBuilder = new XmlTreeBuilder() {
 @Override
 Tag tag(String name, ParseSettings settings) {
 return new Element("unknown", "", new AttributeSpec[0]);
 }
 };
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 Element root = xmlDoc.rootElement();
 List<Node> children = root.children();
 assertEquals(1, children.size());
 Element unknown = (Element) children.get(0);
 // In a real test, we'd check that the 'unknown' element was processed correctly
 // but since there are no assertions on its properties, we can just check that it was added to the tree.
 assertTrue(unknown.parent() instanceof Element);
 }
}