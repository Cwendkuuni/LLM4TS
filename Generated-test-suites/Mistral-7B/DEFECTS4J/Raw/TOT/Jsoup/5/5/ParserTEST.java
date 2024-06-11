package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser("", "", false);
 doc = parser.parse("");
 }

 @Test
 public void testParseEmptyHtml() {
 assertEquals(htmlTag, doc.tagName());
 assertEquals(0, doc.tagCount());
 }

 @Test
 public void testParseHtmlWithRootTag() {
 parser = new Parser("<html><body></body></html>", "", false);
 doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.tagCount());
 assertEquals(bodyTag, doc.body().tagName());
 }

 @Test
 public void testParseHtmlWithBodyFragment() {
 parser = new Parser("<body></body>", "", true);
 doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.tagCount());
 assertEquals(bodyTag, doc.body().tagName());
 }

 @Test
 public void testParseHtmlWithBodyFragmentAndRelaxedParsing() {
 parser = new Parser("<body></body>", "", true);
 parser.relaxed = true;
 doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.tagCount());
 assertEquals(bodyTag, doc.body().tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 parser = new Parser(null, "", false);
 try {
 parser.parse();
 fail("Should throw IllegalArgumentException for null HTML");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyBaseUri() {
 parser = new Parser("", "", false);
 try {
 parser.parse("");
 fail("Should throw IllegalArgumentException for empty base URI");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testParseHtmlWithTextNode() {
 parser = new Parser("<p>Hello World</p>", "", false);
 doc = parser.parse();
 Element p = doc.body().getElementsByTag("p").get(0);
 TextNode textNode = (TextNode) p.getElementsByTag("textnode").get(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseHtmlWithComment() {
 parser = new Parser("<!-- This is a comment -->", "", false);
 doc = parser.parse();
 Element comment = doc.getElementsByTag("comment").get(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseHtmlWithXmlDeclaration() {
 parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\">", "", false);
 doc = parser.parse();
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.getElementsByTag("xml-declaration").get(0);
 assertEquals("UTF-8", xmlDeclaration.encoding());
 }

 @Test
 public void testParseHtmlWithAttributes() {
 parser = new Parser("<p class=\"test\" id=\"one\">Hello World</p>", "", false);
 doc = parser.parse();
 Element p = doc.body().getElementsByTag("p").get(0);
 Attribute classAttribute = p.classAttribute();
 Attribute idAttribute = p.idAttribute();
 assertEquals("test", classAttribute.value());
 assertEquals("one", idAttribute.value());
 }

 @Test
 public void testParseHtmlWithDataNode() {
 parser = new Parser("<script type=\"text/javascript\">console.log('Hello World');</script>", "", false);
 doc = parser.parse();
 Element script = doc.getElementsByTag("script").get(0);
 TextNode dataNode = (TextNode) script.getElementsByTag("datanode").get(0);
 assertEquals("console.log('Hello World');", dataNode.text());
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 parser = new Parser("<base href=\"/test/\">", "", false);
 doc = parser.parse();
 assertEquals("/test/", doc.baseUri());
 }

 @Test
 public void testParseHtmlWithEmptyElement() {
 parser = new Parser("<img src=\"image.jpg\" />", "", false);
 doc = parser.parse();
 Element img = doc.getElementsByTag("img").get(0);
 assertTrue(img.isEmpty());
 }

 @Test
 public void testParseHtmlWithSelfClosingEmptyElement() {
 parser = new Parser("<input type=\"text\" />", "", false);
 doc = parser.parse();
 Element input = doc.getElementsByTag("input").get(0);
 assertTrue(input.tagName().equals("input") && input.tag().isSelfClosing());
 }

 @Test
 public void testParseHtmlWithImplicitParent() {
 parser = new Parser("<div><img src=\"image.jpg\"></div>", "", false);
 doc = parser.parse();
 Element div = doc.getElementsByTag("div").get(0);
 Element img = div.getElementsByTag("img").get(0);
 assertEquals(div, img.parent());
 }

 @Test
 public void testParseHtmlWithInvalidParentRelaxed() {
 parser = new Parser("<p><div></div></p>", "", true); // Allow <p> to contain <div> when relaxed
 doc = parser.parse();
 Element p = doc.getElementsByTag("p").get(0);
 Element div = p.getElementsByTag("div").get(0);
 assertEquals(p, div.parent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseHtmlWithInvalidParent() {
 parser = new Parser("<p><div></div></p>", "", false); // Do not allow <p> to contain <div> by default
 doc = parser.parse();
 Element p = doc.getElementsByTag("p").get(0);
 Element div = p.getElementsByTag("div").get(0);
 // This should throw an exception as <p> cannot contain <div> by default
 }
}