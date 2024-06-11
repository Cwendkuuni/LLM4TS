package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.parse.ParseSettings;
import org.jsoup.parse.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private ParseSettings parseSettings;
 private ParseErrorList parseErrorList;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parseSettings = parseSettings();
 parser = new Parser(treeBuilder);
 parseErrorList = new ArrayList<>();
 }

 @Test
 public void testCreateParser() {
 assertNotNull(parser);
 }

 @Test
 public void testCreateParserWithTreeBuilder() {
 Parser parser1 = new Parser(treeBuilder);
 Parser parser2 = new Parser(treeBuilder);
 assertSame(treeBuilder, parser1.getTreeBuilder());
 assertSame(treeBuilder, parser2.getTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><body><h1><script>alert('test')</script></h1></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertTrue(errors.size() > 0);
 assertEquals("Unexpected character <: script", errors.get(0).getMessage());
 assertFalse(errors.get(0).getLine() > 0);
 assertNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithEmptyHtml() {
 parser.parseInput("", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder expectedTreeBuilder = newTreeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithNegativeNumber() {
 parser.setTrackErrors(-1);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 assertNull(parser.getErrors());
 parser.setTrackErrors(1);
 Document doc = parser.parseInput("<html><body><script> alert('test')</script></body></html>", "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertFalse(errors.isEmpty());
 }

 @Test
 public void testGetErrorsWhenNoErrors() {
 assertNull(parser.getErrors());
 }

 @Test
 public void testSettings() {
 ParseSettings expectedParseSettings = parseSettings;
 ParseSettings actualParseSettings = parser.settings();
 assertSame(expectedParseSettings, actualParseSettings);
 }

 @Test
 public void testSettingsWhenNull() {
 ParseSettings expectedParseSettings = parseSettings;
 parser.settings(null);
 ParseSettings actualParseSettings = parser.settings();
 assertNull(actualParseSettings);
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Fragment</div>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
 assertEquals(1, nodes.size());
 Element node = (Element) nodes.get(0);
 assertEquals("div", node.tagName());
 assertEquals("Fragment", node.text());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><tag>Test</tag></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");
 assertEquals(1, nodes.size());
 Element node = (Element) nodes.get(0);
 assertEquals("tag", node.tagName());
 assertEquals("Test", node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Body Fragment</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("<html><body><p>Body Fragment</p></body></html>", doc.html());
 }

 @Test
 public void testUnescapeEntities() {
 String encodedString = "&lt;Hello&gt; World&nbsp;!";
 String decodedString = Parser.unescapeEntities(encodedString, false);
 assertEquals("<Hello> World!", decodedString);
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 String bodyHtml = "<body><p>Body Fragment</p></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("<html><body><p>Body Fragment</p></body></html>", doc.html());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseBodyFragmentRelaxedDeprecatedWithNull() {
 Parser.parseBodyFragmentRelaxed(null, "http://example.com");
 }
}