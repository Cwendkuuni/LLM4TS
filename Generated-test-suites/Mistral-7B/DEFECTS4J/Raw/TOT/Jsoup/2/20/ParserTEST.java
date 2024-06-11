package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head><title>Test Page</title></head><body><p>Hello World!</p></body></html>");
 }

 @Test
 public void testParseHtml() {
 Element root = doc.rootElement();
 assertEquals("html", root.tagName());
 assertNotNull(root.head());
 assertEquals("head", root.head().tagName());
 assertNotNull(root.body());
 assertEquals("body", root.body().tagName());
 assertNotNull(root.head().title());
 assertEquals("title", root.head().title().tagName());
 assertEquals("Test Page", root.head().title().text());
 assertNotNull(root.body().text());
 assertEquals("Hello World!", root.body().text());
 }

 @Test
 public void testParseEmptyHtml() {
 parser = new Parser("<", "", false);
 doc = parser.parse();
 assertNotNull(doc);
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testParseInvalidHtml() {
 parser = new Parser("<html><>"); // missing end tag
 assertNull(parser.parse());
 }

 @Test
 public void testParseBodyFragment() throws IOException {
 Document fragmentDoc = Parser.parseBodyFragment("<p>Hello World!</p>", "http://example.com");
 Element fragmentRoot = fragmentDoc.rootElement();
 assertEquals("html", fragmentRoot.tagName());
 assertNotNull(fragmentRoot.body());
 assertEquals("body", fragmentRoot.body().tagName());
 assertNotNull(fragmentRoot.body().text());
 assertEquals("Hello World!", fragmentRoot.body().text());
 }

 @Test
 public void testParseComments() {
 parser = new Parser("<!-- This is a comment --><html><head></head><body><p>Hello World!</p></body></html>");
 doc = parser.parse();
 Element root = doc.rootElement();
 assertEquals("html", root.tagName());
 List<Comment> comments = root.child(1).children();
 assertEquals(1, comments.size());
 assertEquals("This is a comment", comments.get(0).text());
 }

 @Test
 public void testParseXmlDecl() {
 parser = new Parser("<?xml version='1.0'?><html><head></head><body><p>Hello World!</p></body></html>");
 doc = parser.parse();
 assertEquals("xml", doc.head().child(0).tagName());
 assertEquals("xml", doc.head().child(0).nodeName());
 assertEquals("1.0", doc.head().child(0).attr("version"));
 }

 @Test
 public void testParseEndTag() {
 parser = new Parser("<p><p>Hello World!</p></p>");
 doc = parser.parse();
 Element root = doc.rootElement();
 Element p1 = root.body().child(0);
 assertEquals("p", p1.tagName());
 assertEquals("p", p1.nodeName());
 Element p2 = p1.child(0);
 assertEquals("p", p2.tagName());
 assertEquals("p", p2.nodeName());
 assertNull(p2.text());
 }

 @Test
 public void testParseStartTag() {
 parser = new Parser("<p>Hello World!</p>");
 doc = parser.parse();
 Element root = doc.rootElement();
 Element p = root.body().child(0);
 assertEquals("p", p.tagName());
 assertEquals("p", p.nodeName());
 assertEquals("Hello World!", p.text());
 }

 @Test
 public void testParseAttributes() {
 parser = new Parser("<p id='test' class='test' title='Test Title'>Hello World!</p>");
 doc = parser.parse();
 Element root = doc.rootElement();
 Element p = root.body().child(0);
 Map<String, String> attributes = p.attributes();
 assertEquals(3, attributes.size());
 assertEquals("test", attributes.get("id"));
 assertEquals("test", attributes.get("class"));
 assertEquals("Test Title", attributes.get("title"));
 }

 @Test
 public void testParseTextNode() {
 parser = new Parser("<p>Hello World!</p>");
 doc = parser.parse();
 Element root = doc.rootElement();
 Element p = root.body().child(0);
 assertEquals("Hello World!", p.child(0).text());
 }

 @Test
 public void testParseCdata() {
 parser = new Parser("<![CDATA[Hello World!]]>");
 doc = parser.parse();
 Element root = doc.rootElement();
 TextNode cdataNode = (TextNode) root.child(0);
 assertEquals("Hello World!", cdataNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTagName() {
 parser = new Parser("<p<p>Hello World!</p>");
 parser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndTag() {
 parser = new Parser("<p>Hello World!");
 parser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeValue() {
 parser = new Parser("<p id='test' class='test' title='<script>alert('test')'>Hello World!</p>");
 parser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeKey() {
 parser = new Parser("<p id='test' class='test' onfocus='test'>Hello World!</p>");
 parser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedCharacters() {
 parser = new Parser("<html><p>Hello World!<?xml version='1.0'?></p>");
 parser.parse();
 fail("Expected IllegalArgumentException");
 }
}