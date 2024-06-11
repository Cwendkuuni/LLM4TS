package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
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
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.getElementsByTag("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test
 public void testParseInputEmptyHtml() {
 parser.parseInput("", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrorsWhenTrackErrorsIsDisabled() {
 parser.parseInput("<html></html>", "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testGetErrorsWhenTrackErrorsIsEnabled() {
 parser.setTrackErrors(3);
 String invalidHtml = "<html><script>alert('Hello')</script></html>";
 parser.parseInput(invalidHtml, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertTrue(errors.size() <= 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetErrorsWhenMaxErrorsReached() {
 parser.setTrackErrors(1);
 parser.parseInput("<html><script>alert('Hello')</script></html>", "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(1, errors.size());
 String invalidHtml2 = "<html><script>alert('Hello')</script></html>";
 parser.parseInput(invalidHtml2, "http://example.com");
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><head></head><body><h1>Test</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.getElementsByTag("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticFunctionsNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test
 public void testParseStaticFunctionsEmptyHtml() {
 Parser.parse("", "http://example.com");
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.getElementsByTag("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test
 public void testParseBodyFragmentEmptyBodyHtml() {
 Parser.parseBodyFragment("", "http://example.com");
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Test</div>";
 Element context = new Element("html");
 Document doc = Document.createShell("http://example.com");
 List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Assert.assertNotNull(nodeList);
 Assert.assertEquals(1, nodeList.size());
 Node node = nodeList.get(0);
 doc.body().appendChild(node);
 Assert.assertEquals("Test", doc.getElementsByClass("div").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, context, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment(fragmentHtml, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment(fragmentHtml, context, null);
 }

 @Test
 public void testparseBodyFragmentRelaxedDeprecated() {
 String bodyHtml = "<body><h1>Test</h1></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.getElementsByTag("h1").text());
 }
}