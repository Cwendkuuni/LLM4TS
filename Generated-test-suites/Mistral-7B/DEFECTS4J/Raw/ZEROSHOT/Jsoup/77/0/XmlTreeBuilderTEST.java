package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Reader input;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 input = new StringReader("<root><element attr=\"value\"><subElement/><anotherElement/></element></root>");
 }

 @Test
 public void testParse() {
 Document document = xmlTreeBuilder.parse(input, "baseUri");
 assertNotNull(document);
 assertEquals("root", document.tagName());

 Element root = document.child(0);
 assertEquals("element", root.tagName());
 List<Node> children = root.childNodes();
 assertEquals(3, children.size());

 Element element = (Element) children.get(0);
 assertNotNull(element);
 assertEquals("subElement", element.tagName());
 Element anotherElement = (Element) children.get(2);
 assertNotNull(anotherElement);
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<subElement/><anotherElement/>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(inputFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();

 Element subElement = (Element) parsedNodes.get(0);
 assertEquals("subElement", subElement.tagName());
 Element anotherElement = (Element) parsedNodes.get(1);
 assertEquals("anotherElement", anotherElement.tagName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token token = new Token(Token.Type.TEXT, "UnexpectedTokenType", 1, 1);
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testInsertNode() {
 Element root = new Element("root");
 Node node = new TextNode("test");
 xmlTreeBuilder.insertNode(node);
 root.appendChild(node);
 assertEquals("test", node.nodeValue());
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("test comment");
 Comment comment = (Comment) xmlTreeBuilder.parse(new StringReader(""), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).child(0);
 xmlTreeBuilder.insert(commentToken);
 assertEquals("test comment", comment.getData());
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('a');
 TextNode textNode = (TextNode) xmlTreeBuilder.parse(new StringReader(""), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).child(0);
 xmlTreeBuilder.insert(characterToken);
 assertEquals("a", textNode.nodeValue());
 }

 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token.Doctype("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.parse(new StringReader(""), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).child(0);
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals("html", documentTypeNode.getName());
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.parse(input, "baseUri");
 Element root = xmlTreeBuilder.doc.child(0);
 Element element = xmlTreeBuilder.insert(new Token.StartTag("element"));
 xmlTreeBuilder.stack.add(element);
 xmlTreeBuilder.parse(input, "baseUri");
 Element endElement = xmlTreeBuilder.insert(new Token.EndTag("element"));
 xmlTreeBuilder.popStackToClose(endElement);
 assertNotNull(xmlTreeBuilder.doc.child(0));
 }
}