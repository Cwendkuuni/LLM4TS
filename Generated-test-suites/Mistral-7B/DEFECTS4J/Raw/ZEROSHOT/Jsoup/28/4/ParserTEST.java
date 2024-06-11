package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.ParseError;
import org.jsoup.ParseErrorList;
import org.jsoup.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
 private ParseErrorList errors;
 @Mock
 private Element context;

 @Before
 public void setUp() {
 parser = new Parser(treeBuilder);
 }

 @Test
 public void shouldCreateParserWithTreeBuilder() {
 Parser parser = new Parser(treeBuilder);
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void shouldParseInputWithDefaults() {
 String html = "<html><body></body></html>";
 Document document = parser.parseInput(html, null);
 assertNotNull(document);
 assertNotNull(document.body());
 assertFalse(parser.isTrackErrors());
 assertNull(parser.getErrors());
 }

 @Test
 public void shouldParseInputWithMaxErrorsSet() {
 parser.setTrackErrors(1);
 String html = "<html><body></body></html>";
 Document document = parser.parseInput(html, null);
 assertNotNull(document);
 assertNotNull(document.body());
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Error message", errors.get(0).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenInvalidMaxErrors() {
 parser.setTrackErrors(-1);
 }

 @Test
 public void shouldReturnCurrentTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 Parser parser = new Parser(expectedTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void shouldUpdateTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 Parser parser = new Parser(treeBuilder);
 Parser updatedParser = parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, updatedParser.getTreeBuilder());
 }

 @Test
 public void shouldCheckIfTrackErrorsIsEnabled() {
 Parser parser = new Parser(treeBuilder);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void shouldEnableOrDisableParseErrorTracking() {
 Parser parser = new Parser(treeBuilder);
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void shouldReturnParseErrors() {
 List<ParseError> errors = new ArrayList<>();
 errors.add(Mockito.mock(ParseError.class));
 Mockito.when(errors.get(0).getMessage()).thenReturn("Error message");
 parser.setTrackErrors(1);
 String html = "<html><body></body></html>";
 parser.parseInput(html, null);
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(1, actualErrors.size());
 assertEquals("Error message", actualErrors.get(0).getMessage());
 }

 @Test
 public void shouldParseFragmentIntoListOfNodes() {
 String fragmentHtml = "<div><p>Hello World</p></div>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 assertEquals(2, nodes.size());
 }

 @Test
 public void shouldParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, null);
 assertEquals("<html><head></head><body><p>Hello World</p></body></html>", document.toString());
 }

 @Test
 public void shouldUnescapeHtmlEntities() {
 String input = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String output = Parser.unescapeHtml(input);
 assertEquals("<p>Hello & World</p>", output);
 }

 @Test
 public void shouldParseInputWithRelaxedMode() {
 String bodyHtml = "<body><script src=\"script.js\"></script></body>";
 Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
 assertNotNull(document);
 assertFalse(document.body().child(0).nodeName().equals("script"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void shouldNotModifyListOfNodesWhenReturnedFromParseFragment() {
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 for (Node node : nodes) {
 Mockito.doReturn(node).when(node).clone();
 }
 List<Node> modifiedNodes = parser.parseFragment(fragmentHtml, context, null);
 assertEquals(nodes, modifiedNodes);
 }
}