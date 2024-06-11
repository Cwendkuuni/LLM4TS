package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXML() {
 String xmlString = "<root><child>Hello</child><child><grandChild/></child></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));

 EqualsVerifier.forClass(Document.class).usingGetClass().verify(xmlDocument);
 Element root = xmlDocument.getRootElement();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 List<Element> children = root.children();
 assertNotNull(children);
 assertEquals(2, children.size());

 Element child1 = children.get(0);
 assertNotNull(child1);
 assertEquals("child", child1.tagName());
 List<Node> child1Content = child1.childNodes();
 assertNotNull(child1Content);
 assertEquals(0, child1Content.size());
 String child1Text = child1.text();
 assertNotNull(child1Text);
 assertEquals("Hello", child1Text);

 Element child2 = children.get(1);
 assertNotNull(child2);
 assertEquals("child", child2.tagName());
 List<Node> child2Content = child2.childNodes();
 assertNotNull(child2Content);
 assertEquals(1, child2Content.size());
 Element grandChild = (Element) child2Content.get(0);
 assertNotNull(grandChild);
 assertEquals("grandChild", grandChild.tagName());
 assertEquals(0, grandChild.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXML() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String invalidXmlString = "<root>"; // missing closing tag
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(invalidXmlString));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithMalformedComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithMalformedComment = "<root><!-- This is a comment but the --> malformed comment</root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithMalformedComment));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithEmptyElement() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithEmptyElement = "<root><empty/></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithEmptyElement));
 Element emptyElement = (Element) xmlDocument.getElementsByTag("empty").first();
 assertNotNull(emptyElement);
 assertEquals(0, emptyElement.childNodes().size());
 }

 @Test
 public void testParseXMLWithWhitespace() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithWhitespace = "<root>\n <child>\n Hello\n </child>\n</root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithWhitespace));
 Element root = xmlDocument.getRootElement();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 List<Element> children = root.children();
 assertNotNull(children);
 assertEquals(1, children.size());

 Element child = children.get(0);
 assertNotNull(child);
 assertEquals("child", child.tagName());
 List<Node> childContent = child.childNodes();
 assertNotNull(childContent);
 assertEquals(1, childContent.size());
 TextNode textNode = (TextNode) childContent.get(0);
 assertNotNull(textNode);
 assertEquals(" Hello", textNode.text());
 }

 @Test
 public void testParseXMLWithCDataSection() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithCDataSection = "<root><![CDATA[ <child>Hello CData<child> ]]> </root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithCDataSection));
 Element root = xmlDocument.getRootElement();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 List<Element> children = root.children();
 assertNotNull(children);
 assertEquals(1, children.size());
 Element child = children.get(0);
 assertNotNull(child);
 assertEquals("child", child.tagName());
 List<Node> childContent = child.childNodes();
 assertNotNull(childContent);
 assertEquals(1, childContent.size());
 CDataNode cDataNode = (CDataNode) childContent.get(0);
 assertNotNull(cDataNode);
 assertEquals("<child>Hello CData<child>", cDataNode.getData());
 }

 @Test
 public void testParseXMLWithXmlDeclaration() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithXmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>\n<root><child>Hello</child></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithXmlDeclaration));
 assertEquals(XmlTreeBuilder.ParseSettings.preserveCase, xmlDocument.outputSettings().getSyntax());
 Element root = xmlDocument.getRootElement();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 List<Element> children = root.children();
 assertNotNull(children);
 assertEquals(1, children.size());
 Element child = children.get(0);
 assertNotNull(child);
 assertEquals("child", child.tagName());
 List<Node> childContent = child.childNodes();
 assertNotNull(childContent);
 assertEquals(0, childContent.size());
 String childText = child.text();
 assertNotNull(childText);
 assertEquals("Hello", childText);
 }

 @Test
 public void testParseXMLWithComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithComment = "<root><!-- This is a comment --></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithComment));
 Element root = xmlDocument.getRootElement();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 List<Node> comments = root.childNodes();
 assertNotNull(comments);
 assertEquals(1, comments.size());
 Comment comment = (Comment) comments.get(0);
 assertNotNull(comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXMLWithDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithDoctype = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr='value'></element></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithDoctype));
 Element root = xmlDocument.getRootElement();
 assertNotNull(root);
 assertEquals("root", root.tagName());
 List<Element> children = root.children();
 assertNotNull(children);
 assertEquals(1, children.size());
 Element element = children.get(0);
 assertNotNull(element);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(attributes().getAttributeName("attr")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlStringWithInvalidDoctype = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><child></child></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlStringWithInvalidDoctype));
 }
}