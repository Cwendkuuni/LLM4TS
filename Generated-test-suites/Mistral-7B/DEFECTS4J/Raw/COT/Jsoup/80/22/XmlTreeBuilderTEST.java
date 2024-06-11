package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * Tests for {@link XmlTreeBuilder} class
 *
 * @author Johnson
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, parseSettings);
 }

 @Test
 public void testParse() throws IOException {
 String xmlInput = "<root><element attr=\"value\"><subElement/><subElement/></element></root>";
 Document xmlDoc = builder.parse(new StringReader(xmlInput), "baseUrl");
 assertNotNull(xmlDoc);
 assertNotNull(xmlDoc.rootElement());
 Element rootElement = xmlDoc.rootElement();
 List<Element> elements = rootElement.children();
 assertNotNull(elements);
 assertEquals(1, elements.size());
 assertEquals("root", rootElement.tagName());
 Element element = elements.get(0);
 List<Element> subElements = element.children();
 assertNotNull(subElements);
 assertEquals(2, subElements.size());
 for (Element subElement : subElements) {
 assertNotNull(subElement);
 assertNull(subElement.tagName()); // subelements should be empty
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 try {
 builder.parse(new StringReader("<xml/>"), null);
 } catch (IllegalArgumentException e) {
 assertEquals("baseUri can't be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 try {
 builder.parse(new StringReader(""), "baseUri");
 } catch (IllegalArgumentException e) {
 assertEquals("Input cannot be empty.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlInput = "<root><element attr=\"value\"><subElement/><subElement/></element></root>";
 List<Node> nodes = builder.parseFragment(xmlInput, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNotNull(nodes);
 assertEquals(14, nodes.size()); // 1 root, 1 element, 2 subElements, 2 startTags, 2 endTags, 3 textNodes, 1 commentNode, 1 docTypeNode
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());
 Element element = (Element) nodes.get(1);
 assertEquals("element", element.tagName());
 List<Element> subElements = (List<Element>) element.children();
 assertEquals(2, subElements.size());
 for (Element subElement : subElements) {
 assertNull(subElement.tagName()); // subelements should be empty
 }
 TextNode textNode1 = (TextNode) nodes.get(3);
 assertEquals("", textNode1.text());
 TextNode textNode2 = (TextNode) nodes.get(5);
 assertEquals("", textNode2.text());
 TextNode textNode3 = (TextNode) nodes.get(9);
 assertEquals("<root>", textNode3.text());
 Comment commentNode = (Comment) nodes.get(11);
 assertNotNull(commentNode);
 assertNull(commentNode.getData());
 Doctype doctypeNode = (Doctype) nodes.get(12);
 assertNotNull(doctypeNode);
 assertEquals("xml", doctypeNode.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 try {
 builder.parseFragment("", "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException e) {
 assertEquals("Input cannot be empty.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(TokenType.StartTag, "<element>", null, null, null, null, null, null, null, null);
 Element element = builder.insert(startTagToken);
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(TokenType.EndTag, "</element>", null, null, null, null, null, null, null, null);
 builder.popStackToClose(endTagToken);
 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(TokenType.Character, "abc", null, null, null, null, null, null, null, null);
 builder.insert(characterToken);
 TextNode textNode = (TextNode) builder.stack.get(builder.stack.size() - 1);
 assertEquals("abc", textNode.text());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(TokenType.Comment, "<!--", null, null, null, null, null, null, null, null);
 Comment comment = (Comment) builder.insert(commentToken);
 assertNotNull(comment);
 assertEquals("", comment.getData());
 TextNode textNode = (TextNode) builder.stack.get(builder.stack.size() - 1);
 assertEquals("<!--", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(TokenType.Doctype, "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>", null, null, null, null, null, null, null, null);
 DocumentType doctypeNode = (DocumentType) builder.insert(doctypeToken);
 assertNotNull(doctypeNode);
 assertEquals("root", doctypeNode.tagName());
 TextNode textNode = (TextNode) builder.stack.get(builder.stack.size() - 1);
 assertEquals("<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>", textNode.text());
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token startTagToken = new Token(TokenType.StartTag, "<selfClosingTag/>", null, null, null, null, null, null, null, null);
 Element element = builder.insert(startTagToken);
 assertNotNull(element);
 assertEquals("selfClosingTag", element.tagName());
 assertEquals(0, builder.stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidToken() {
 Token invalidToken = new Token(Integer.MAX_VALUE, "", null, null, null, null, null, null, null, null);
 try {
 builder.insert(invalidToken);
 } catch (IllegalArgumentException e) {
 assertEquals("Unexpected token type: " + invalidToken.type, e.getMessage());
 throw e;
 }
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 builder.stack.add(new Element("root"));
 builder.stack.add(new Element("unknownTag"));
 Token endTagToken = new Token(TokenType.EndTag, "</unknownTag>", null, null, null, null, null, null, null, null);
 builder.popStackToClose(endTagToken);
 assertEquals(1, builder.stack.size());
 }

 @Test
 public void testPopStackToCloseKnownTag() {
 builder.stack.add(new Element("root"));
 builder.stack.add(new Element("element"));
 builder.stack.add(new Element("subElement"));
 Token endTagToken = new Token(TokenType.EndTag, "</element>", null, null, null, null, null, null, null, null);
 builder.popStackToClose(endTagToken);
 assertEquals(2, builder.stack.size());
 }
}