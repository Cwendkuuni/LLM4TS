package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.head();
 assertEquals("html", html.tagName());
 assertEquals("http://example.com", html.baseUri());

 Element head = html.child(0);
 assertEquals("head", head.tagName());

 Element title = head.child(0);
 assertEquals("title", title.tagName());
 assertEquals("Test", title.text());

 Element body = html.child(1);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><p>Hello World</p></body>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc;
 Element body = html.child(0);
 Element p = body.child(0);

 assertEquals("body", body.tagName());
 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser parser = new Parser("<html></html>", null, false);
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.head();
 Element comment = html.child(0);
 assertEquals("comment", comment.tagName());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns=\"http://www.w3.org/1999/xhtml\">", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.head();
 XmlDeclaration decl = html.child(0);
 assertEquals("xmlDeclaration", decl.tagName());
 assertTrue(decl.isStandalone());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.head();
 Element body = html.child(0);
 Element p = body.child(0);
 Element emptyP = body.child(1);

 assertEquals("body", body.tagName());
 assertEquals(2, body.childSize());
 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 assertEquals("p", emptyP.tagName());
 assertEquals(0, emptyP.childSize());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><body><p class=\"test\">Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.head();
 Element body = html.child(0);
 Element p = body.child(0);

 assertEquals("p", p.tagName());
 assertEquals(1, p.childSize());
 Attribute classAttr = p.classAttr();
 assertEquals("test", classAttr.value());
 assertEquals("Hello World", p.child(0).text());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<p class=\"test\" id=\"test-id\"></p>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.body().child(0);
 Attribute classAttr = p.classAttr();
 Attribute idAttr = p.idAttr();
 assertEquals("test", classAttr.value());
 assertEquals("test-id", idAttr.value());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.head();
 Element body = html.child(0);
 Element p = body.child(0);
 TextNode textNode = (TextNode) p.child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><body><![CDATA[\n<p>\nHello World\n<p>\n]]]></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.head();
 Element body = html.child(0);
 TextNode cdataNode = (TextNode) body.child(0);
 assertEquals("\n<p>\nHello World\n<p>\n", cdataNode.text());
 }

 @Test
 public void testParseUnknownTag() {
 Parser parser = new Parser("<html><body><unknown></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.head();
 Element body = html.child(0);
 Element unknown = body.child(0);
 assertEquals("unknown", unknown.tagName());
 assertEquals(0, unknown.childSize());
 }

 @Test
 public void testParseInvalidTagOrder() {
 Parser parser = new Parser("<html></body></html>", "http://example.com");
 try {
 parser.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}