package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Testing class for Parser class
 */
public class ParserTest {

 private String html = "<html><head><title>Test</title></head><body>" +
         "<p><base href=\"test.html\"/><a href=\"test2.html\">Link</a></p>" +
         "<textarea>Content in textarea</textarea>" +
         "<div><script>alert(\"JavaScript\");</script></div>" +
         "<div><!-- This is a comment --></div>" +
         "<![CDATA[<html><body>CDATA]]>" +
         "<img src=\"image.png\"/>" +
         "</body></html>";

 private String baseUri = "test.com";

 @Test
 public void testParse() {
 Document doc = Parser.parse(html, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<p>Body fragment</p>", baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragment("<p><script>alert(\"JavaScript\");</script>", baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testParseStartTag() {
 Element doc = new Element(Parser.htmlTag, baseUri);
 Parser parser = new Parser(html, baseUri, false);
 Element tag = parser.parseStartTag();
 doc.appendChild(tag);
 assertEquals(htmlTag, tag.tag());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseEndTag();
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseComment();
 Comment comment = (Comment) parser.last();
 assertEquals("This is a comment ", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseXmlDecl();
 XmlDeclaration xmlDecl = (XmlDeclaration) parser.last();
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.data());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseCdata();
 DataNode cdata = (DataNode) parser.last();
 assertEquals("<html><body>CDATA</body></html>", cdata.text());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser(html, baseUri, false);
 Attribute attribute = parser.parseAttribute();
 assertEquals(null, attribute);

 parser.parseStartTag();
 attribute = parser.parseAttribute();
 assertEquals("href", attribute.key());
 attribute = parser.parseAttribute();
 assertEquals("test2.html", attribute.value());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser(html, baseUri, false);
 TextNode textNode = (TextNode) parser.parseTextNode();
 assertEquals("hello < there", textNode.text());
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("invalidTag", null);
 }

 @Test
 public void testParseInvalidTagAsSelfClosing() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("invalidTag", null);
 parser.parseEndTag("invalidTag");
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseEmptyTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag("img");
 parser.parseEndTag("img");
 assertEquals("img", parser.last().tag().tagName());
 assertTrue(parser.last().tag().isSelfClosing());
 }

 @Test
 public void testParseEmptyTagWithQuotes() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag("img");
 parser.parseEndTag("img");
 parser.parseEndTag();
 assertEquals("img", parser.last().tag().tagName());
 assertFalse(parser.last().tag().isSelfClosing());
 }

 @Test
 public void testParseEmptyTagAsSelfClosing() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag("img");
 parser.parseEndTag();
 assertEquals("img", parser.last().tag().tagName());
 assertTrue(parser.last().tag().isSelfClosing());
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidEndTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("div");
 parser.parseEndTag("p");
 }

 @Test
 public void testParseInvalidEndTagAsSelfClosing() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("div");
 parser.parseEndTag("div");
 parser.parseEndTag();
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseValidEndTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("body");
 parser.parseEndTag("body");
 assertEquals("body", parser.last().tag().tagName());
 }

 @Test
 public void testParseNestedEndTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("html");
 parser.parseStartTag("head");
 parser.parseEndTag("head");
 parser.parseEndTag("html");
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseNestedStartTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("html");
 parser.parseStartTag("head");
 parser.parseEndTag("head");
 parser.parseStartTag("body");
 parser.parseEndTag("body");
 parser.parseEndTag("html");
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseNestedStartTagWithChildren() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("html");
 parser.parseStartTag("head");
 parser.parseStartTag("title");
 parser.parseTextNode();
 parser.parseEndTag("title");
 parser.parseEndTag("head");
 parser.parseStartTag("body");
 parser.parseStartTag("p");
 parser.parseTextNode();
 parser.parseEndTag("p");
 parser.parseEndTag("body");
 parser.parseEndTag("html");
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseNestedTagsWithAttributes() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("html");
 parser.parseStartTag("head");
 parser.parseStartTag("link");
 parser.parseAttribute("rel", "stylesheet");
 parser.parseAttribute("href", "style.css");
 parser.parseEndTag("link");
 parser.parseEndTag("head");
 parser.parseStartTag("body");
 parser.parseStartTag("p");
 parser.parseTextNode();
 parser.parseEndTag("p");
 parser.parseEndTag("body");
 parser.parseEndTag("html");
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseNestedTagsWithNestedAttributes() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("html");
 parser.parseStartTag("head");
 parser.parseStartTag("link");
 parser.parseAttribute("rel", "stylesheet");
 parser.parseAttribute("href", "style.css");
 parser.parseStartTag("meta");
 parser.parseAttribute("charset", "UTF-8");
 parser.parseEndTag("meta");
 parser.parseEndTag("link");
 parser.parseEndTag("head");
 parser.parseStartTag("body");
 parser.parseStartTag("p");
 parser.parseTextNode();
 parser.parseEndTag("p");
 parser.parseEndTag("body");
 parser.parseEndTag("html");
 assertEquals(Parser.htmlTag, parser.last().tag());
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser(html, baseUri, false);
 parser.parseStartTag();
 parser.parseStartTag("base");
 parser.parseAttribute("href", "newBase.html");
 parser.parseEndTag("base");
 assertEquals("base", parser.last().tag().tagName());
 assertEquals("newBase.html", parser.last().tag().baseUri());
 parser.parseEndTag("html");
 assertEquals("newBase.html", parser.last().tag().baseUri());
 }

}