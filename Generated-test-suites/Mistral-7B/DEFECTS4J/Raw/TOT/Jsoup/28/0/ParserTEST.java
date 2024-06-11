package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals("Hello World", doc.title());
 Element h1 = doc.select("h1").first();
 assertNotNull(h1);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><script>alert('foo');</script>";
 ParseErrorList errors = ParseErrorList.tracking(5);
 Document doc = parser.parseInput(html, "http://example.com", errors);

 assertEquals(5, errors.size());
 for (ParseError error : errors) {
 assertEquals("Unexpected 'script'", error.message());
 }
 assertNull(doc);
 }

 @Test
 public void testParseInputEmptyString() {
 Document doc = parser.parseInput("", "http://example.com");
 assertNull(doc);
 }

 @Test
 public void testParseInputNullString() {
 assertNull(parser.parseInput(null, "http://example.com"));
 }

 @Test
 public void testParseInputWithBaseUriNull() {
 String html = "<html><a href='/foo'>Link</a></html>";
 Document doc = parser.parseInput(html, null);
 assertNotNull(doc);
 assertEquals("", doc.baseUri());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser1 = new Parser(new HtmlTreeBuilder());
 Parser parser2 = parser1.setTreeBuilder(new XmlTreeBuilder());
 assertEquals(XmlTreeBuilder.class, parser2.treeBuilder.getClass());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 assertFalse(parser.isTrackErrors());

 Parser parser1 = parser.setTrackErrors(5);
 assertTrue(parser1.isTrackErrors());
 }

 @Test
 public void testParseStaticMethods() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html);
 assertEquals("", doc.baseUri());
 assertEquals("Hello World", doc.title());
 Element h1 = doc.select("h1").first();
 assertNotNull(h1);
 assertEquals("Hello World", h1.text());
 }
}