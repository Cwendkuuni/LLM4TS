package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><head><title>Test</title></head>" +
 "<body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 String baseUri = "http://example.com";
 parser.parseInput(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 String html = "<html><head><title>Test</title></head>" +
 "<body><h1>Hello World</h1></body></html>";
 parser.parseInput(html, null);
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetTreeBuilder() {
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testGetErrors() {
 String invalidHtml = "<html><body><script>alert('Hello')</script><body>";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(invalidHtml, "http://example.com").getErrors();
 assertEquals(1, errors.size());
 assertEquals("Error parsing start tag : body", errors.get(0).getMessage());
 }

 @Test
 public void testGetErrorsEmpty() {
 Document doc = parser.parseInput("<html><head><title>Test</title></head>" +
 "<body><h1>Hello World</h1></body></html>", "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());
 }

 @Test
 public void testParseFragment() {
 Element context = new Element("div");
 String fragmentHtml = "<p>Test</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertEquals(1, nodes.size());
 assertEquals("Test", nodes.get(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element body = doc.body();
 assertEquals(1, body.childNodeSize());
 assertEquals("Test", body.child(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Test</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Element body = doc.body();
 assertEquals(1, body.childNodeSize());
 assertEquals("Test", body.child(0).text());
 }
}