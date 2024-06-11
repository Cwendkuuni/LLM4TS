package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class ParserTest {

 @Test
 public void testParseHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("html", root.tagName());
 assertEquals("Test Page", root.getElementsByTag("title").get(0).text());
 assertEquals("Hello World", root.getElementsByTag("h1").text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello Body</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
 Element root = doc; // body fragment does not have a head

 assertEquals("body", root.tagName());
 assertEquals("Hello Body", root.getElementsByTag("h1").text());
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><base href=\"/test\">" +
 " <head><title>Test Page</title></head>" +
 " <body><a href=\"link.html\">Link</a></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc;

 assertEquals("/test/link.html", doc.getBaseUri());
 assertEquals("link.html", root.getElementsByTag("a").get(0).absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser.parse(null, "http://test.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseComment() {
 String html = "<!-- This is a comment --><html><head><title>Test Page</title></head><body></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 Element comment = root.child(0);
 assertEquals("comment", comment.tagName());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 String html = "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 " <html><head><title>Test Page</title></head><body></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 XmlDeclaration xmlDecl = root.child(0);
 assertEquals("xml", xmlDecl.tagName());
 assertEquals("1.0", xmlDecl.version());
 assertEquals("UTF-8", xmlDecl.encoding());
 }

 @Test
 public void testParseEndTag() {
 String html = "<div><p>Test<p></div>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals(2, root.childNodeSize());
 assertTrue(root.child(0).tagName().equals("div"));
 assertTrue(root.child(1).tagName().equals("p"));
 assertEquals(0, root.child(1).childNodeSize());
 }

 @Test
 public void testParseStartTag() {
 String html = "<h1 id=\"test-id\" class=\"test-class\">Test</h1>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("h1", root.getElementsByTag("h1").get(0).tagName());
 assertEquals("Test", root.getElementsByTag("h1").get(0).text());
 assertEquals("test-id", root.getElementsByTag("h1").get(0).id());
 assertEquals("test-class", root.getElementsByTag("h1").get(0).classes().toString());
 }

 @Test
 public void testParseEmptyElement() {
 String html = "<img src=\"image.jpg\"/>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("img", root.getElementsByTag("img").get(0).tagName());
 assertEquals(0, root.getElementsByTag("img").get(0).childNodeSize());
 }

 @Test
 public void testParseScriptTag() {
 String html = "<script src=\"script.js\"></script>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("script", root.getElementsByTag("script").get(0).tagName());
 assertEquals("script.js", root.getElementsByTag("script").get(0).absUrl("src"));
 }

 @Test
 public void testParseTextNode() {
 String html = "<p>This is a test</p>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("p", root.tagName());
 assertEquals("This is a test", root.getElementsByTag("p").get(0).text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><![CDATA[ <script src=\"script.js\"></script> ]]></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 Element script = root.getElementsByTag("script").get(0);
 assertEquals("script", script.tagName());
 assertEquals("script.js", script.absUrl("src"));
 }

 @Test
 public void testParseDataTag() {
 String html = "<textarea>This is a test</textarea>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("textarea", root.tagName());
 assertEquals("This is a test", root.getElementsByTag("textarea").get(0).text());
 }

 @Test
 public void testParseImplicitParent() {
 String html = "<p><img src=\"image.jpg\"/></p>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element root = doc.getRootElement();

 assertEquals("p", root.tagName());
 assertEquals(1, root.childNodeSize());
 Element p = root.child(0);
 assertEquals(2, p.childNodeSize());
 assertEquals("img", p.child(0).tagName());
 }
}