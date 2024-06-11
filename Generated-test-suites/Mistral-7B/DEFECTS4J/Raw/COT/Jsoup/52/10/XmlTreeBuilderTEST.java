package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 doc = new Document();
 }

 @Test
 public void testInitialiseParse() {
 builder.initialiseParse("", "", null);
 assertNotNull(builder.stack);
 assertNotNull(doc);
 }

 @Test
 public void testProcessStartTag() {
 Element element = new Element("root");
 Token startTag = new Token.StartTag("root");
 builder.insert(startTag);
 assertSame(element, builder.currentElement());
 }

 @Test
 public void testProcessEndTag() {
 Element root = new Element("root");
 Element child = new Element("child");
 root.appendChild(child);
 builder.stack.add(root);
 Token endTag = new Token.EndTag("root");
 builder.popStackToClose(endTag);
 assertNull(builder.currentElement());
 }

 @Test
 public void testProcessComment() {
 Comment comment = new Comment("This is a comment");
 Token commentToken = new Token.Comment("This is a comment");
 builder.insert(commentToken);
 assertSame(comment, builder.currentNode());
 }

 @Test
 public void testProcessCharacter() {
 TextNode textNode = new TextNode("Hello World!");
 Token characterToken = new Token.Character('H');
 builder.insert(characterToken);
 assertEquals(textNode.text(), "Hello World!");
 assertSame(textNode, builder.currentNode());
 }

 @Test
 public void testProcessDoctype() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 Token doctype = new Token.Doctype("html");
 builder.insert(doctype);
 assertSame(documentType, builder.currentNode());
 }

 @Test
 public void testProcessUnknownTag() {
 Element unknownTag = new Element("unknownTag");
 Token startTag = new Token.StartTag("unknownTag");
 builder.insert(startTag);
 assertSame(unknownTag, builder.currentElement());
 }

 @Test
 public void testProcessSelfClosingTag() {
 Element selfClosingTag = new Element("selfClosingTag");
 Token startTag = new Token.StartTag("selfClosingTag", true);
 builder.insert(startTag);
 assertSame(selfClosingTag, builder.currentElement());
 assertFalse(builder.stack.contains(selfClosingTag));
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 builder.process(new Token(-1, "Unexpected token type"));
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><child>Hello World!</child></root>";
 List<Node> nodes = builder.parseFragment(inputFragment, "", null);
 assertEquals(3, nodes.size());
 assertEquals(nodes.get(0).nodeName(), "root");
 assertEquals(nodes.get(1).nodeName(), "child");
 assertEquals(nodes.get(2).nodeName(), "#text");
 }

 @Test(expected = RuntimeException.class)
 public void testParseFragmentInvalidInput() {
 builder.parseFragment("", "", null);
 }
}