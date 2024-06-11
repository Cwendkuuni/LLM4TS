package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parse.ParseError;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document htmlDocument;
 private Document xmlDocument;
 private Element rootElement;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 htmlDocument = parser.parseInput("<html><body><h1>Hello</h1></body></html>", null);
 xmlDocument = parser.parseInput("<root><data>42</data></root>", null);
 rootElement = htmlDocument.body();
 }

 @Test
 public void testCreateParserWithTreeBuilder() {
 Parser parser = new Parser(treeBuilder);
 assertNotNull(parser);
 assertNotNull(parser.getTreeBuilder());
 }

 @Test
 public void testParseInputWithHtmlDocument() {
 assertEquals("<html><body><h1>Hello</h1></body></html>", htmlDocument.html());
 }

 @Test
 public void testParseInputWithXmlDocument() {
 assertEquals("<root><data>42</data></root>", xmlDocument.html());
 }

 @Test
 public void testGetTreeBuilder() {
 Parser parser = new Parser(treeBuilder);
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Parser parserWithNewTreeBuilder = parser.setTreeBuilder(new XmlTreeBuilder());
 assertNotNull(parserWithNewTreeBuilder);
 assertFalse(parserWithNewTreeBuilder.isTrackErrors());
 assertFalse(parserWithNewTreeBuilder.isTrackErrors());
 assertNotNull(parserWithNewTreeBuilder.getTreeBuilder());
 assertEquals(new XmlTreeBuilder(), parserWithNewTreeBuilder.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 assertFalse(parser.isTrackErrors());
 Parser parserWithTrackErrors = parser.setTrackErrors(1);
 assertTrue(parserWithTrackErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 int initialMaxErrors = parser.getMaxErrors();
 parser.setTrackErrors(1);
 assertEquals(1, parser.getMaxErrors());
 parser.setTrackErrors(0);
 assertEquals(initialMaxErrors, parser.getMaxErrors());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetErrorsWithDefaultParser() {
 parser.getErrors();
 }

 @Test
 public void testGetErrorsWithTrackingParser() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(0, errors.size());
 }

 @Test
 public void testParseFragment() {
 List<Node> nodeList = Parser.parseFragment("<p>First Paragraph</p><p>Second Paragraph</p>", rootElement, null);
 assertEquals(2, nodeList.size());
 }

 @Test
 public void testParseBodyFragment() {
 Document document = Parser.parseBodyFragment("<p>First Paragraph</p><p>Second Paragraph</p>", null);
 assertEquals("<html><body><p>First Paragraph</p><p>Second Paragraph</p></body></html>", document.html());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String htmlString = "&lt;p&gt;Hello&amp;World&lt;/p&gt;";
 String expected = "<p>HelloWorld</p>";
 assertEquals(expected, Parser.unescapeHtmlEntities(htmlString));
 }

 @Test
 public void testParse() {
 Document document = Parser.parse("<html><body><h1>Hello</h1></body></html>", null);
 assertEquals("<html><body><h1>Hello</h1></body></html>", document.html());
 }

 @Test
 public void testParseFragmentWithContext() {
 Element context = new Element("div");
 context.text("Context");
 List<Node> nodeList = Parser.parseFragment("<p>Fragment</p>", context, null);
 assertEquals("Context", context.html());
 assertEquals(1, nodeList.size());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document document = Parser.parseBodyFragmentRelaxed("<html><body><h1>Hello</h1></body></html>", null);
 assertEquals("<html><body><h1>Hello</h1></body></html>", document.html());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseBodyFragmentRelaxedWithErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());
 Document document = Parser.parseBodyFragmentRelaxed("<html><body><script>alert('Hello')</script></body></html>", null);
 List<ParseError> errorsAfterParse = parser.getErrors();
 assertEquals(1, errorsAfterParse.size());
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 assertNotNull(parser);
 assertNotNull(parser.getTreeBuilder());
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 assertNotNull(parser);
 assertNotNull(parser.getTreeBuilder());
 }
}