package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Before
 public void setUp() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><p>Hello World</p></body></html>";
 document = parser.parseInput(html, "http://localhost");
 assertNotNull(document);
 assertEquals("Hello World", document.body().text());
 assertNull(parser.getErrors().size());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><head></head><body><p>Hello World</p><script>alert('Hi')</script></body></html>";
 parser.setTrackErrors(5);
 document = parser.parseInput(html, "http://localhost");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected char 40 '(' found when reading start tag", error.getMessage());
 assertNull(document.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://localhost");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder treeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(treeBuilder);
 assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsZero() {
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><head></head><body><p>Hello World</p></body></html>";
 Document doc = Parser.parse(html, "http://localhost");
 assertEquals(html, doc.html());
 assertEquals("Hello World", doc.body().text());
 assertNull(Parser.getErrors());

 Element context = Jsoup.parse("<div><p>Test</p></div>").body();
 List<Node> nodes = Parser.parseFragment("<script>alert('Hi')</script>", context, "http://localhost");
 assertEquals(1, nodes.size());
 assertNotNull(nodes.get(0));
 assertTrue(nodes.get(0) instanceof Element);
 assertEquals("script", nodes.get(0).tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragment(html, "http://localhost");
 assertEquals(html, doc.html());
 assertEquals("Hello World", doc.body().text());
 assertNull(Parser.getErrors());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String html = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://localhost");
 assertEquals(html, doc.html());
 assertEquals("Hello World", doc.body().text());
 assertNull(Parser.getErrors());
 }

 @Test
 public void testParseFragment() {
 String html = "<script>alert('Hi')</script>";
 Element context = Jsoup.parse("<div><p>Test</p></div>").body();
 List<Node> nodes = Parser.parseFragment(html, context, "http://localhost");
 assertEquals(1, nodes.size());
 assertNotNull(nodes.get(0));
 assertTrue(nodes.get(0) instanceof Element);
 assertEquals("script", nodes.get(0).tagName());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String escapedString = "&lt;p&gt; &amp; Hello &apos;World&apos; &quot; test &gt;";
 String unescapedString = Parser.unescapeHtmlEntities(escapedString);
 assertEquals("<p> & Hello 'World' \" test </p>", unescapedString);
 }
}