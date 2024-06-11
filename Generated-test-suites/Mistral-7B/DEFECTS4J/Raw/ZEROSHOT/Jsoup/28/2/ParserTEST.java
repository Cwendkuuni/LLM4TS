package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Test
 void testParserWithDefaults() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String html = "<html><body></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");

 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.head().childNodes());
 assertNotNull(doc.body());
 assertNull(doc.body().childNodes());
 }

 @Test
 void testParserWithTrackErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 parser.setTrackErrors(1);

 String htmlWithError = "<html><body><script>"; // scripts are not allowed in HTML by default
 String baseUri = "http://example.com";

 String errorMessage = assertThrows(IllegalArgumentException.class, () -> {
 parser.parseInput(htmlWithError, baseUri);
 }).getMessage();

 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals(ParseError.TAG_MISMATCH, errors.get(0).getType());
 assertEquals("Expected: end tag <body>\n" +
 "Actual: <script>\n" +
 "at line 2, column 1", errorMessage);
 }

 @Test
 void testParserWithCustomTreeBuilder() {
 treeBuilder = new XmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String xml = "<root><element /></root>";
 Document doc = parser.parseInput(xml, "http://example.com");

 assertNotNull(doc);
 Element root = doc.rootElement();
 assertNotNull(root);
 Element element = root.child(0);
 assertNotNull(element);
 }

 @Test
 void testParseFragment() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String fragmentHtml = "<div class='test'><p>Hello World</p></div>";
 Document doc = Document.createShell("http://example.com");
 Element context = doc.body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");

 assertNotNull(nodes);
 assertFalse(nodes.isEmpty());
 Node node = nodes.get(0);
 assertEquals("div", node.tagName());
 assertEquals("class", node.tagName().toLowerCase() + " attributename");
 assertEquals("test", node.attr("class"));
 assertNotNull(node.childNodes());
 assertEquals(1, node.childNodes().size());
 Element child = (Element) node.childNodes().get(0);
 assertEquals("p", child.tagName());
 assertEquals("Hello World", child.text());
 }

 @Test
 void testParseBodyFragment() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String bodyHtml = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.head().childNodes());
 assertNotNull(doc.body());
 Element body = doc.body();
 assertNotNull(body);
 assertFalse(body.childNodes().isEmpty());
 Element p = (Element) body.childNodes().get(0);
 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 }

 @Test
 void testParseWithCustomBaseUri() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String html = "<img src=\"test.html\" alt=\"test\">";
 Document doc = parser.parseInput(html, "http://example.com/base");

 Element img = doc.select("img").first();
 String imgSrc = img.attr("src");
 assertEquals("http://example.com/base/test.html", imgSrc);
 }

 @Test
 void testParseWithDefaultBaseUri() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String html = "<img src=\"test.html\" alt=\"test\">";
 Document doc = parser.parseInput(html, null);

 Element img = doc.select("img").first();
 String imgSrc = img.attr("src");
 assertEquals("test.html", imgSrc);
 }

 @Test
 void testParseWithCustomTreeBuilderAndCustomBaseUri() {
 treeBuilder = new XmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String xml = "<root><element><subelement src=\"test.xml\"></subelement></element></root>";
 Document doc = parser.parseInput(xml, "http://example.com/base");

 Element root = doc.rootElement();
 Element element = root.element(0);
 Element subelement = element.element(0);
 String subelementSrc = subelement.attr("src");
 assertEquals("http://example.com/base/test.xml", subelementSrc);
 }

 @Test
 void testParseWithCustomTreeBuilderAndNoBaseUri() {
 treeBuilder = new XmlTreeBuilder();
 parser = new Parser(treeBuilder);

 String xml = "<root><element><subelement src=\"test.xml\"></subelement></element></root>";
 Document doc = parser.parseInput(xml, null);

 Element root = doc.rootElement();
 Element element = root.element(0);
 Element subelement = element.element(0);
 String subelementSrc = subelement.attr("src");
 assertNull(subelementSrc);
 }

 @Test
 void testGetTreeBuilder() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 void testSetTreeBuilder() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(new XmlTreeBuilder());
 parser.setTreeBuilder(treeBuilder);
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 void testIsTrackErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 void testSetTrackErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 parser.setTrackErrors(1);
 assertEquals(1, parser.getMaxErrors());
 parser.setTrackErrors(2);
 assertEquals(2, parser.getMaxErrors());
 parser.setTrackErrors(0);
 assertEquals(0, parser.getMaxErrors());
 }

 @Test
 void testGetErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 List<ParseError> expectedErrors = List.of(new ParseError("Test error", 1, 1));
 parser.setErrors(expectedErrors);
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 void testParseWithMaxErrorsReached() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 parser.setTrackErrors(2);
 String htmlWithErrors = "<html><body><div><script></script></div></body></html>";
 assertThrows(ParseErrorList.ErrorLimitExceededException.class, () -> {
 parser.parseInput(htmlWithErrors, "http://example.com");
 });
 }

 @Test
 void testParseWithMaxErrorsReachedAndMultipleErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 parser.setTrackErrors(2);
 String htmlWithMultipleErrors = "<html><body><div><script></script></div><div><script></script></div></body></html>";
 assertThrows(ParseErrorList.ErrorLimitExceededException.class, () -> {
 parser.parseInput(htmlWithMultipleErrors, "http://example.com");
 });
 }

 @Test
 void testParseWithMaxErrorsReachedAndLessThanMaxErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 parser.setTrackErrors(2);
 String htmlWithLessThanMaxErrors = "<html><body><div><script></script></div></body></html>";
 List<ParseError> expectedErrors = List.of(new ParseError("Test error", 1, 1), new ParseError("Test error", 1, 2));
 parser.setErrors(expectedErrors);
 List<ParseError> actualErrors = parser.parseInput(htmlWithLessThanMaxErrors, "http://example.com").errors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 void testStaticParse() {
 String html = "<html><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertNotNull(doc);
 }

 @Test
 void testStaticParseFragment() {
 String fragmentHtml = "<div class='test'><p>Hello World</p></div>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
 assertNotNull(nodes);
 assertFalse(nodes.isEmpty());
 }

 @Test
 void testStaticParseBodyFragment() {
 String bodyHtml = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNotNull(doc);
 }

 @Test
 void testStaticParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertNotNull(doc);
 }
 }