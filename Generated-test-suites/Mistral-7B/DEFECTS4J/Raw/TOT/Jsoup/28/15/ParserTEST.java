package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

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
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 assertNotNull(doc);
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<script> alert('Hello'); </script><html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertNotNull(errors.get(0).getColumn());
 assertNull(doc);
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<script> alert('Hello'); </script><script> alert('Hello'); </script><html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(2, errors.size());
 assertNotNull(errors.get(0).getLine());
 assertNotNull(errors.get(0).getColumn());
 assertNotNull(errors.get(1).getLine());
 assertNotNull(errors.get(1).getColumn());
 assertNull(doc);
 }

 @Test
 public void testParseInputWithValidBaseUri() {
 String html = "<img src=\"images/image.jpg\" alt=\"Image\">";
 Document doc = Parser.parse(html, "http://example.com/test.html");
 Element img = doc.select("img").first();
 assertEquals("images/image.jpg", img.attr("src"));
 }

 @Test
 public void testParseInputWithInvalidBaseUri() {
 String html = "<img src=\"images/image.jpg\" alt=\"Image\">";
 Document doc = Parser.parse(html, "http://example.com");
 Element img = doc.select("img").first();
 assertNull(img);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
 assertEquals(2, nodes.size());
 Element li = (Element) nodes.get(0);
 assertEquals("Item 1", li.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element p1 = doc.select("p").get(0);
 assertEquals("Paragraph 1", p1.text());
 Element p2 = doc.select("p").get(1);
 assertEquals("Paragraph 2", p2.text());
 }

 @Test
 public void testParseBodyFragmentWithInvalidHtml() {
 String bodyHtml = "<script> alert('Hello'); </script>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNull(doc);
 }
}