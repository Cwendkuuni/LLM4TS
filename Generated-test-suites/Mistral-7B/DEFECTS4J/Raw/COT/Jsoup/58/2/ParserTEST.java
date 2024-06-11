package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
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
 Document doc = parser.parseInput(html, "");
 Element root = doc.rootElement();
 assertEquals("html", root.tagName());
 assertEquals("Hello World", root.child(0).child(0).text());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 assertEquals("http://example.com/image.jpg", doc.baseUri());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<script><!--evil-->";
 ParseErrorList errors = ParseErrorList.tracking(1);
 Document doc = parser.parseInput(html, "");
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected '<', expecting '<!', or '<?' at line 1, column 0", error.message());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expected = treeBuilder;
 TreeBuilder actual = parser.getTreeBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parserWithTrackErrors = parser.setTrackErrors(1);
 assertTrue(parserWithTrackErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 Parser parserWithoutTrackErrors = parser.setTrackErrors(0);
 assertFalse(parserWithoutTrackErrors.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<script><!--evil-->";
 ParseErrorList errors = ParseErrorList.tracking(1);
 parser.parseInput(html, "");
 List<ParseError> expected = new ArrayList<>();
 expected.add(errors.get(0));
 List<ParseError> actual = parser.getErrors();
 assertEquals(expected, actual);
 }

 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 assertEquals(ParseSettings.defaultSettings(), settings);
 }

 @Test
 public void testSettingsWithCustomSettings() {
 ParseSettings customSettings = new ParseSettings();
 Parser parserWithCustomSettings = parser.settings(customSettings);
 ParseSettings actualSettings = parserWithCustomSettings.settings();
 assertEquals(customSettings, actualSettings);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Fragment</div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Document doc = Parser.parseFragment(fragmentHtml, context, "");
 assertEquals(1, doc.body().child(0).childCount());
 assertEquals("Fragment", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><data>Data</data></xml>";
 Document doc = Parser.parseXmlFragment(fragmentXml, "");
 assertEquals(1, doc.child(0).childCount());
 assertEquals("Data", doc.child(0).child(0).text());
 }

 @Test
 public void testParseInputWithErrorHandling() {
 String html = "<h1><script>alert('Hello');</script>Hello World</h1>";
 ParseErrorList errors = ParseErrorList.tracking(10);
 Document doc = parser.parseInput(html, "");
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected '<', expecting '<script', at line 1, column 14", error.message());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div>Fragment</div>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");
 assertEquals(1, doc.child(0).childCount());
 assertEquals("Fragment", doc.child(0).child(0).text());
 }

 @Test
 public void testUnescapeEntities() {
 String html = "&lt;p&gt;<i>&amp;amp;</i>&lt;/p&gt;";
 String expected = "<p><i>&amp;</i></p>";
 String actual = Parser.unescapeEntities(html, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 String bodyHtml = "<div>Fragment</div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");
 assertEquals(1, doc.child(0).childCount());
 assertEquals("Fragment", doc.child(0).child(0).text());
 }
}