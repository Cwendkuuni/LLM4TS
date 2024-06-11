package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParserTest {

 private Parser parser;
 private Parser htmlParser;
 private Parser xmlParser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 htmlParser = new Parser(treeBuilder);
 xmlParser = new Parser(new XmlTreeBuilder());
 parser = htmlParser;
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, null);
 assertEquals("Hello World", document.title());
 assertEquals("html", document.tagName());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 String baseUri = "https://example.com/";
 Document document = parser.parseInput(html, baseUri);
 String imgSrc = document.select("img").attr("src");
 assertEquals("image.jpg", imgSrc);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><script>throw new Error('Test');</script></body></html>";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(html, null).errors();
 assertEquals(1, errors.size());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Test</p></div>";
 Element context = Jsoup.parseBodyFragment("<p>Context</p>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 assertEquals(2, nodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullContext() {
 String fragmentHtml = "<div><p>Test</p></div>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
 fail();
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div><p>Test</p></div>";
 Document document = Parser.parseBodyFragment(bodyHtml, null);
 assertEquals("Test", document.body().text());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String string = "&amp; &lt; &gt; &quot; &apos;";
 assertEquals("&amp; &lt; &gt; \" \\' ", parser.unescapeHtml(string));
 }

 @Test
 public void testParseInputWithErrors() {
 parser = xmlParser;
 String xml = "<root><error><message>Test</message></error></root>";
 List<ParseError> errors = new ArrayList<>();
 errors.add(new ParseError("Test", 1, 1));
 parseInputWithErrors(xml, errors);
 assertEquals(1, errors.size());
 assertEquals("Test", errors.get(0).getMessage());
 }

 private void parseInputWithErrors(String input, List<ParseError> expectedErrors) {
 parser.setTrackErrors(expectedErrors.size());
 Document document = parser.parseInput(input, null);
 List<ParseError> actualErrors = document.errors();
 assertEquals(expectedErrors.size(), actualErrors.size());
 for (int i = 0; i < expectedErrors.size(); i++) {
 assertEquals(expectedErrors.get(i).getMessage(), actualErrors.get(i).getMessage());
 assertEquals(expectedErrors.get(i).getLine(), actualErrors.get(i).getLine());
 assertEquals(expectedErrors.get(i).getColumn(), actualErrors.get(i).getColumn());
 }
}

 @Test
 public void testParseInputWithNoErrors() {
 parser = htmlParser;
 String html = "<html><body><h1>Hello World</h1></body></html>";
 List<ParseError> errors = new ArrayList<>();
 parseInputWithErrors(html, errors);
 assertTrue(errors.isEmpty());
 }
}