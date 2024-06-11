package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parse.HtmlTreeBuilder;
import org.jsoup.parse.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit. Assert;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document document;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Test</h1></body></html>";
 String baseUri = "http://test.com";
 document = parser.parseInput(html, baseUri);

 Assert.assertNotNull(document);
 Assert.assertEquals("html", document.tagName());
 Assert.assertEquals("Test", document.head().children().get(0).text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><script>document.write('Hello')</script><h1>Test</h1></body></html>";
 String baseUri = "http://test.com";
 parser.setTrackErrors(1);
 document = parser.parseInput(html, baseUri);

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unexpected 'script' tag", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<html><body><script>document.write('Hello')</script><script>document.write('World')</script><h1>Test</h1></body></html>";
 String baseUri = "http://test.com";
 parser.setTrackErrors(2);
 document = parser.parseInput(html, baseUri);

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(2, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput(html, null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 Assert.assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(xmlTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 Assert.assertEquals(xmlTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsZero() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsPositive() {
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParse() {
 Document document = Parser.parse("<html><body><h1>Test</h1></body></html>", "http://test.com");

 Assert.assertNotNull(document);
 Assert.assertEquals("html", document.tagName());
 Assert.assertEquals("Test", document.head().children().get(0).text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Test</h1>";
 Element context = new Element("div");
 Document document = Document.createShell("http://test.com");
 document.body().appendChild(context);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://test.com");

 Node node = nodes.get(0);
 Assert.assertEquals("h1", node.tagName());
 Assert.assertEquals("Test", node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<html><body><h1>Test</h1></body></html>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://test.com");

 Assert.assertNotNull(document);
 Assert.assertEquals("html", document.tagName());
 Assert.assertEquals("Test", document.body().children().get(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullHtml() {
 Parser.parseBodyFragment(null, "http://test.com");
 }

 @Test
 public void testParseBodyFragmentNullBaseUri() {
 Document document = Parser.parseBodyFragment("<html><body><h1>Test</h1></body></html>", null);

 Assert.assertNotNull(document);
 Assert.assertEquals("html", document.tagName());
 Assert.assertEquals("Test", document.body().children().get(0).text());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String htmlEscaped = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String htmlUnescaped = parser.unescapeHtmlEntities(htmlEscaped);

 Assert.assertEquals("<p>Hello & World</p>", htmlUnescaped);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document document = Parser.parseBodyFragmentRelaxed("<html><body><h1>Test</h1></body></html>", "http://test.com");

 Assert.assertNotNull(document);
 Assert.assertEquals("html", document.tagName());
 Assert.assertEquals("Test", document.body().children().get(0).text());
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();

 Assert.assertNotNull(parser);
 Assert.assertTrue(parser.isTrackErrors());
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();

 Assert.assertNotNull(parser);
 Assert.assertFalse(parser.isTrackErrors());
 Assert.assertNotEquals(treeBuilder, parser.getTreeBuilder());
 }
}