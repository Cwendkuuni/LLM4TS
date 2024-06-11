package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 @Mock
 private TreeBuilder treeBuilder;
 @Mock
 private ParseErrorList errorList;
 @Mock
 private ParseSettings settings;

 @Before
 public void setup() {
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();
 Document document = parser.parseInput(html, baseUri);
 assertNotNull(document);
 assertEquals("<html><body><h1>Hello</h1></body></html>", document.html());
 assertNull(parser.getErrors());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello</h1><script>alert('Hello')</script></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(Mockito.mock(ParseError.class));
 parser.setTrackErrors(1);
 ParseErrorList errorListSpy = Mockito.spy(ParseErrorList.class);
 Mockito.when(errorListSpy.trackError(Mockito.any(ParseError.class))).then(i -> i.getArgument(0));
 Mockito.when(errorListSpy.getErrors()).thenReturn(expectedErrors);
 Mockito.when(treeBuilder.parse(Mockito.anyString(), Mockito.anyString(), Mockito.eq(errorListSpy), Mockito.any(ParseSettings.class)))
 .thenReturn(Mockito.mock(Document.class));
 Document document = parser.parseInput(html, baseUri);
 assertEquals(expectedErrors.size(), parser.getErrors().size());
 for (ParseError error : parser.getErrors()) {
 assertEquals(expectedErrors.get(0), error);
 }
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = Mockito.mock(TreeBuilder.class);
 Mockito.when(treeBuilder.getDefaultSettings()).thenReturn(settings);
 Mockito.when(treeBuilder.getDefaultSettings().getTrimWhitespace()).thenReturn(true);
 assertEquals(expectedTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = Mockito.mock(TreeBuilder.class);
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxErrorsGreaterThanZero() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsWithMaxErrorsLessThanZero() {
 parser.setTrackErrors(-1);
 }

 @Test
 public void testGetErrors() {
 assertNull(parser.getErrors());
 ParseErrorList errorListSpy = Mockito.spy(ParseErrorList.class);
 Mockito.when(errorListSpy.getErrors()).thenReturn(new ArrayList<>());
 parser.setTreeBuilder(errorListSpy);
 parser.parseInput("<html></html>", "http://example.com");
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(Mockito.mock(ParseError.class));
 Mockito.when(errorListSpy.getErrors()).thenReturn(expectedErrors);
 assertEquals(expectedErrors, parser.getErrors());
 }

 @Test
 public void testSettings() {
 ParseSettings settingsSpy = Mockito.spy(ParseSettings.class);
 Mockito.when(settings.getTrimWhitespace()).thenReturn(true);
 assertEquals(settingsSpy, parser.settings());
 }

 @Test
 public void testParseStatic() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com";
 Document document = Parser.parse(html, baseUri);
 assertNotNull(document);
 assertEquals("<html><body><h1>Hello</h1></body></html>", document.html());
 assertNull(Parser.getErrors());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Hello</p></div>";
 Element context = Mockito.mock(Element.class);
 String baseUri = "http://example.com";
 List<Node> nodeList = new ArrayList<>();
 nodeList.add(Mockito.mock(Element.class));
 Mockito.when(treeBuilder.parseFragment(Mockito.anyString(), Mockito.eq(context), Mockito.anyString(), Mockito.eq(errorList), Mockito.any(ParseSettings.class))).thenReturn(nodeList);
 List<Node> expectedNodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertEquals(nodeList, expectedNodes);
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><tag>Hello</tag></xml>";
 String baseUri = "http://example.com";
 List<Node> nodeList = new ArrayList<>();
 nodeList.add(Mockito.mock(Element.class));
 Mockito.when(treeBuilder.parseFragment(Mockito.anyString(), Mockito.any(String.class), Mockito.eq(errorList), Mockito.any(ParseSettings.class))).thenReturn(nodeList);
 List<Node> expectedNodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 assertEquals(nodeList, expectedNodes);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello</p></body>";
 String baseUri = "http://example.com";
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertEquals("<html><head></head><body><p>Hello</p></body></html>", document.html());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&lt;Hello&gt; &amp; &apos; &quot; &amp;#123; &amp;#x61; &amp;#x43; &amp;#x61; &amp;#x6D;";
 String output = Parser.unescapeEntities(input, false);
 assertEquals("<Hello>& & ' \" {a} {c} {a} {m}", output);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><p>Hello</p></body>";
 String baseUri = "http://example.com";
 Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 assertEquals("<html><head></head><body><p>Hello</p></body></html>", document.html());
 }
}