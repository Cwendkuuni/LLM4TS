package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false); // empty constructor should not throw an exception
 Document doc = parser.parse();
 assertNotNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "", false);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 Parser parser = new Parser("", null, false);
 }

 @Test
 public void testParseHtmlFragment() {
 String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 Element div = body.childNodes().get(0);
 assertEquals(1, div.childNodes().size());
 assertEquals("Hello World", div.childNodes().get(0).text());
 }

 @Test
 public void testParseHtmlFragmentWithRelaxedParsing() {
 String html = "<html><div><p>Hello World</p></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");
 assertNotNull(doc);
 Element div = doc.body();
 assertEquals(1, div.childNodes().size());
 assertEquals("Hello World", div.childNodes().get(0).text());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<p>Hello World</p>", "", false);
 Document doc = parser.parse();
 assertEquals(1, doc.childNodes().size());
 Element p = doc.childNodes().get(0);
 assertEquals(1, p.childNodes().size());
 TextNode textNode = (TextNode) p.childNodes().get(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!-- This is a comment -->", "", false);
 Document doc = parser.parse();
 assertEquals(1, doc.childNodes().size());
 Comment comment = (Comment) doc.childNodes().get(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<![CDATA[This is raw data]]>", "", false);
 Document doc = parser.parse();
 assertEquals(1, doc.childNodes().size());
 DataNode cdataNode = (DataNode) doc.childNodes().get(0);
 assertEquals("This is raw data", cdataNode.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\">", "", false);
 Document doc = parser.parse();
 assertEquals(1, doc.childNodes().size());
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.childNodes().get(0);
 assertEquals("UTF-8", xmlDeclaration.encoding());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<div id=\"myDiv\"><p class=\"myClass\"></p></div>", "", false);
 Document doc = parser.parse();
 Element div = doc.body();
 assertEquals(1, div.childNodes().size());
 Element p = div.childNodes().get(0);
 assertEquals(1, p.childNodes().size());
 assertEquals("div", p.tagName());
 assertEquals("myDiv", p.attributes().get("id").getValue());
 assertEquals("myClass", p.attributes().get("class").getValue());
 }

 @Test
 public void testParseEmptyElement() {
 Parser parser = new Parser("<img src=\"image.jpg\"/>", "", false);
 Document doc = parser.parse();
 Element img = doc.body();
 assertEquals(1, img.childNodes().size());
 assertTrue(img.tag().isSelfClosing());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<div><p>Hello World</p></div>", "", false);
 Document doc = parser.parse();
 assertEquals(2, doc.childNodes().size());
 Element div = doc.body();
 assertEquals(1, div.childNodes().size());
 Element p = div.childNodes().get(0);
 assertEquals(1, p.childNodes().size());
 assertTrue(p.tag().isClosed());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<a href=\"https://example.com\" class=\"myClass\" id=\"myId\">Link</a>", "", false);
 Element a = parser.parse().body();
 Attribute hrefAttribute = a.tag().attributes().get("href");
 Attribute classAttribute = a.tag().attributes().get("class");
 Attribute idAttribute = a.tag().attributes().get("id");
 assertEquals("https://example.com", hrefAttribute.getValue());
 assertEquals("myClass", classAttribute.getValue());
 assertEquals("myId", idAttribute.getValue());
 }
}