package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseBasicHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element root = doc.body();
 Element h1 = root.getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Fragment</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element root = doc; // doc has empty head
 Element h1 = root.getElementsByTag("h1").first();
 assertEquals("Fragment", h1.text());
 }

 @Test
 public void testParseWithBaseTag() {
 String html = "<html><head><base href=\"/index.html\"><title>Test</title></head><body><a href=\"about.html\">About</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 String baseUri = doc.baseUri();
 assertEquals("http://example.com/index.html", baseUri);
 Element aboutLink = doc.getElementsByTag("a").first();
 assertEquals("about.html", aboutLink.absUrl("href"));
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html<title>Test</title>"; // missing > on tag
 Document doc = Parser.parse(html, "http://example.com");
 assertNull(doc);
 }

 @Test
 public void testParseTextNodes() {
 String html = "<p>This is a <textarea>test</textarea> with some text.</p>";
 Document doc = Parser.parse(html, "http://example.com");
 Element root = doc.body();
 TextNode textNode = (TextNode) root.getElementsByTag("textarea").first().child(0);
 assertEquals("test", textNode.text());
 }

 @Test
 public void testParseCommentsAndCdata() {
 String html = "<html><!-- this is a comment --><![CDATA[ <script type=\"text/javascript\">//<![CDATA[ var x = 1; // JavaScript comment //]]]> ]]><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element root = doc;
 Element h1 = root.getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 // check comment and CDATA were processed correctly
 Element comment = root.getElementsByTag("comment").first();
 assertEquals(" this is a comment ", comment.text());
 Element cdata = root.getElementsByTag("cdata").first();
 assertEquals("<![CDATA[ <script type=\"text/javascript\">//<![CDATA[ var x = 1; // JavaScript comment //]]]>", cdata.text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 Document doc = Parser.parse(html, "http://example.com");
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.children().first();
 assertEquals("UTF-8", xmlDecl.encoding());
 assertEquals("1.0", xmlDecl.version());
 }
}