package org.jsoup.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XmlTreeBuilderTest {

 @Test
 public void testParseSimpleXml() {
 Document xmlDoc = XmlTreeBuilder.parse("<root><child>Hello</child></root>", null);
 Element root = xmlDoc.getDocumentElement();
 assertNotNull(root);
 assertEquals("root", root.getNodeName());
 Element child = (Element) root.getElementsByTagName("child").item(0);
 assertNotNull(child);
 assertEquals("Hello", child.getTextContent());
 }

 @Test
 public void testParseXmlWithComments() {
 Document xmlDoc = XmlTreeBuilder.parse("<root><!-- This is a comment --><child>Hello</child></root>", null);
 NodeList nodeList = xmlDoc.getElementsByTagName("!--");
 assertEquals(1, nodeList.getLength());
 Node commentNode = nodeList.item(0);
 assertNotNull(commentNode);
 assertEquals("!-- This is a comment --", commentNode.getTextContent());
 }

 @Test
 public void testParseEmptyXml() {
 Document xmlDoc = XmlTreeBuilder.parse("", null);
 assertNotNull(xmlDoc);
 assertNotNull(xmlDoc.getDocumentElement());
 assertEquals(0, xmlDoc.getDocumentElement().getChildNodes().getLength());
 }

 @Test
 public void testParseInvalidXml() {
 Assertions.assertThrows(IllegalArgumentException.class, () -> XmlTreeBuilder.parse("<root><child><", null));
 }

 @Test
 public void testParseFragment() {
 Document xmlDoc = XmlTreeBuilder.parse("<root><child>Hello</child></root>", null);
 Node fragment = xmlDoc.getElementById("child");
 assertNotNull(fragment);
 assertEquals("Hello", fragment.getTextContent());
 List<Node> fragmentNodes = XmlTreeBuilder.parseFragment("<fragment><child>Fragment</child></fragment>", null, ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertNotNull(fragmentNodes);
 assertEquals(1, fragmentNodes.size());
 Node fragmentNode = fragmentNodes.get(0);
 assertEquals("Fragment", fragmentNode.getNodeName());
 }
}