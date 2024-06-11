package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
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
 Document expectedDoc = Jsoup.parse(html);
 Document doc = parser.parseInput(html, null);
 assertEquals(expectedDoc, doc);
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com/";
 Document expectedDoc = Jsoup.parse(html, baseUri);
 Document doc = parser.parseInput(html, baseUri);
 assertEquals(expectedDoc, doc);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 Parser parserWithNoTracking = parser.setTrackErrors(0);
 assertFalse(parserWithNoTracking.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(new ParseError("Test Error", 1, 2, ""));
 Parser parserWithErrors = parser.setTrackErrors(1);
 String htmlWithError = "<html><body><h1>Hello World<img src='invalid'>";
 parserWithErrors.parseInput(htmlWithError, null);
 List<ParseError> actualErrors = parserWithErrors.getErrors();
 assertEquals(expectedErrors.size(), actualErrors.size());
 for (int i = 0; i < expectedErrors.size(); i++) {
 assertEquals(expectedErrors.get(i).getMessage(), actualErrors.get(i).getMessage());
 assertEquals(expectedErrors.get(i).getLine(), actualErrors.get(i).getLine());
 assertEquals(expectedErrors.get(i).getColumn(), actualErrors.get(i).getColumn());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputEmptyString() {
 parser.parseInput("", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Fragment</p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Document expectedDoc = Jsoup.parse("<html><body><div><p>Fragment</p></div></body></html>");
 List<Node> expectedNodes = expectedDoc.body().childNodes();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 assertEquals(expectedNodes.size(), nodes.size());
 for (int i = 0; i < nodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = nodes.get(i);
 assertEquals(expectedNode.nodeName(), actualNode.nodeName());
 }
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div><p>Body Fragment</p></div>";
 Document expectedDoc = Parser.parseBodyFragment(bodyHtml, null);
 assertEquals(expectedDoc.head().toString(), "");
 assertEquals(expectedDoc.body().toString(), "<div><p>Body Fragment</p></div>");
 }

 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, null);
 }
}