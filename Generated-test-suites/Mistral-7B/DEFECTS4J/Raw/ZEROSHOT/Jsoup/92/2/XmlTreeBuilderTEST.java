package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<root><child><grandchild>Hello World</grandchild></child></root>"), "http://example.com");
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 document = null;
 }

 @Test
 public void testParse() {
 assertNotNull(document);
 assertEquals("root", document.tagName());

 List<Node> childNodes = document.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals("child", childNodes.get(0).tagName());

 List<Node> childNodesOfChild = ((Element) childNodes.get(0)).childNodes();
 assertEquals(1, childNodesOfChild.size());
 assertEquals("grandchild", childNodesOfChild.get(0).tagName());
 assertEquals("Hello World", childNodesOfChild.get(0).text());
 }

 @Test
 public void testParseWithBaseUri() {
 String baseUri = "http://example.com/xml";
 document = xmlTreeBuilder.parse(new StringReader("<root><child href='test.xml'><grandchild>Hello World</grandchild></child></root>"), baseUri);
 assertEquals(baseUri, document.baseUri());

 List<Node> childNodes = document.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals("child", childNodes.get(0).tagName());

 List<Node> childNodesOfChild = ((Element) childNodes.get(0)).childNodes();
 assertEquals(1, childNodesOfChild.size());
 assertEquals("grandchild", childNodesOfChild.get(0).tagName());
 assertEquals("Hello World", childNodesOfChild.get(0).text());

 assertEquals("http://example.com/xml/test.xml", childNodes.get(0).attr("href"));
 }

 @Test
 public void testParseFragment() {
 List<Node> parsedFragment = xmlTreeBuilder.parseFragment("<root><child><grandchild>Hello World</grandchild></child></root>", "http://example.com");
 assertEquals("root", parsedFragment.get(0).tagName());

 Element root = (Element) parsedFragment.get(0);
 List<Node> childNodes = root.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals("child", childNodes.get(0).tagName());

 Element child = (Element) childNodes.get(0);
 List<Node> grandchildNodes = child.childNodes();
 assertEquals(1, grandchildNodes.size());
 assertEquals("grandchild", grandchildNodes.get(0).tagName());
 assertEquals("Hello World", grandchildNodes.get(0).text());
 }

 @Test
 public void testParseFragmentWithContext() {
 Element context = new Element("context");
 List<Node> parsedFragment = xmlTreeBuilder.parseFragment("<root><child><grandchild>Hello World</grandchild></child></root>", context, "http://example.com");
 assertEquals(context, parsedFragment.get(0).parent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.Type.StartTag, "<root>", null, null, null, false);
 Element root = xmlTreeBuilder.insert(startTagToken.asStartTag());
 assertEquals("root", root.tagName());
 assertEquals("http://example.com", root.baseUri());
 }

 @Test
 public void testProcessEndTag() {
 String tagName = "root";
 Element root = new Element(tagName, "http://example.com");
 stack.push(root);
 Token endTagToken = new Token(Token.Type.EndTag, "</" + tagName + ">", null, null, null, false);
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 assertNull(stack.peek());
 }

 @Test
 public void testProcessComment() {
 String commentData = "This is a comment";
 Token commentToken = new Token(Token.Type.Comment, null, null, commentData, null, false);
 Comment comment = xmlTreeBuilder.insert(commentToken);
 assertEquals("", comment.tagName());
 assertEquals(commentData, comment.text());
 }

 @Test
 public void testProcessCharacter() {
 char character = 'A';
 String characterData = Character.toString(character);
 Token characterToken = new Token(Token.Type.Character, characterData, null, null, null, false);
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertEquals(characterData, textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String doctypeName = "xml";
 String publicId = "public -1";
 String systemId = "system -1";
 Token doctypeToken = new Token(Token.Type.Doctype, null, doctypeName, publicId, systemId, false);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals(doctypeName, documentType.nodeName());
 assertEquals(publicId, documentType.publicIdentifier());
 assertEquals(systemId, documentType.systemIdentifier());
 }
}