package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Parser.java
 *
 * @author [Your Name]
 */
public class ParserTest {

 private static final String HTML_SAMPLE = "<html><head><title>Title</title></head><body><h1>Heading</h1><p>Paragraph <a href='link.html'>Link</a></p></body></html>";

 @Test
 public void testParse() {
 Parser parser = new Parser(HTML_SAMPLE, "http://example.com/");
 Document doc = parser.parse();
 Element root = doc.body();

 assertEquals("body", root.tagName());
 assertEquals("http://example.com/", doc.baseUri());

 Element title = root.child(0);
 assertEquals("h1", title.tagName());
 assertEquals("Heading", title.text());

 Element link = title.child(0);
 assertEquals("a", link.tagName());
 assertEquals("link.html", link.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser parser = new Parser(null, "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Paragraph</p>", "http://example.com/");
 Document doc = parser.parse();
 Element root = doc;

 assertEquals("document", root.tagName());
 assertEquals("http://example.com/", doc.baseUri());
 assertEquals("p", root.child(0).tagName());
 assertEquals("Paragraph", root.child(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<div><p>Paragraph</p></div>", "http://example.com/");
 Document doc = parser.parse();
 Element root = doc;

 assertEquals("document", root.tagName());
 assertEquals("http://example.com/", doc.baseUri());
 assertEquals("p", root.child(0).tagName());
 assertEquals("Paragraph", root.child(0).text());

 Element div = root.child(0);
 assertEquals("div", div.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullHtml() {
 Parser parser = new Parser(null, "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 Parser parser = new Parser("<p>Paragraph</p>", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testParseEmptyHtml() {
 Parser parser = new Parser("", "http://example.com/");
 }

 @Test(expected = IllegalStateException.class)
 public void testParseEmptyBaseUri() {
 Parser parser = new Parser("<html></html>", "");
 }

 @Test(expected = IllegalStateException.class)
 public void testParseMissingEndTag() {
 Parser parser = new Parser("<html><head><title>Title</title></body></html>", "http://example.com/");
 }

 @Test(expected = IllegalStateException.class)
 public void testParseNestedMissingEndTag() {
 Parser parser = new Parser("<html><head><title>Title</title><body><p>Paragraph<div", "http://example.com/");
 }

 @Test(expected = IllegalStateException.class)
 public void testParseInvalidTagName() {
 Parser parser = new Parser("<html><head><title>Title</title><foo></foo></html>", "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeValue() {
 Parser parser = new Parser("<p foo='<script>alert(\"Hello\")</script>'></p>", "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttributeName() {
 Parser parser = new Parser("<p foo='bar'></p>", "http://example.com/");
 }

 @Test(expected = IllegalStateException.class)
 public void testParseSelfClosingTagWithoutEndTag() {
 Parser parser = new Parser("<img src='image.jpg' />", "http://example.com/");
 }

 @Test
 public void testParseXmlDeclaration() {
 Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "http://example.com/");
 Document doc = parser.parse();
 assertEquals("about:legacy-compat", doc.documentElement().getDoctype().systemId());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!---->", "http://example.com/");
 Document doc = parser.parse();
 assertEquals("", doc.body().text());
 Element comment = doc.body().child(0);
 assertEquals("-->", comment.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<![CDATA[Hello World]]>", "http://example.com/");
 Document doc = parser.parse();
 Element root = doc;

 assertEquals("document", root.tagName());
 assertEquals("http://example.com/", doc.baseUri());
 assertEquals("", root.child(0).text());
 Element cdata = root.child(0).child(0);
 assertEquals("CDATA", cdata.tagName());
 assertEquals("Hello World", cdata.text());
 }

 private Element last(LinkedList<Element> stack) {
 return stack.getLast();
 }
}