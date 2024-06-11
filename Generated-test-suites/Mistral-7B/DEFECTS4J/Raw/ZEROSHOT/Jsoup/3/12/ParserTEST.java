package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element root = doc.rootElement();

 assertEquals("html", root.tagName());
 assertEquals("http://example.com", root.baseUri());

 Element body = root.child(0);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>This is a body fragment.</p>", "http://example.com");
 Document doc = parser.parseBodyFragment();
 Element root = doc;

 assertEquals("html", root.tagName());
 assertEquals("http://example.com", root.baseUri());

 Element body = root.child(0);
 assertEquals("body", body.tagName());
 body.children().forEach(child -> {
 Equals("p", child.tagName());
 assertEquals("This is a body fragment.", child.text());
 });
 }

 @Test
 public void testParseInvalidInput() {
 Parser parser = new Parser("", "http://example.com");
 try {
 parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 Parser parser2 = new Parser("<html", "http://example.com");
 try {
 parser2.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseTags() {
 Parser parser = new Parser("<html><body><h1>Hello World</h1><p id=\"test\">This is a test.</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element root = doc.rootElement();

 assertEquals("html", root.tagName());
 assertEquals("http://example.com", root.baseUri());

 Element body = root.child(0);
 assertEquals("body", body.tagName());

 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Hello World", h1.text());

 Element p = body.child(1);
 assertEquals("p", p.tagName());
 assertEquals("This is a test.", p.text());
 assertEquals("test", p.attr("id"));
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "http://example.com");
 Document doc = parser.parseBodyFragment();
 Element root = doc;

 assertEquals("html", root.tagName());
 assertEquals("http://example.com", root.baseUri());

 Element xmlDecl = root.child(0);
 assertEquals("xml-declaration", xmlDecl.tagName());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element root = doc;

 assertEquals("html", root.tagName());
 assertEquals("http://example.com", root.baseUri());

 Element body = root.child(0);
 assertEquals("body", body.tagName());
 assertEquals(0, body.children().size());

 Element comment = body.previousSibling();
 assertEquals("comment", comment.tagName());
 assertEquals("This is a comment", comment.text());
 }
}