package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.junit.Assert;

import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private ParseSettings settings;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 settings = treeBuilder.defaultSettings();
 }

 @Test
 public void testParseInputStringString() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://test.com");
 Element title = doc.title();
 Element h1 = doc.body().getElementsByTag("h1").get(0);

 Assert.assertEquals("Test", title.text());
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputStringStringWithErrors() {
 String invalidHtml = "<html><head><title>Test</title></head><body><script>alert('Hello World')</script><h1>Hello World</h1></body></html>";
 Parser parserWithErrors = new Parser(treeBuilder);
 parserWithErrors.setTrackErrors(5);
 List<ParseError> errors = parserWithErrors.parseInput(invalidHtml, "http://test.com").getErrors();

 Assert.assertEquals(5, errors.size());
 Assert.assertNotNull(errors.get(0).getMessage());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 Assert.assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 Assert.assertSame(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = parser.parseInput("<html><body><script>alert('Hello World')</script></body></html>", "http://test.com").getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 Element h1 = doc.body().getElementsByTag("h1").get(0);

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseXmlFragment() {
 String xmlFragment = "<xml><tag>Hello World</tag></xml>";
 List<Node> nodes = Parser.parseXmlFragment(xmlFragment, "http://test.com");
 Element tag = (Element) nodes.get(0);

 Assert.assertNotNull(nodes);
 Assert.assertEquals("Hello World", tag.text());
 }

 @Test
 public void testParseInputStringStringWithSettings() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 ParseSettings settingsWithWhitespaceTrim = new ParseSettings.Builder().trimWhitespaceText().build();
 Document doc = parser.parseInput(html, "http://test.com", settingsWithWhitespaceTrim);
 Element title = doc.title();
 Element h1 = doc.body().getElementsByTag("h1").get(0);

 Assert.assertEquals("Test", title.text().trim());
 Assert.assertEquals("Hello World", h1.text().trim());
 }
}