package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class ParserTest {

 private static final String HTML = "<html><head><title>Test</title><body><h1 id=\"test1\">Test</h1><p><img src=\"image.png\" alt=\"Image\"><base href=\"http://test.com\"><base target=\"_blank\"><textarea></textarea><script>function test() { alert('Hello'); }</script></p></body></html>";

 @Test
 public void testParse() {
 Document doc = Parser.parse(HTML, "http://test.com");
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("http://test.com", doc.baseUri());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<p><img src=\"image.png\" alt=\"Image\"><base href=\"http://test.com\"><base target=\"_blank\"><textarea></textarea><script>function test() { alert('Hello'); }</script></p>", "http://test.com");
 assertNotNull(doc);
 assertNull(doc.head());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragmentRelaxed("<p><img src=\"image.png\" alt=\"Image\"><base href=\"http://test.com\"><base target=\"_blank\"><textarea></textarea><script>function test() { alert('Hello'); }</script></p>", "http://test.com");
 assertNotNull(doc);
 assertNull(doc.head());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser(HTML, "http://test.com", false);
 parser.parseStartTag();
 assertSame(htmlTag, parser.last().tag());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p><h1>Test</h1></p></body></html>", "http://test.com", false);
 parser.parseEndTag();
 assertSame(bodyTag, parser.last().tag());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- Test --></html>", "http://test.com", false);
 parser.parseComment();
 assertNotNull(parser.last());
 assertTrue(parser.last() instanceof Comment);
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns=\"http://www.w3.org/1999/xhtml\">", "http://test.com", false);
 parser.parseXmlDecl();
 assertNotNull(parser.last());
 assertTrue(parser.last() instanceof XmlDeclaration);
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<img src=\"image.png\" alt=\"Image\" title=\"Title\" class=\"class1 class2\">", "http://test.com");
 Attribute attribute = parser.parseAttribute();
 assertNotNull(attribute);
 assertEquals("class", attribute.getKey());
 assertEquals("class1 class2", attribute.getValue());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<p>Test</p>", "http://test.com");
 TextNode textNode = (TextNode) parser.last().child(0);
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<script><![CDATA[function test() { alert('Hello'); }]]></script>", "http://test.com");
 DataNode dataNode = (DataNode) parser.last().child(0);
 assertEquals("<![CDATA[function test() { alert('Hello'); }]]>", dataNode.data());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseUnknownTag() {
 Parser parser = new Parser("<html><unknown></unknown></html>", "http://test.com", false);
 parser.parse();
 }

 @Test
 public void testParseEmptyTag() {
 Parser parser = new Parser("<img src=\"image.png\"/>", "http://test.com");
 Element img = (Element) parser.last();
 assertTrue(img.tag().isSelfClosing());
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Parser parser = new Parser("", "http://test.com", true);
 Document doc = parser.parse();
 assertNotNull(doc);
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testParseBodyFragmentWithBaseTag() {
 Parser parser = new Parser("<base href=\"http://test2.com\"><p></p>", "http://test.com", true);
 Document doc = parser.parse();
 assertEquals("http://test2.com", doc.baseUri());
 }

 @Test
 public void testParseBodyFragmentWithBaseTargetTag() {
 Parser parser = new Parser("<base target=\"_blank\"><p></p>", "http://test.com", true);
 Document doc = parser.parse();
 assertTrue(doc.baseUri().endsWith("_blank"));
 }

 @Test
 public void testParseBodyFragmentWithTextareaTag() {
 Parser parser = new Parser("<textarea></textarea>", "http://test.com", true);
 Element textarea = (Element) parser.last();
 assertTrue(textarea.hasText());
 assertEquals("", textarea.text());
 }

 @Test
 public void testParseBodyFragmentWithScriptTag() {
 Parser parser = new Parser("<script>function test() { alert('Hello'); }</script>", "http://test.com", true);
 Document doc = parser.parse();
 assertNotNull(doc.body().child(0));
 }

 @Test
 public void testParseBodyFragmentWithMixedTags() {
 Parser parser = new Parser("<p><img src=\"image.png\" alt=\"Image\"><base href=\"http://test2.com\"><base target=\"_blank\"><textarea></textarea></p>", "http://test.com", true);
 Document doc = parser.parse();
 assertEquals("http://test2.com", doc.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithImproperlyFormattedHtmlString() {
 Parser parser = new Parser("Test<script>function test() { alert('Hello'); }</script>", "http://test.com", true);
 parser.parse();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithBaseTagInsideBodyTag() {
 Parser parser = new Parser("<body><base href=\"http://test2.com\"></body>", "http://test.com", true);
 parser.parse();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithScriptTagInsideBodyTag() {
 Parser parser = new Parser("<body><script>function test() { alert('Hello'); }</script></body>", "http://test.com", true);
 parser.parse();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithTagWithInvalidParent() {
 Parser parser = new Parser("<body><p><ul></p></ul>", "http://test.com", true);
 parser.parse();
 }
}