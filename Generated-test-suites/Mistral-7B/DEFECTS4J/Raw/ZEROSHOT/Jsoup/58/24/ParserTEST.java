package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

public class ParserTest {

 private Parser parser;
 private Document doc;
 private String htmlInput;

 @Before
 public void setUp() {
 treeBuilderSetup();
 parser = new Parser(treeBuilder);
 htmlInput = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 }

 @After
 public void tearDown() {
 parser = null;
 doc = null;
 }

 @Test
 public void testParseInput() {
 doc = parser.parseInput(htmlInput, "http://test.com");
 assertEquals("Test Page", doc.title());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 assertSame(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertNotSame(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = new ArrayList<>();
 errors.add(new ParseError("Test Error", 1, 1));
 parser.setErrors(errors);
 List<ParseError> testErrors = parser.getErrors();
 assertEquals(errors.size(), testErrors.size());
 for (int i = 0; i < errors.size(); i++) {
 assertEquals(errors.get(i).getMessage(), testErrors.get(i).getMessage());
 assertEquals(errors.get(i).getLine(), testErrors.get(i).getLine());
 assertEquals(errors.get(i).getColumn(), testErrors.get(i).getColumn());
 }
 }

 @Test
 public void testSettings() {
 ParseSettings settings = new ParseSettings();
 parser.settings(settings);
 assertEquals(settings, parser.settings());
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse(htmlInput, "http://test.com");
 assertEquals("Test Page", doc.title());
 assertEquals("Hello World", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtmlInput() {
 Parser.parse(null, "http://test.com");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtmlInput() {
 Parser.parse("", "http://test.com");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseFragment() {
 Element context = Element.parse("<div></div>");
 List<Node> nodes = Parser.parseFragment("<p>Test fragment</p>", context, "http://test.com");
 assertEquals(1, nodes.size());
 assertEquals("Test fragment", nodes.get(0).text());
 }

 @Test
 public void testParseXmlFragment() {
 String xmlInput = "<xml><test>Test fragment</test></xml>";
 List<Node> nodes = Parser.parseXmlFragment(xmlInput, "http://test.com");
 assertEquals(1, nodes.size());
 assertEquals("Test fragment", nodes.get(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<body><p>Test fragment</p></body>", "http://test.com");
 assertEquals("Test fragment", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullHtmlInput() {
 Parser.parseBodyFragment(null, "http://test.com");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseBodyFragmentEmptyHtmlInput() {
 Document doc = Parser.parseBodyFragment("", "http://test.com");
 assertTrue(doc.body().text().isEmpty());
 }
}