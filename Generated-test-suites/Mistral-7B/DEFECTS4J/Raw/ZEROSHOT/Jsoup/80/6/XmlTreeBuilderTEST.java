package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.PRESERVE_CASE, settings);
 }

 @Test
 public void testParseWithBaseUri() {
 String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><child xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ElementType\"><grandChild/></child></root>";
 String baseUri = "http://test.com";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = document.rootElement();
 assertEquals("root", rootElement.tagName());
 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 Element grandChildElement = childElement.child(0);
 assertNull(grandChildElement.attributes());
 assertFalse(grandChildElement.hasText());
 }

 @Test
 public void testParseWithoutBaseUri() {
 String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><child xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ElementType\"><grandChild/></child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString));
 Element rootElement = document.rootElement();
 assertEquals("root", rootElement.tagName());
 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 Element grandChildElement = childElement.child(0);
 assertNull(grandChildElement.attributes());
 assertFalse(grandChildElement.hasText());
 }

 @Test
 public void testInsertElement() {
 String xmlString = "<root><child></child></root>";
 Document document = Jsoup.parse(xmlString, ParseSettings.preserveCase());
 Element rootElement = document.rootElement();
 Element childElement = document.createElement("grandChild");
 xmlTreeBuilder.insertNode(childElement);
 rootElement.appendChild(childElement);
 assertEquals(1, rootElement.childNodes().size());
 assertEquals("grandChild", childElement.tagName());
 }

 @Test
 public void testInsertComment() {
 String xmlString = "<root><!-- This is a comment --></root>";
 Document document = Jsoup.parse(xmlString, ParseSettings.preserveCase());
 Comment comment = document.newComment("This is a comment");
 xmlTreeBuilder.insert(comment);
 Element rootElement = document.rootElement();
 assertEquals(1, rootElement.childNodes().size());
 assertTrue(rootElement.child(0) instanceof Comment);
 }

 @Test
 public void testInsertCharacter() {
 String xmlString = "<root>&#160;</root>";
 Document document = Jsoup.parse(xmlString, ParseSettings.preserveCase());
 CharacterNode characterNode = document.newTextNode(" ");
 xmlTreeBuilder.insert(characterNode);
 Element rootElement = document.rootElement();
 assertEquals(1, rootElement.childNodes().size());
 assertTrue(rootElement.child(0) instanceof TextNode);
 }

 @Test
 public void testInsertDoctype() {
 String xmlString = "<!DOCTYPE root [<!ELEMENT child (grandChild)>]>";
 Document document = Jsoup.parse(xmlString, ParseSettings.preserveCase());
 DocumentType doctypeNode = document.newDocumentType("root", null, null);
 xmlTreeBuilder.insert(doctypeNode);
 Element rootElement = document.rootElement();
 assertEquals(1, rootElement.childNodes().size());
 assertTrue(rootElement.child(0) instanceof DocumentType);
 }

 @Test
 public void testPopStackToClose() {
 String xmlString = "<root><child><grandChild></grandChild></child></root>";
 Document document = Jsoup.parse(xmlString, ParseSettings.preserveCase());
 Element rootElement = document.rootElement();
 Element childElement = rootElement.child(0);
 Element grandChildElement = childElement.child(0);
 assertEquals(2, rootElement.childNodes().size());
 xmlTreeBuilder.popStackToClose(grandChildElement.tag());
 assertEquals(1, rootElement.childNodes().size());
 assertFalse(grandChildElement.isDetached());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child><grandChild/></child></root>";
 String baseUri = "http://test.com";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase()).stream().map(n -> n.childNodes().get(0)).toList();
 assertEquals(2, nodes.size());
 assertEquals("grandChild", nodes.get(0).tagName());
 assertEquals("child", nodes.get(1).parent().tagName());
 }
}