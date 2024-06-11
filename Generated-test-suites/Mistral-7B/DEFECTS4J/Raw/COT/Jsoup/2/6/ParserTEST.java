package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

 private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --><head></head><body><h1>Test Heading</h1></body></html>";
 private static final String HTML_WITH_CDATA = "<html><![CDATA[This is some CDATA<script>alert('Hello World')</script>]]></html>";
 private static final String HTML_WITH_XML_DECL = "<html xmlns:xml=\"http://www.w3.org/1999/xhtml\">";
 private static final String HTML_WITH_EMPTY_TAG = "<img src=\"image.png\" />";
 private static final String HTML_WITH_SELF_CLOSED_TAG = "<input type=\"text\" />";
 private static final String HTML_WITH_BASE_TAG = "<base href=\"/test\">";
 private static final String HTML_WITH_INVALID_TAG = "<invalid>";
 private static final String HTML_WITH_MIXED_TAGS = "<html><body><div><h1>Header</h1><p>Paragraph</p></div></body></html>";

 @Test
 public void testParse() {
 Document doc = Parser.parse(HTML_WITH_COMMENT, "http://example.com");
 assertEquals("This is a comment", doc.body().child(0).child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<p>This is a body fragment.</p>", "http://example.com");
 assertEquals("This is a body fragment.", doc.body().text());
 }

 @Test
 public void testParseComment() {
 Document doc = Parser.parse("<html><!-- This is a comment --><head></head><body></body></html>", "http://example.com");
 assertTrue(doc.body().child(0) instanceof Comment);
 }

 @Test
 public void testParseXmlDecl() {
 Document doc = Parser.parse(HTML_WITH_XML_DECL, "http://example.com");
 assertTrue(doc.xml() != null);
 }

 @Test
 public void testParseEndTag() {
 Document doc = Parser.parse("<html><body><h1>Heading</h1></body></html>", "http://example.com");
 assertEquals(1, doc.body().child(0).childCount());
 }

 @Test
 public void testParseStartTag() {
 Document doc = Parser.parse("<html><body><h1 id=\"id1\">Heading</h1></body></html>", "http://example.com");
 Element h1 = doc.body().child(0);
 assertEquals(1, h1.childCount());
 assertTrue(h1.id().equals("id1"));
 }

 @Test
 public void testParseAttribute() {
 Document doc = Parser.parse("<img src=\"image.png\" alt=\"Alternative text\" title=\"ToolTip Text\" />", "http://example.com");
 Element img = doc.body().child(0);
 assertEquals("image.png", img.attr("src"));
 assertEquals("Alternative text", img.attr("alt"));
 assertEquals("ToolTip Text", img.attr("title"));
 }

 @Test
 public void testParseTextNode() {
 Document doc = Parser.parse("<p>This is a text node.</p>", "http://example.com");
 Element p = doc.body().child(0);
 assertEquals("This is a text node.", p.child(0).text());
 }

 @Test
 public void testParseCdata() {
 Document doc = Parser.parse(HTML_WITH_CDATA, "http://example.com");
 Element script = doc.body().child(0);
 assertEquals("This is some CDATA<script>alert('Hello World')</script>", script.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTag() {
 Parser.parse(HTML_WITH_INVALID_TAG, "http://example.com");
 }

 @Test
 public void testParseMixedTags() {
 Document doc = Parser.parse(HTML_WITH_MIXED_TAGS, "http://example.com");
 assertEquals(1, doc.body().childCount());
 Element div = doc.body().child(0);
 assertEquals("Header", div.child(0).child(0).text());
 assertEquals("Paragraph", div.child(0).child(1).child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyTagWithContent() {
 Parser.parse("<img src=\"image.png\" />Hello World", "http://example.com");
 }

 @Test
 public void testParseEmptyTag() {
 Document doc = Parser.parse(HTML_WITH_EMPTY_TAG, "http://example.com");
 assertEquals("", doc.body().child(0).text());
 }

 @Test
 public void testParseSelfClosedTag() {
 Document doc = Parser.parse(HTML_WITH_SELF_CLOSED_TAG, "http://example.com");
 assertEquals("", doc.body().child(0).text());
 }

 @Test
 public void testParseBaseTag() {
 Document doc = Parser.parse(HTML_WITH_BASE_TAG, "http://example.com");
 assertEquals("http://example.com/test", doc.baseUri());
 }
}