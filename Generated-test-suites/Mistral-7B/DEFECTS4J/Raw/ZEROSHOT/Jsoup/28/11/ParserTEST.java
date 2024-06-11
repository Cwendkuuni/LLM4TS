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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilderMock;
 private Document document;

 @Before
 public void setUp() {
 treeBuilderMock = Mockito.mock(TreeBuilder.class);
 parser = new Parser(treeBuilderMock);
 document = Mockito.mock(Document.class);
 }

 @Test
 public void testNewParserWithTreeBuilder() {
 Parser parser = new Parser(treeBuilderMock);
 assertEquals(treeBuilderMock, parser.getTreeBuilder());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<html><body><p>Hello World</p></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();

 Mockito.when(treeBuilderMock.parse(html, baseUri, Mockito.any(ParseErrorList.class))).thenReturn(document);
 Mockito.when(treeBuilderMock.getErrorList()).thenReturn(errors);

 Document result = parser.parseInput(html, baseUri);
 assertEquals(document, result);
 assertFalse(errors.isEmpty());
 assertEquals(1, errors.size());
 assertTrue(errors.get(0) instanceof ParseError);
 }

 @Test
 public void testParseInputWithoutBaseUri() {
 String html = "<html><body><p>Hello World</p></body></html>";
 List<ParseError> errors = new ArrayList<>();

 Mockito.when(treeBuilderMock.parse(html, Mockito.anyString(), Mockito.any(ParseErrorList.class))).thenReturn(document);
 Mockito.when(treeBuilderMock.getErrorList()).thenReturn(errors);

 Document result = parser.parseInput(html, null);
 assertEquals(document, result);
 assertNull(errors);
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new XmlTreeBuilder());
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(treeBuilder);
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(treeBuilderMock);
 assertFalse(parser.isTrackErrors());

 Parser parserWithErrors = parser.setTrackErrors(1);
 assertTrue(parserWithErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 Parser parser = new Parser(treeBuilderMock);
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = new ArrayList<>();
 errors.add(new ParseError());
 Parser parser = new Parser(treeBuilderMock);
 Mockito.when(treeBuilderMock.getErrorList()).thenReturn(errors);
 List<ParseError> result = parser.getErrors();
 assertEquals(result, errors);
 }

 @Test
 public void testParse() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document result = Parser.parse(html, "http://example.com");
 assertEquals(Document.class, result.getClass());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = Mockito.mock(Element.class);
 Document document = Mockito.mock(Document.class);
 List<Node> result = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 assertFalse(result.isEmpty());
 assertEquals(1, result.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document document = Mockito.mock(Document.class);
 Element body = Mockito.mock(Element.class);
 List<Node> nodes = new ArrayList<>();
 nodes.add(Mockito.mock(Element.class));
 Mockito.when(treeBuilderMock.parseFragment(bodyHtml, body, "http://example.com", Mockito.any(ParseErrorList.class))).thenReturn(nodes);
 Mockito.when(document.body()).thenReturn(body);
 Document result = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals(document, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseBodyFragmentRelaxedDeprecated() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 assertTrue(parser.getClass().isAssignableFrom(HtmlParser.class));
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 assertTrue(parser.getClass().isAssignableFrom(XmlParser.class));
 }
}