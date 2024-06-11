package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 Document document = parser.parseInput(html, baseUri);
 List<ParseError> parseErrors = parser.getErrors();

 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title().text());
 Assert.assertEquals("Hello World", document.body().text());
 Assert.assertEquals(0, parseErrors.size());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><head><title>Test</title></head><body><p>Hello <strong>World</strong></p></body></html>";
 String baseUri = "http://example.com";
 int maxErrors = 1;
 List<ParseError> errors = new ArrayList<>();

 parser.setTrackErrors(maxErrors);
 Document document = parser.parseInput(html, baseUri);
 List<ParseError> parseErrors = parser.getErrors();

 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title().text());
 Assert.assertEquals("Hello <strong>World</strong>", document.body().text());
 Assert.assertEquals(1, parseErrors.size());
 Assert.assertEquals("Expected tag name \"p\", but found start tag named \"strong\"", parseErrors.get(0).getMessage());
 }

 @Test(expected = RuntimeException.class)
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><head><title>Test</title></head><body><p>Hello </p><p>World</p></body></html>";
 String baseUri = "http://example.com";
 int maxErrors = 1;
 List<ParseError> errors = new ArrayList<>();

 parser.setTrackErrors(maxErrors);
 for (int i = 0; i < 10; i++) {
 Document document = parser.parseInput(html, baseUri);
 List<ParseError> parseErrors = parser.getErrors();
 }
 }

 @Test
 public void testParseInputWithCustomSettings() {
 String html = "<html><head><title>Test</title></head><body><p>Hello <strong>World</strong></p></body></html>";
 String baseUri = "http://example.com";
 ParseSettings settings = new ParseSettings.Builder().build();
 settings.setOmitTagNames(true);
 parser.settings(settings);
 List<ParseError> errors = new ArrayList<>();

 Document document = parser.parseInput(html, baseUri);
 List<ParseError> parseErrors = parser.getErrors();

 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title().text());
 Assert.assertEquals("Hello World", document.body().text());
 Assert.assertEquals(0, parseErrors.size());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Hello World</p>";
 Element context = new Element("html");
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

 Assert.assertEquals(1, nodes.size());
 Assert.assertInstanceOf(Element.class, nodes.get(0));
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<root><element>Hello World</element></root>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

 Assert.assertEquals(1, nodes.size());
 Assert.assertInstanceOf(Element.class, nodes.get(0));
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World</p>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 List<ParseError> parseErrors = parser.getErrors();

 Assert.assertNotNull(document);
 Assert.assertEquals("", document.head().text());
 Assert.assertEquals("Hello World", document.body().text());
 Assert.assertEquals(0, parseErrors.size());
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String unescapedString = Parser.unescapeEntities(string, false);

 Assert.assertEquals("<p>Hello & World</p>", unescapedString);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Hello World</p>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 List<ParseError> parseErrors = parser.getErrors();

 Assert.assertNotNull(document);
 Assert.assertEquals("", document.head().text());
 Assert.assertEquals("Hello World", document.body().text());
 Assert.assertEquals(0, parseErrors.size());
 }
}