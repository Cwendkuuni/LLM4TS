package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false); // empty constructor should throw NullPointerException
 try {
 parser.parse();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }

 String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 Document doc = Parser.parse(html, "");
 assertEquals("Test", doc.title().text());
 assertEquals("Hello", doc.body().child(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");
 assertEquals("", doc.head().text());
 assertEquals("Hello", doc.body().child(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Hello</h1>"; // missing closing tag
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");
 assertEquals("", doc.head().text());
 assertEquals("Hello", doc.body().child(0).text());
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><head><title>Test<textarea></title></head><body><h1>Hello<script>console.log('world')</script></h1></body></html>";
 Document doc = Parser.parse(html, "");
 Element title = doc.title();
 assertEquals(2, title.childrenSize());
 assertTrue(title.child(1) instanceof TextNode);
 assertTrue(title.child(1).text().startsWith("<textarea>"));
 Element h1 = doc.body().child(0);
 assertEquals(3, h1.childrenSize());
 assertTrue(h1.child(1) instanceof DataNode);
 assertTrue(h1.child(1).text().startsWith("console.log('world')"));
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><div><p>Hello<span>World</span></p></div></body></html>";
 Document doc = Parser.parse(html, "");
 Element div = doc.body().child(0);
 assertEquals(1, div.childrenSize());
 Element p = div.child(0);
 assertEquals(2, p.childrenSize());
 assertTrue(p.child(1) instanceof TextNode);
 assertTrue(p.child(1).text().equals("World"));
 }

 @Test
 public void testParseComment() {
 String html = "<html><!--This is a comment--><body></body></html>";
 Document doc = Parser.parse(html, "");
 Element comment = doc.body().child(0);
 assertTrue(comment instanceof Comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:test='http://example.com/test' xml:lang='en'><body></body></html>";
 Document doc = Parser.parse(html, "");
 Element xmlDeclaration = doc.head().child(0);
 assertTrue(xmlDeclaration instanceof XmlDeclaration);
 assertEquals("http://example.com/test", xmlDeclaration.attr("xmlns:test"));
 assertEquals("en", xmlDeclaration.attr("xml:lang"));
 }

 @Test
 public void testParseAttribute() {
 String html = "<a href='#' id='test' title='Test Title'>Link</a>";
 Document doc = Parser.parse(html, "");
 Element a = doc.body().child(0);
 List<Attribute> attributes = a.attributes();
 assertEquals(3, attributes.size());
 Attribute href = attributes.get(0);
 assertEquals("href", href.getKey());
 assertEquals("#", href.getValue());
 Attribute id = attributes.get(1);
 assertEquals("id", id.getKey());
 assertEquals("test", id.getValue());
 Attribute title = attributes.get(2);
 assertEquals("title", title.getKey());
 assertEquals("Test Title", title.getValue());
 }

 @Test
 public void testParseTextNode() {
 String html = "<p>Hello <there</p>";
 Document doc = Parser.parse(html, "");
 Element p = doc.body().child(0);
 TextNode textNode = (TextNode) p.child(0);
 assertEquals("Hello <there", textNode.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<script><![CDATA[console.log('world')]]></script>";
 Document doc = Parser.parse(html, "");
 Element script = doc.body().child(0);
 DataNode cdataNode = (DataNode) script.child(0);
 assertEquals("console.log('world')", cdataNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 String html = "<html<head>"; // missing '>' in tag
 Document doc = Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 String html = "<html><body><div><p>Hello<pWorld</div></body></html>"; // missing '>' in tag
 Document doc = Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTagName() {
 String html = "<html><body><div><p>Hello<foo></p></div></body></html>";
 Document doc = Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedEndTag() {
 String html = "<html><body><div><p>Hello<div></div></div></body></html>";
 Document doc = Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttribute() {
 String html = "<a href='#' <title='Test Title'>Link</a>"; // missing '>' in attribute
 Document doc = Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeValue() {
 String html = "<a href='#' title='<script>console.log('world')'>Link</a>"; // contains script tag
 Document doc = Parser.parse(html, "");
 fail("Expected IllegalArgumentException");
 }
}