package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.nodes.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document document;

 @Before
 public void setup() {
 treeBuilder = mock(TreeBuilder.class);
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput_ValidHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, "http://example.com");
 assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test
 public void testParseInput_InvalidHtml() {
 String html = "<html<title>Test</title>";
 parser = new Parser(treeBuilder) {{ maxErrors = 1; }};
 List<ParseError> errors = new ArrayList<>();
 ParseErrorList errorList = Mockito.mock(ParseErrorList.class);
 when(errorList.add(Mockito.any(ParseError.class))).then(invocation -> errors.add(invocation.getArgument(0)));
 PowerMockito.mockStatic(ParseErrorList.class);
 when(ParseErrorList.tracking(Mockito.anyInt())).thenReturn(errorList);
 errors.clear();
 document = parser.parseInput(html, "http://example.com");
 assertEquals("<html<title>Test</title>", document.html());
 assertEquals(1, errors.size());
 ParseError parseError = errors.get(0);
 assertEquals("Error parsing start tag : html", parseError.getMessage());
 assertFalse(parseError.isRecoverable());
 }

 @Test
 public void testParseInput_EmptyHtml() {
 String html = "";
 document = parser.parseInput(html, "http://example.com");
 assertNull(document);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
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
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = new ArrayList<>();
 ParseErrorList errorList = Mockito.mock(ParseErrorList.class);
 when(errorList.add(Mockito.any(ParseError.class))).then(invocation -> errors.add(invocation.getArgument(0)));
 when(errorList.size()).thenReturn(3);
 PowerMockito.mockStatic(ParseErrorList.class);
 when(ParseErrorList.tracking(Mockito.anyInt())).thenReturn(errorList);
 assertEquals(3, parser.getErrors().size());
 }

 @Test
 public void testParse_ValidHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 document = Parser.parse(html, "http://example.com");
 assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test
 public void testParse_InvalidHtml() {
 String html = "<html<title>Test</title>";
 List<ParseError> errors = new ArrayList<>();
 ParseErrorList errorList = Mockito.mock(ParseErrorList.class);
 when(errorList.add(Mockito.any(ParseError.class))).then(invocation -> errors.add(invocation.getArgument(0)));
 PowerMockito.mockStatic(ParseErrorList.class);
 when(ParseErrorList.noTracking()).thenReturn(errorList);
 errors.clear();
 document = Parser.parse(html, "http://example.com");
 assertEquals("<html<title>Test</title>", document.html());
 assertEquals(1, errors.size());
 ParseError parseError = errors.get(0);
 assertEquals("Error parsing start tag : html", parseError.getMessage());
 assertFalse(parseError.isRecoverable());
 }

 @Test
 public void testParseFragment_ValidHtml() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = mock(Element.class);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertEquals("<div>Hello World</div>", node.html());
 }

 @Test
 public void testParseFragment_EmptyHtml() {
 String fragmentHtml = "";
 Element context = mock(Element.class);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseBodyFragment_ValidHtml() {
 String bodyHtml = "<div>Hello World</div>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><div>Hello World</div></body></html>", document.html());
 }

 @Test
 public void testParseBodyFragmentRelaxed_ValidHtml() {
 String bodyHtml = "<div>Hello World</div>";
 document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><div>Hello World</div></body></html>", document.html());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseBodyFragmentRelaxed_Deprecated() {
 Parser.parseBodyFragmentRelaxed("", "");
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(new HtmlTreeBuilder(), actualTreeBuilder);
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(new XmlTreeBuilder(), actualTreeBuilder);
 }
}