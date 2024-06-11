package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, baseUri);

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 Assert.assertNull(doc);
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body><h1>Hello </h1><h1>World</h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 Document doc = null;
 int maxErrors = parser.getMaxErrors();
 for (int i = 0; i < maxErrors + 1; i++) {
 doc = parser.parseInput(html, baseUri);
 }

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(maxErrors + 1, errors.size());
 }

 @Test
 public void testParseInputWithNoTrackErrors() {
 String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(0);
 Document doc = parser.parseInput(html, baseUri);

 Assert.assertNotNull(doc);
 }

 @Test
 public void testParseInputWithInvalidBaseUri() {
 String html = "<img src=\"/invalidUri\" />";
 String baseUri = null;
 parser.parseInput(html, baseUri);

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error in tag html: baseUri is null", errors.get(0).getMessage());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder newParserTreeBuilder = parser.getTreeBuilder();

 Assert.assertEquals(newTreeBuilder, newParserTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = new Element("html");
 Document doc = Document.createShell("http://example.com");
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");

 Element div = (Element) nodes.get(0);
 Assert.assertEquals("Hello World", div.text());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<root><child>Hello World</child></root>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");

 Element child = (Element) nodes.get(0);
 Assert.assertEquals("Hello World", child.text());
 }
}