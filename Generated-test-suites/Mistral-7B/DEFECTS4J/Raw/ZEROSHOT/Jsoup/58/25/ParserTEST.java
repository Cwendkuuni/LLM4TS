package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.nodes.ParseError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 @Mock
 private TreeBuilder treeBuilder;
 @Mock
 private ParseErrorList errors;
 @Mock
 private ParseSettings settings;

 @Before
 public void setUp() {
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><div>Test</div></body></html>";
 String baseUri = "http://example.com";

 when(treeBuilder.parse(html, baseUri, errors, settings)).thenReturn(new Document());

 Document document = parser.parseInput(html, baseUri);
 assertNotNull(document);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = mock(TreeBuilder.class);
 parser.setTreeBuilder(expectedTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = mock(TreeBuilder.class);
 Parser actualParser = parser.setTreeBuilder(expectedTreeBuilder);
 assertEquals(expectedTreeBuilder, actualParser.getTreeBuilder());
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
 public void testGetErrorsWhenNotTrackingErrors() {
 List<ParseError> expectedErrors = new ArrayList<>();
 parser.setErrors(expectedErrors);
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 public void testGetErrorsWhenTrackingErrors() {
 int maxErrors = 5;
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(mock(ParseError.class));
 parser.setTrackErrors(maxErrors);
 parser.setErrors(expectedErrors);
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetErrorsWhenNoTrackingErrors() {
 parser.setTrackErrors(0);
 List<ParseError> actualErrors = parser.getErrors();
 assertNotNull(actualErrors);
 actualErrors.remove(0);
 }

 @Test
 public void testSettings() {
 ParseSettings expectedSettings = mock(ParseSettings.class);
 parser.setSettings(expectedSettings);
 ParseSettings actualSettings = parser.settings();
 assertEquals(expectedSettings, actualSettings);
 }

 @Test
 public void testParse() {
 String html = "<html><body><div>Test</div></body></html>";
 String baseUri = "http://example.com";
 Document document = Parser.parse(html, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Test</div>";
 Element context = mock(Element.class);
 String baseUri = "http://example.com";
 List<Node> expectedNodes = new ArrayList<>();
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertEquals(expectedNodes, actualNodes);
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><test>Test</test></xml>";
 String baseUri = "http://example.com";
 List<Node> expectedNodes = new ArrayList<>();
 List<Node> actualNodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 assertEquals(expectedNodes, actualNodes);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><div>Test</div></body>";
 String baseUri = "http://example.com";
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;Test&gt;";
 String expectedUnescapedString = "<Test>";
 String actualUnescapedString = Parser.unescapeEntities(string, false);
 assertEquals(expectedUnescapedString, actualUnescapedString);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div>Test</div></body>";
 String baseUri = "http://example.com";
 Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 assertNotNull(document);
 }
}