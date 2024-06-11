package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

 @Test
 public void testParser() {
 Parser parser = new Parser("", "", false); // empty constructor should not throw an exception
 Parser parser1 = new Parser("<html><head></head><body></body></html>", "", false);
 Parser parser2 = new Parser("<html><head><title>Test</title></head><body></body></html>", "", false);
 Parser parser3 = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "", false);

 assertEquals("html", parser1.doc().tagName());
 assertNotNull(parser1.doc().head());
 assertNotNull(parser1.doc().body());
 assertNull(parser1.doc().title());

 assertEquals("html", parser2.doc().tagName());
 assertNotNull(parser2.doc().head());
 assertNotNull(parser2.doc().body());
 assertNotNull(parser2.doc().title());
 assertEqual("Test", parser2.doc().title().text());

 assertEquals("html", parser3.doc().tagName());
 assertNotNull(parser3.doc().head());
 assertNotNull(parser3.doc().body());
 assertNotNull(parser3.doc().title());
 assertEqual("Test", parser3.doc().title().text());
 assertNotNull(parser3.body().child(0));
 assertEqual("Hello", parser3.body().child(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<html><head></head><body><h1>Hello</h1></body></html>", "", true);
 assertEquals("html", parser.doc().tagName());
 assertNotNull(parser.doc().head());
 assertNotNull(parser.doc().body());
 assertNull(parser.doc().title());
 assertNotNull(parser.body().child(0));
 assertEqual("Hello", parser.body().child(0).text());
 }

 @Test
 public void testParsing() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1><p><a href=\"http://www.example.com\">Link</a></p></body></html>", "", false);

 assertEquals("html", parser.doc().tagName());
 assertNotNull(parser.doc().head());
 assertNotNull(parser.doc().body());
 assertNotNull(parser.doc().title());
 assertEqual("Test", parser.doc().title().text());
 assertNotNull(parser.body().child(0));
 assertEqual("h1", parser.body().child(0).tagName());
 assertNotNull(parser.body().child(1));
 assertEqual("p", parser.body().child(1).tagName());
 assertNotNull(parser.body().child(1).child(0));
 assertEqual("a", parser.body().child(1).child(0).tagName());
 assertEqual("http://www.example.com", parser.body().child(1).child(0).absUrl("href"));
 }

 @Test
 public void testParsingWithInvalidHtml() {
 Parser parser = new Parser("<h1></h1>", "", false); // missing opening tag
 assertNull(parser.doc());

 parser = new Parser("</h1>", "", false); // missing closing tag
 assertNull(parser.doc());

 parser = new Parser("<html><head><title>Test</title></head></html>", "", false); // missing body
 assertEquals("html", parser.doc().tagName());
 assertNotNull(parser.doc().head());
 assertNull(parser.doc().body());
 assertNotNull(parser.doc().title());
 }

 @Test
 public void testParsingWithXmlDecl() {
 Parser parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\">", "", false);
 assertEquals("xml", parser.doc().tagName());
 assertNotNull(parser.doc().xmlDecl());
 }

 @Test
 public void testParsingWithComment() {
 Parser parser = new Parser("<html><!-- this is a comment --><head></head><body></body></html>", "", false);
 assertNotNull(parser.doc().head().child(0));
 assertInstanceOf(Comment.class, parser.doc().head().child(0));
 assertEquals("this is a comment", parser.doc().head().child(0).text());
 }

 @Test
 public void testParsingWithCdata() {
 Parser parser = new Parser("<html><![CDATA[this is cdata]]><head></head><body></body></html>", "", false);
 assertNotNull(parser.doc().head().child(0));
 assertInstanceof(TextNode.class, parser.doc().head().child(0));
 assertEquals("this is cdata", parser.doc().head().child(0).text().toString());
 }

 @Test
 public void testParsingWithBaseTag() {
 Parser parser = new Parser("<html><head><base href=\"http://www.example.com\"><title>Test</title></head><body></body></html>", "", false);
 assertEquals("http://www.example.com", parser.doc().baseUri());
 }

 @Test(expected = RuntimeException.class)
 public void testParsingWithUnknownTag() {
 Parser parser = new Parser("<html><unknownTag></unknownTag></html>", "", false);
 }

 @Test
 public void testParsingWithImplicitParent() {
 Parser parser = new Parser("<html><body><img src=\"image.jpg\"></html>", "", false);
 assertNotNull(parser.doc());
 assertNotNull(parser.doc().body().child(0));
 assertInstanceof(Element.class, parser.doc().body().child(0));
 assertEqual("img", parser.doc().body().child(0).tagName());
 assertNotNull(parser.doc().body().child(0).attributes());
 assertNotNull(parser.doc().body().child(0).attributes().get("src"));
 assertEqual("image.jpg", parser.doc().body().child(0).attributes().get("src").value());
 }
}