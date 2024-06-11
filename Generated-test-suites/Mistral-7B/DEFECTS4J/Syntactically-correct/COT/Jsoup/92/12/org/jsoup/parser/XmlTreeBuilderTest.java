package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * XmlTreeBuilderTest.java
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml><root><child></child></root></xml>";
 Document document = xmlTreeBuilder.parse(new StringReader(input), null);
 assertEquals(Document.class, document.getClass());
 assertEquals(XmlTreeBuilder.class, document.parser());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child></child></root>";
 Token startTagToken = new Token.StartTag("root", null, null, null, null, null, null, null, null, null, null);
 Element element = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><child></child></root>";
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 String input = "<!--comment-->";
 Token commentToken = new Token.Comment("<!--comment-->");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(Comment.class, xmlTreeBuilder.currentNode().getClass());
 }

 @Test
 public void testProcessCharacter() {
 String input = "abc";
 Token characterToken = new Token.Character("a");
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentNode();
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentNode();
 assertEquals("html", documentTypeNode.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Token(123, null);
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testParseFragment() {
 String input = "<root><child></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, null, null, parser);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 xmlTreeBuilder.parseFragment(null, null, null, parser);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 xmlTreeBuilder.parseFragment(input, null, null, parser);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullParser() {
 xmlTreeBuilder.parseFragment(input, null, null, null);
 }

 @Test
 public void testParseFragmentContext() {
 String input = "<root><child></child></root>";
 Element context = new Element("html");
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, context, null, parser);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 assertEquals("html", context.tagName());
 }

}