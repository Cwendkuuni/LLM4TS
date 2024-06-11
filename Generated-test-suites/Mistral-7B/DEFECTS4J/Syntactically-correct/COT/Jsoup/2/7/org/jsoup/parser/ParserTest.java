package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * JUnit 4 test file for Parser class
 */
public class ParserTest {

 private static final String HTML_TEMPLATE = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";

 @Test
 public void testParse() {
 Parser parser = new Parser(HTML_TEMPLATE, "http://example.com");
 Document doc = parser.parse();
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "http://example.com");
 try {
 parser.parse();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 try {
 parser.parse();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", "http://example.com");
 Document doc = parser.parse();
 assertNotNull(doc);
 assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("", "http://example.com");
 Document doc = parser.parse();
 assertNotNull(doc);
 assertTrue(doc.isEmpty());
 }

 @Test
 public void testParseInvalidHtml() {
 Parser parser = new Parser("</html>", "http://example.com");
 try {
 parser.parse();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!-- Test Comment -->", "http://example.com");
 Document doc = parser.parse();
 Element comment = doc.select("comment").first();
 assertNotNull(comment);
 assertEquals("Test Comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", "http://example.com");
 Document doc = parser.parse();
 XmlDeclaration decl = doc.selectXmlDeclaration().first();
 assertNotNull(decl);
 assertEquals("UTF-8", decl.encoding());
 assertEquals("1.0", decl.version());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p>Test</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.select("p").first();
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><body><p title=\"Test\">Test</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.select("p[title]").first();
 assertEquals("Test", p.attr("title"));
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<p id=\"test\" class=\"test\" title=\"Test\">Test</p>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.select("p[id=test][class=test]").first();
 assertEquals("test", p.attr("id"));
 assertEquals("test", p.attr("class"));
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<p>Test</p>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.select("p").first();
 assertEquals("Test", p.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<![CDATA[Test]]>", "http://example.com");
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.select("textNode").first();
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testParseBodyFragmentWithoutBaseUri() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", null);
 try {
 parser.parse();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Parser parser = new Parser("<body></body>", null);
 Document doc = parser.parse();
 assertTrue(doc.isEmpty());
 }
}