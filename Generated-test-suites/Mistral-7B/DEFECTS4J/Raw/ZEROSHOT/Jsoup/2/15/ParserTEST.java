package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

public class ParserTest {

 private static final String HTML_TEMPLATE = "<html><head><title>Test</title></head><body><div><p>Hello World!</p></div></body></html>";

 @BeforeClass
 public static void setUp() {
 Validate.noException(Parser.parse(HTML_TEMPLATE, ""));
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse(HTML_TEMPLATE, "");

 assertEquals("html", doc.tagName());
 assertNotNull(doc.title());
 assertEquals("Test", doc.title().text());
 assertFalse(doc.body().tagName().equals("html"));

 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("p", p.tagName());
 assertEquals("Hello World!", p.text());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<p>Hello World!</p>", "");

 assertEquals("p", doc.tagName());
 assertFalse(doc.head() != null);
 assertEquals("Hello World!", doc.body().text());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- test comment --></html>", "");
 Document doc = parser.parse();

 Element comment = doc.body().child(0);
 assertEquals(Comment.class, comment.getClass());
 assertEquals("test comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head></head><body></body></html>", "");
 Document doc = parser.parse();

 assertEquals("html", doc.tagName());
 assertEquals("http://www.w3.org/1999/xhtml", doc.xmlNamespaceURI());
 assertEquals("en", doc.xmlLangAttribute());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><div><p></p></div></body></html>", "");
 Document doc = parser.parse();

 assertEquals(4, doc.body().children().size());
 Element div = doc.body().get(1);
 Element p = div.get(0);
 assertEquals(0, p.children().size());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><body><div><p><span id=\"test\">Hello World!</span></p></div></body></html>", "");
 Document doc = parser.parse();

 Element span = doc.body().getElementsByTagAndClass("span", "test").first();
 assertEquals("span", span.tagName());
 assertNotNull(span.id());
 assertEquals("test", span.id());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<html><body><img src=\"test.png\" alt=\"test image\"></body></html>", "");
 Document doc = parser.parse();
 Element img = doc.body().get(0).getElementsByTag("img").first();

 assertEquals("src", img.tag().attributeSelect(" src ").get(0).getKey());
 assertEquals("test.png", img.tag().attributeSelect(" src ").get(0).getValue());
 assertEquals("alt", img.tag().attributeSelect(" alt ").get(0).getKey());
 assertEquals("test image", img.tag().attributeSelect(" alt ").get(0).getValue());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>Hello World!</p></body></html>", "");
 Document doc = parser.parse();
 Element p = doc.body().get(0);

 assertEquals(1, p.children().size());
 TextNode textNode = (TextNode) p.get(0);
 assertEquals("Hello World!", textNode.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><body><![CDATA[Hello World!]]></body></html>", "");
 Document doc = parser.parse();
 Element cdata = doc.body().get(0);

 assertEquals(1, cdata.children().size());
 DataNode dataNode = (DataNode) cdata.get(0);
 assertEquals("Hello World!", dataNode.text());
 }
}