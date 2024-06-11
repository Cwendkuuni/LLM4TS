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
 public void setUp() {
 parser = new Parser("");
 doc = parser.parse("<html><head/><body></body></html>");
 }

 @Test
 public void testParseEmpty() {
 Document emptyDoc = parser.parse("");
 assertEquals(emptyDoc, doc);
 }

 @Test
 public void testParseHtmlTag() {
 Document htmlDoc = parser.parse("<html></html>");
 assertEquals(htmlTag, htmlDoc.tagName());
 }

 @Test
 public void testParseHeadTag() {
 Document htmlDoc = parser.parse("<html><head></head></html>");
 Element head = htmlDoc.head();
 assertEquals(headTag, head.tagName());
 }

 @Test
 public void testParseBodyTag() {
 Document htmlDoc = parser.parse("<html><body></body></html>");
 Element body = htmlDoc.body();
 assertEquals(bodyTag, body.tagName());
 }

 @Test
 public void testParseTitleTag() {
 Document htmlDoc = parser.parse("<html><head><title>Test</title></head><body></body></html>");
 Element title = htmlDoc.title();
 assertEquals("Test", title.text());
 }

 @Test
 public void testParseTextareaTag() {
 Document htmlDoc = parser.parse("<html><body><textarea>Test</textarea></body></html>");
 Element textarea = htmlDoc.body().child(0);
 assertEquals(textareaTag, textarea.tagName());
 TextNode textNode = (TextNode) textarea.child(0);
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testParseComment() {
 Document htmlDoc = parser.parse("<html><!-- This is a comment --></html>");
 Element comment = htmlDoc.body().child(0);
 assertTrue(comment instanceof Comment);
 }

 @Test
 public void testParseXmlDecl() {
 Document htmlDoc = parser.parse("<html xmlns='http://www.w3.org/1999/xhtml'>");
 Element xmlDecl = htmlDoc.body().child(0);
 assertTrue(xmlDecl instanceof XmlDeclaration);
 }

 @Test
 public void testParseEndTag() {
 Document htmlDoc = parser.parse("<html><body><div></div></body></html>");
 Element div = htmlDoc.body().child(0);
 assertEquals(1, div.childSize());
 assertEquals(divTag, div.tagName());
 }

 @Test
 public void testParseStartTagWithAttributes() {
 Document htmlDoc = parser.parse("<html><body><img src='test.jpg' alt='Test'/></body></html>");
 Element img = htmlDoc.body().child(0);
 assertEquals(2, img.childSize());
 assertEquals("img", img.tagName());
 Attribute srcAttr = img.attribute("src");
 assertEquals("test.jpg", srcAttr.value());
 Attribute altAttr = img.attribute("alt");
 assertEquals("Test", altAttr.value());
 }

 @Test
 public void testParseTextNode() {
 Document htmlDoc = parser.parse("<html><body><p>Test</p></body></html>");
 Element p = htmlDoc.body().child(0);
 assertEquals(1, p.childSize());
 TextNode textNode = (TextNode) p.child(0);
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testParseCdata() {
 Document htmlDoc = parser.parse("<html><body><![CDATA[Test]]></body></html>");
 Element cdata = htmlDoc.body().child(0);
 assertTrue(cdata instanceof DataNode);
 assertEquals("Test", ((TextNode) cdata).text());
 }

 @Test
 public void testParseBodyFragment() {
 Document fragmentDoc = Parser.parseBodyFragment("<p>Test</p>", "http://test.com");
 Element p = fragmentDoc.body().child(0);
 assertEquals(1, p.childSize());
 TextNode textNode = (TextNode) p.child(0);
 assertEquals("Test", textNode.text());
 Element head = fragmentDoc.head();
 assertNull(head);
 }

 @Test
 public void testParseBodyFragmentWithEmptyTag() {
 Document fragmentDoc = Parser.parseBodyFragment("<img src='test.jpg'/>", "http://test.com");
 Element img = fragmentDoc.body().child(0);
 assertEquals(1, img.childSize());
 assertTrue(img.tag().isSelfClosing());
 TextNode altNode = img.child(0);
 assertEquals(0, altNode.childSize());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document fragmentDoc = Parser.parseBodyFragmentRelaxed("<p><img src='test.jpg'/></p>", "http://test.com");
 Element p = fragmentDoc.body().child(0);
 assertEquals(1, p.childSize());
 TextNode textNode = (TextNode) p.child(0);
 assertEquals("", textNode.text());
 Element img = p.child(0);
 assertEquals(1, img.childSize());
 TextNode srcNode = img.child(0);
 assertEquals("test.jpg", srcNode.text());
 }
}