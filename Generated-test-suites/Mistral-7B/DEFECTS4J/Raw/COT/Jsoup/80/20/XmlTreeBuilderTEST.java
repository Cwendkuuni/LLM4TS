package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child>Hello World</child></root>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = builder.parse(new StringReader(xmlString), "http://example.com");
 Element root = doc.getRootElement();
 assertEquals("root", root.tagName());
 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseThrowsExceptionOnInvalidInput() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String invalidXmlString = "<root><child>Hello World<";
 try {
 Document doc = builder.parse(new StringReader(invalidXmlString), "http://example.com");
 fail("Expected ParseException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root");
 builder.initialiseParse(new StringReader("<root></root>"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 Token startTag = builder.next();
 assertEquals(StartTag.class, startTag.getClass());
 assertEquals("root", startTag.name());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root");
 builder.initialiseParse(new StringReader("<root><child></child></root>"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 Token endTag = builder.next();
 assertEquals(EndTag.class, endTag.getClass());
 assertEquals("root", endTag.tagName());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.initialiseParse(new StringReader("<!-- This is a comment -->"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 Token comment = builder.next();
 assertEquals(Comment.class, comment.getClass());
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.initialiseParse(new StringReader("<text>Hello World</text>"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 Token character = builder.next();
 assertEquals(Character.class, character.getClass());
 assertEquals("Hello World", character.getData());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.initialiseParse(new StringReader("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 Token doctype = builder.next();
 assertEquals(Doctype.class, doctype.getClass());
 assertEquals("html", doctype.name());
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root");
 Element child = new Element("child");
 builder.initialiseParse(new StringReader("<root><child></child></root>"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 builder.popStackToClose(new Token.EndTag("child"));
 assertEquals(root, builder.doc.getRootElement());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopStackToCloseOnUnknownTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root");
 Element child = new Element("unknown");
 builder.initialiseParse(new StringReader("<root><unknown></unknown></root>"), null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 builder.runParser();
 builder.popStackToClose(new Token.EndTag("child"));
 fail("Expected IllegalStateException");
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child>Hello World</child></root>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 List<Node> nodes = builder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element child = (Element) nodes.get(1);
 assertEquals("child", child.tagName());
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseFragmentThrowsExceptionOnInvalidInput() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String invalidXmlFragment = "<root><child>Hello World<";
 try {
 List<Node> nodes = builder.parseFragment(invalidXmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 fail("Expected ParseException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

}