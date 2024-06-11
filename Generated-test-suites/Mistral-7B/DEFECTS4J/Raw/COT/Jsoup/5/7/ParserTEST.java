package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * ParserTest.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false);
 Document doc = parser.parse();
 assertEquals("html", doc.tagName());
 assertNull(doc.body());
 assertNull(doc.head());
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><head></head><body><p id=\"test\">Hello World</p></body></html>";
 Parser parser = new Parser(html, "http://test.com", false);
 Document doc = parser.parse();
 assertEquals("http://test.com", doc.baseUri());
 Element p = doc.getElementById("test");
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p id=\"test\">Hello World</p>";
 Parser parser = new Parser(bodyHtml, "", true);
 Document doc = parser.parse();
 assertEquals("html", doc.tagName());
 assertEquals("", doc.head().text());
 Element body = doc.body();
 Element p = body.getElementById("test");
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p id=\"test\">Hello World</p>";
 Parser parser = new Parser(bodyHtml, "", true);
 parser.relaxed = true;
 Document doc = parser.parse();
 assertEquals("html", doc.tagName());
 assertEquals("", doc.head().text());
 Element body = doc.body();
 Element p = body.getElementById("test");
 assertEquals("Hello World", p.text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "", false);
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("", null, false);
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><head></head><body><p id=\"test\">Hello World</p></body></html>", "", false);
 parseStartTagTestHelper(parser, "html", null, true);
 parseStartTagTestHelper(parser, "head", null, true);
 parseStartTagTestHelper(parser, "body", null, true);
 parseStartTagTestHelper(parser, "p", new Attribute("id", "test"), false);
 }

 @Test
 public void testParseStartTagWithAttributes() {
 Parser parser = new Parser("<p id=\"test\" class=\"testclass\" title=\"Test Title\">Hello World</p>", "", false);
 parseStartTagTestHelper(parser, "p", new Attribute("id", "test"), new Attribute("class", "testclass"), new Attribute("title", "Test Title"), false);
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><head></head><body><p id=\"test\">Hello World</p></body></html>", "", false);
 parseEndTagTestHelper(parser, "p");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEndTagWithWrongTagName() {
 Parser parser = new Parser("<html><head></head><body><p id=\"test\">Hello World</p></body></html>", "", false);
 parseEndTagTestHelper(parser, "div");
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><head><!-- This is a comment --></head><body></body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element comment = parser.last().getChild(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head></head><body></body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element xmlDecl = parser.last().getChild(0);
 assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.attributes().get("xmlns:html").value());
 }

 @Test
 public void testParseEndTagWithoutClosingTag() {
 Parser parser = new Parser("<html><head><!-- This is a comment --></head><body><p id=\"test\">Hello World</p><p id=\"test2\">Hello World</p></body></html>", "", false);
 parseEndTagTestHelper(parser, "p");
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element p = parser.last().getChild(0);
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseEmptyElement() {
 Parser parser = new Parser("<html><head></head><body><img src=\"test.png\"/></body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element img = parser.last().getChild(0);
 assertTrue(img.isEmptyElement());
 }

 @Test
 public void testParseTagWithInvalidCloseTag() {
 Parser parser = new Parser("<html><head></head><body><p id=\"test\">Hello World</p></body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element p = parser.last().getChild(0);
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseTagWithInvalidOpenTag() {
 Parser parser = new Parser("<html><head></head><body></body></p><html>", "", false);
 assertEquals(0, parser.stack.size());
 }

 @Test
 public void testParseTagWithImplicitParent() {
 Parser parser = new Parser("<img src=\"test.png\" alt=\"test\" />", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element img = parser.last().getChild(0);
 assertEquals("img", img.tagName());
 assertEquals("test.png", img.absUrl("src"));
 assertEquals(null, img.getChild(0));
 }

 @Test
 public void testParseTagWithInvalidParent() {
 Parser parser = new Parser("<html><head></head><body><h1>Hello World</h1></body></div>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element h1 = parser.last().getChild(0);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseTagWithSelfClosingTag() {
 Parser parser = new Parser("<html><head></head><body><img src=\"test.png\" alt=\"test\" />Hello World</body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element img = parser.last().getChild(0);
 assertEquals("img", img.tagName());
 assertEquals("test.png", img.absUrl("src"));
 assertEquals(null, img.getChild(0));
 assertEquals("Hello World", parser.last().getChild(1).text());
 }

 @Test
 public void testParseTagWithInvalidSelfClosingTag() {
 Parser parser = new Parser("<html><head></head><body><p id=\"test\">Hello World</p></p></body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element p = parser.last().getChild(0);
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseTagWithInvalidTagName() {
 Parser parser = new Parser("<html><head></head><body><z></z></body></html>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 assertNull(parser.last().getChild(0));
 }

 @Test
 public void testParseTagWithImplicitParent2() {
 Parser parser = new Parser("<ul><li>Item 1</li><li>Item 2</li></ul>", "", false);
 assertEquals(1, parser.stack.size());
 assertEquals("html", parser.stack.getLast().tagName());
 Element ul = parser.last().getChild(0);
 assertEquals("ul", ul.tagName());
 Element li1 = ul.getChild(0);
 assertEquals("li", li1.tagName());
 assertEquals("Item 1", li1.text());
 Element li2 = ul.getChild(1);
 assertEquals("li", li2.tagName());
 assertEquals("Item 2", li2.text());
 }

 private void parseStartTagTestHelper(Parser parser, String tagName, Attribute... attributes) {
 assertEquals(parser.stack.size() + 1, parser.stack.size());
 assertEquals(tagName, parser.last().tagName());
 Element lastElement = parser.last();
 if (attributes != null) {
 for (Attribute attribute : attributes) {
 assertEquals(attribute.key(), lastElement.attribute(attribute.key()).key());
 assertEquals(attribute.value(), lastElement.attribute(attribute.key()).value());
 }
 }
 if (tagName.equals("body")) {
 assertEquals(htmlTag, parser.last().parent());
 } else {
 assertEquals(parser.last().parent(), lastElement);
 }
 }

 private void parseEndTagTestHelper(Parser parser, String tagName) {
 assertEquals(parser.stack.size() - 1, parser.stack.size());
 assertEquals(tagName, parser.last().tagName());
 Element lastElement = parser.last();
 assertEquals(parser.last().parent(), lastElement.parent());
 }

}