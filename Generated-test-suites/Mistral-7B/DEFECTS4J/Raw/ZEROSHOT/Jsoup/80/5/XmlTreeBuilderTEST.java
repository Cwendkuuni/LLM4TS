package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><child/><child><grandchild/></child></root>"), "http://example.com");
 Element rootElement = xmlDoc.select("root").first();
 assertEquals("root", rootElement.tagName());
 List<Node> childElements = rootElement.childNodes();
 assertEquals(2, childElements.size());
 for (int i = 0; i < childElements.size(); i++) {
 Element childElement = (Element) childElements.get(i);
 assertEquals("child", childElement.tagName());
 List<Node> grandchildElements = childElement.childNodes();
 assertEquals(1, grandchildElements.size());
 Element grandchildElement = (Element) grandchildElements.get(0);
 assertEquals("grandchild", grandchildElement.tagName());
 assertNull(grandchildElement.attributes().get("foo"));
 }
 }

 @Test
 public void testParseWithEmptyDocument() {
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(""), "http://example.com");
 assertNull(xmlDoc);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token startTagToken = new Token.StartTag("root", null, null);
 Element rootElement = builder.insert(startTagToken);
 assertEquals("root", rootElement.tagName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token startTagToken = new Token.StartTag("root", null, null);
 Token endTagToken = new Token.EndTag("root", null, null);
 builder.parseFragment("<root></root>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.popStackToClose(endTagToken);
 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token commentToken = new Token.Comment("<!-- This is a comment -->");
 builder.insert(commentToken);
 Comment comment = (Comment) builder.currentElement().lastChild();
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token characterToken = new Token.Character('A');
 builder.insert(characterToken);
 TextNode characterNode = (TextNode) builder.currentElement().lastChild();
 assertEquals("A", characterNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token doctypeToken = new Token.Doctype("root", null, null, null, null);
 DocumentType doctypeNode = (DocumentType) builder.insert(doctypeToken);
 assertEquals("root", doctypeNode.tagName());
 assertSame(doctypeNode, builder.currentElement().lastChild());
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element rootElement = new Element("root", "", new HashMap<>());
 Element childElement1 = new Element("child1", "", new HashMap<>());
 Element childElement2 = new Element("child2", "", new HashMap<>());
 builder.stack.add(rootElement);
 builder.stack.add(childElement1);
 builder.stack.add(childElement2);
 builder.popStackToClose(new Token.EndTag("child1", null, null));
 assertEquals(2, builder.stack.size());
 assertEquals(childElement2, builder.stack.get(1));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 List<Node> fragmentNodes = builder.parseFragment("<root><child><grandchild/></child></root>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element rootElement = (Element) fragmentNodes.get(0);
 assertEquals("root", rootElement.tagName());
 List<Node> childElements = rootElement.childNodes();
 assertEquals(1, childElements.size());
 Element childElement = (Element) childElements.get(0);
 assertEquals("child", childElement.tagName());
 List<Node> grandchildElements = childElement.childNodes();
 assertEquals(1, grandchildElements.size());
 Element grandchildElement = (Element) grandchildElements.get(0);
 assertEquals("grandchild", grandchildElement.tagName());
 assertNull(grandchildElement.attributes().get("foo"));
 }

 @Test
 public void testProcessUnknownSelfClosingTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token startTagToken = new Token.StartTag("z", null, null);
 startTagToken.setIsSelfClosing(true);
 Element zElement = builder.insert(startTagToken);
 assertSame(zElement, builder.currentElement().lastChild());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateTokenType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token invalidToken = new Token(100, "invalid", null);
 builder.process(invalidToken);
 }
}