package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 Document doc = xmlTreeBuilder.defaultSettings().parse("", "");
 assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
 }

 @Test
 public void testParse() {
 String xml = "<root><child><subchild></subchild></child></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");;
 Element root = doc.rootElement();
 List<Node> nodes = doc.childNodes();
 assertEquals(3, nodes.size());
 assertEquals("root", root.tagName());
 assertEquals(2, root.childNodes().size());
 assertEquals("child", nodes.get(0).tagName());
 assertEquals("subchild", nodes.get(1).tagName());
 }

 @Test
 public void testProcessStartTag() {
 String xml = "<root><child></child></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");;
 Element root = doc.rootElement();
 List<Node> nodes = doc.childNodes();
 Element child = (Element) nodes.get(0);
 assertEquals(1, child.childNodes().size());
 }

 @Test
 public void testProcessEndTag() {
 String xml = "<root><child></child></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");;
 Element root = doc.rootElement();
 List<Node> nodes = doc.childNodes();
 Element child = (Element) nodes.get(0);
 assertEquals(2, nodes.size());
 // popStackToClose not tested here as it's a private method
 }

 @Test
 public void testProcessComment() {
 String xml = "<!-- This is a comment -->";
 Document doc = xmlTreeBuilder.parse(xml, "");;
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 assertEquals(Comment.class, nodes.get(0).getClass());
 }

 @Test
 public void testProcessCharacter() {
 String xml = "<root>&#x26;<child></child></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");;
 List<Node> nodes = doc.childNodes();
 Element root = (Element) nodes.get(0);
 TextNode character = (TextNode) nodes.get(1);
 assertEquals("&amp;", character.text());
 }

 @Test
 public void testProcessDoctype() {
 String xml = "<!DOCTYPE html [ <!ATTLIST foo bar CDATA #REQUIRED> ]>";
 Document doc = xmlTreeBuilder.parse(xml, "");;
 List<Node> nodes = doc.childNodes();
 DocumentType doctypeNode = (DocumentType) nodes.get(0);
 assertEquals("html", doctypeNode.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnexpectedTokenType() {
 String xml = "<root><child></child><!-- This is a comment --></root>";
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(xml, "");;
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("Unexpected token type: 3", e.getMessage());
 }
 }

 @Test
 public void testInsertNode() {
 Element root = new Element("root");
 xmlTreeBuilder.insertNode(root);
 assertEquals(1, root.childNodes().size());
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 xmlTreeBuilder.parse("", null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullParseSettings() {
 xmlTreeBuilder.parse("", new ParseSettings());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child><subchild></subchild></child>";
 Document doc = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), xmlTreeBuilder.defaultSettings());
 List<Node> nodes = doc.childNodes();
 assertEquals(3, nodes.size());
 }

}