package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
 }

 @Test
 public void testParseHtml() {
 // Test parsing a valid HTML document
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a body fragment
 String bodyHtml = "<h1>Fragment</h1>";
 doc = parser.parseBodyFragment(bodyHtml, "");
 assertNotNull(doc);
 assertEquals("<h1>Fragment</h1>", doc.body().html());
 assertNull(doc.head());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 // Test parsing a body fragment with relaxed parsing
 String bodyHtmlRelaxed = "<h1>Fragment</h1>"; // missing <body> tag
 doc = parser.parseBodyFragmentRelaxed(bodyHtmlRelaxed, "");
 assertNotNull(doc);
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("<h1>Fragment</h1>", body.html());
 }

 @Test(expected = RuntimeException.class)
 public void testParseInvalidHtml() {
 // Test parsing invalid HTML (missing start tag)
 String invalidHtml = "</html>";
 try {
 parser.parse(invalidHtml, "");
 } catch (RuntimeException e) {
 // Expected error
 }
 }

 @Test
 public void testParseCdata() {
 // Test parsing CDATA
 String cdata = "<![CDATA[<script src=\"javascript:alert('Hello')\" type=\"text/javascript\"]]>";
 doc = parser.parse("<html><head></head><body>" + cdata + "</body></html>");
 Element script = doc.body().getElementsByTag("script").first();
 TextNode textNode = (TextNode) script.child(0);
 assertEquals("javascript:alert('Hello')", textNode.text());
 }

 @Test
 public void testParseXmlDecl() {
 // Test parsing XML declaration
 doc = parser.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head></head><body></body></html>");
 assertEquals("1.0", doc.xmlVersion());
 assertEquals("UTF-8", doc.xmlEncoding());
 }

 @Test
 public void testParseComment() {
 // Test parsing HTML comments
 doc = parser.parse("<html><!-- This is a comment --></html>");
 String commentText = doc.body().getElementsByClass("comment").first().text();
 assertEquals("This is a comment", commentText);
 }

 @Test
 public void testParseAttribute() {
 // Test parsing HTML attributes
 String htmlWithAttr = "<a href=\"https://example.com\" title=\"Test\">Link</a>";
 doc = parser.parse(htmlWithAttr);
 Element link = doc.getElementsByTag("a").first();
 assertEquals("https://example.com", link.absUrl("href"));
 assertEquals("Test", link.attr("title"));
 }

 @Test
 public void testParseTextNode() {
 // Test parsing text nodes
 doc = parser.parse("<p>Hello <there></p>");
 TextNode textNode = (TextNode) doc.getElementsByTag("p").first().child(0);
 assertEquals("Hello ", textNode.text());
 }

 @Test
 public void testParseEmptyElement() {
 // Test parsing empty elements
 String emptyElementHtml = "<img src=\"image.jpg\" alt=\"Image\" />";
 doc = parser.parse(emptyElementHtml);
 Element img = doc.getElementsByTag("img").first();
 assertNotNull(img);
 assertEquals(3, img.childNodeSize()); // 3 children: img tag, alt text node, and src text node
 assertEquals("image.jpg", img.absUrl("src"));
 assertEquals("Image", img.attr("alt"));
 }
}