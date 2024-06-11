package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithBaseUri() {
 String xmlInput = "<root><child xmlns:test='http://test.com'><test:grandchild/></child></root>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "http://example.com/");
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 }

 @Test
 public void testInitialiseParse() {
 String xmlInput = "<root><child xmlns:test='http://test.com'><test:grandchild/></child></root>";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase();
 xmlTreeBuilder.initialiseParse(xmlInput, "http://example.com/", errors, settings);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessNullInput() {
 xmlTreeBuilder.process(null);
 }

 @Test
 public void testProcessStartTag() {
 String xmlInput = "<root><child/></root>";
 xmlTreeBuilder.process(new Token.StartTag("root", null, null, null, true));
 Element root = xmlTreeBuilder.currentElement();
 assertEquals("root", root.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<root><child><endtag/></child></root>";
 xmlTreeBuilder.process(new Token.EndTag("endtag", null));
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<!-- This is a comment -->";
 xmlTreeBuilder.process(new Token.Comment("<!-- This is a comment -->", null));
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<root>abc</root>";
 xmlTreeBuilder.process(new Token.Character('a'));
 xmlTreeBuilder.process(new Token.Character('b'));
 xmlTreeBuilder.process(new Token.Character('c'));
 assertEquals(3, xmlTreeBuilder.doc.childNodes().size());
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(2);
 assertEquals("abc", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE root SYSTEM \"file:test.dtd\">";
 xmlTreeBuilder.process(new Token.Doctype("root", null, "file:test.dtd", null));
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("root", documentTypeNode.tagName());
 assertEquals("file:test.dtd", documentTypeNode.systemId());
 }

 @Test
 public void testPopStackToClose() {
 String xmlInput = "<root><child><child/><child><endtag/></child></child></root>";
 xmlTreeBuilder.insert(new Token.StartTag("root", null, null, null, true));
 xmlTreeBuilder.insert(new Token.StartTag("child", null, null, null, false));
 xmlTreeBuilder.insert(new Token.StartTag("child", null, null, null, false));
 xmlTreeBuilder.insert(new Token.EndTag("child", null));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child", null));
 assertEquals(2, xmlTreeBuilder.stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPopStackToCloseUnknownTag() {
 String xmlInput = "<root><child><unknown/></child></root>";
 xmlTreeBuilder.insert(new Token.StartTag("root", null, null, null, true));
 xmlTreeBuilder.insert(new Token.StartTag("child", null, null, null, false));
 xmlTreeBuilder.insert(new Token.StartTag("unknown", null, null, null, false));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child", null));
 }

 @Test
 public void testParseFragment() {
 String xmlInput = "<root><child><grandchild/></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, "http://example.com/", new ParseErrorList(), ParseSettings.preserveCase());
 assertEquals(3, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 Element grandchild = (Element) nodes.get(2);
 assertEquals("grandchild", grandchild.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "http://example.com/", new ParseErrorList(), ParseSettings.preserveCase());
 }
}