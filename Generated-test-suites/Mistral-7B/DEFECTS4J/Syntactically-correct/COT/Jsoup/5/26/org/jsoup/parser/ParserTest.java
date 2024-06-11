package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * Test class for Parser class
 */
public class ParserTest {

 private static final String HTML_PARSING_TEST = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 private static final String BODY_FRAGMENT_TEST = "<body><h1>Hello</h1></body>";
 private static final String BODY_FRAGMENT_RELAXED_TEST = "<body><h1>Hello</h1></body>";

 @Test
 public void testParse() {
 Parser parser = new Parser(HTML_PARSING_Test, "http://test.com");
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text());
 Equals(1, doc.body().childNodeSize());
 Equals(1, doc.head().childNodeSize());
 Equals(1, doc.childNodeSize());
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
 Parser parser = new Parser(HTML_PARSING_TEST, null);
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser(BODY_FRAGMENT_TEST, "http://test.com");
 Document doc = parser.parse();
 assertEquals("Hello", doc.body().child(0).child(0).text());
 Equals(0, doc.head().childNodeSize());
 Equals(1, doc.body().childNodeSize());
 Equals(1, doc.childNodeSize());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser(BODY_FRAGMENT_RELAXED_TEST, "http://test.com");
 Document doc = parser.parse();
 assertEquals("Hello", doc.body().child(0).child(0).text());
 Equals(0, doc.head().childNodeSize());
 Equals(1, doc.body().childNodeSize());
 Equals(1, doc.childNodeSize());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html>", "http://test.com");
 Document doc = parser.parse();
 Equals(1, doc.childNodeSize());
 Element html = doc.child(0);
 Equals("html", html.tagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseStartTagWithInvalidTagName() {
 Parser parser = new Parser("<zeus>", "http://test.com");
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><h1>Hello</h1></body></html>");
 Document doc = parser.parse();
 Equals(1, doc.body().childNodeSize());
 Element h1 = doc.body().child(0);
 Equals("h1", h1.tagName());
 Equals(1, h1.childNodeSize());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseEndTagWithInvalidTagName() {
 Parser parser = new Parser("<html><body><zeus></zeus></body></html>");
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>");
 Document doc = parser.parse();
 Equals(1, doc.childNodeSize());
 Element comment = doc.child(0);
 Equals("--", comment.tagName().substring(0, 2));
 Equals("This is a comment", comment.tag().text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns:html=\"http://www.w3.org/1999/xhtml\">");
 Document doc = parser.parse();
 Equals("http://www.w3.org/1999/xhtml", doc.xmlNamespaceURI());
 Equals("html", doc.xmlNamespacePrefix());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>Hello world</p></body></html>");
 Document doc = parser.parse();
 Equals("Hello world", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><body><![CDATA[Hello world]]></body></html>");
 Document doc = parser.parse();
 Equals("Hello world", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"description\"/>");
 Element img = parser.parse().body().child(0);
 assertEquals("image.jpg", img.attr("src"));
 assertEquals("description", img.attr("alt"));
 }

 @Test
 public void testParseAttributeNullValue() {
 Parser parser = new Parser("<img src=\"image.jpg\"/>");
 Element img = parser.parse().body().child(0);
 assertNull(img.attr("alt"));
 }

 @Test
 public void testParseAttributeEmptyValue() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"\"/>");
 Element img = parser.parse().body().child(0);
 assertEquals("", img.attr("alt"));
 }

 @Test
 public void testParseAttributeQuotedValue() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"Description\" class=\"classname\"/>");
 Element img = parser.parse().body().child(0);
 assertEquals("Description", img.attr("alt"));
 assertEquals("classname", img.attr("class"));
 }

 @Test
 public void testParseAttributeUnquotedValue() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=description class=classname/>");
 Element img = parser.parse().body().child(0);
 assertEquals("description", img.attr("alt"));
 assertEquals("classname", img.attr("class"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseAttributeMissingValue() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt/>");
 Element img = parser.parse().body().child(0);
 assertEquals(null, img.attr("alt"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseAttributeWithWhiteSpace() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\" description \"/>");
 Element img = parser.parse().body().child(0);
 assertEquals(" description ", img.attr("alt"));
 }

 @Test
 public void testParseAttributeWithSpecialChars() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"description$%^&*()_+={}[]|\\:\"\"/>");
 Element img = parser.parse().body().child(0);
 assertEquals("description$%^&*()_+={}[]|\\:", img.attr("alt"));
 }

 @Test
 public void testParseEmptyTag() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"description\"/>");
 Element img = parser.parse().body().child(0);
 assertFalse(img.hasText());
 }

 @Test
 public void testParseSelfClosingTag() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"description\" />");
 Element img = parser.parse().body().child(0);
 assertFalse(img.hasText());
 }

 @Test
 public void testParseImplicitParentTag() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"description\"/>");
 Document doc = parser.parse();
 Element img = doc.body().child(0);
 assertEquals("img", img.tagName());
 Element div = img.parent();
 assertEquals("div", div.tagName());
 }

 @Test
 public void testParseImplicitParentTagRelaxed() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"description\" />");
 Parser parserRelaxed = new Parser("<img src=\"image.jpg\" alt=\"description\" />", null, true);
 Document doc = parserRelaxed.parse();
 Element img = doc.body().child(0);
 assertEquals("img", img.tagName());
 Element div = img.parent();
 assertEquals("div", div.tagName());
 }

 @Test
 public void testParseInvalidParentTag() {
 Parser parser = new Parser("<html><body><h1><div></h1></div></body></html>");
 Document doc = parser.parse();
 assertNull(doc.body().child(0).child(0).parent());
 }

 @Test
 public void testParseInvalidParentTagRelaxed() {
 Parser parser = new Parser("<html><body><h1><div></h1></div></body></html>", null, true);
 Document doc = parser.parse();
 assertNull(doc.body().child(0).child(0).parent());
 }

 @Test
 public void testParseUnknownTag() {
 Parser parser = new Parser("<html><body><zeus></zeus></body></html>");
 Document doc = parser.parse();
 assertEquals(0, doc.childNodeSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownTagRelaxed() {
 Parser parser = new Parser("<html><body><zeus></zeus></body></html>", null, true);
 Document doc = parser.parse();
 assertEquals(0, doc.childNodeSize());
 }

 @Test
 public void testParseEmptyDocument() {
 Parser parser = new Parser("<html></html>");
 Document doc = parser.parse();
 assertEquals(0, doc.childNodeSize());
 }

}