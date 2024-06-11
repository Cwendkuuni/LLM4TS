package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://www.example.com");
 Document doc = parser.parse();

 assertEquals("Test", doc.title().text());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello</p>", "http://www.example.com");
 Document doc = parser.parseBodyFragment();
 Element p = doc.body().select("p").first();
 assertEquals("Hello", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://www.example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 }

 @Test
 public void testStackAndPop() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://www.example.com");
 Document doc = parser.parse();
 Element title = doc.title();
 Element body = doc.body();
 Element h1 = body.select("h1").first();

 assertEquals("Test", title.text());
 assertEquals("Hello", h1.text());

 assertEquals(htmlTag, parser.last().tag());
 parser.popStack();
 assertEquals(headTag, parser.last().tag());
 parser.popStack();
 assertEquals(bodyTag, parser.last().tag());
 parser.popStack();
 assertEquals(doc, parser.last()); // parser last element is Document
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><head><title>Test</title><body><h1 attr='test'>Hello</h1></body></html>", "http://www.example.com");
 Document doc = parser.parse();
 Element h1 = doc.body().select("h1").first();
 assertEquals("test", h1.attr("attr"));
 assertEquals("Hello", h1.text());
 }

 @Test
 public void testParseEmptyTag() {
 Parser parser = new Parser("<img src='test.jpg' />", "http://www.example.com");
 Document doc = parser.parse();
 Element img = doc.body().select("img").first();
 assertNotNull(img);
 assertEquals("test.jpg", img.absUrl("src"));
 }

 @Test
 public void testParseImplicitTag() {
 Parser parser = new Parser("<textarea name='test'>Test</textarea>", "http://www.example.com");
 Document doc = parser.parse();
 Element textarea = doc.body().select("textarea").first();
 assertEquals("Test", textarea.text());
 assertEquals("Test", textarea.absUrl("").attr("name"));
 }

 @Test
 public void testParseDataNode() {
 Parser parser = new Parser("<script type='text/javascript'>alert('Hello');</script>", "http://www.example.com");
 Document doc = parser.parse();
 Element script = doc.body().select("script").first();
 assertNotNull(script);
 TextNode dataNode = (TextNode) script.childNode(0);
 assertEquals("alert('Hello');", dataNode.text());
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser("<base href='/new-site/' />", "http://www.example.com");
 Document doc = parser.parse();
 assertEquals("/new-site/", doc.baseUri());
 }

 @Test
 public void testParseComments() {
 Parser parser = new Parser("<html><!-- This is a comment --><body><!-- Another comment -->Hello</body></html>", "http://www.example.com");
 Document doc = parser.parse();
 assertEquals(2, doc.body().childNodeSize());
 TextNode comment1 = (TextNode) doc.body().child(0);
 assertEquals("This is a comment ", comment1.text());
 TextNode comment2 = (TextNode) doc.body().child(1);
 assertEquals("Another comment", comment2.text());
 }

 @Test
 public void testParseXMLDecl() {
 Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "http://www.example.com");
 Document doc = parser.parse();
 XmlDeclaration decl = (XmlDeclaration) doc.child(0);
 assertEquals("about:legacy-compat", decl.systemId());
 }
}