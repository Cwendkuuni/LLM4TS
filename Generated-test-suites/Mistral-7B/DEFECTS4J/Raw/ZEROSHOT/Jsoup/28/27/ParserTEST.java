package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

 @Mock
 private TreeBuilder treeBuilder;
 @Mock
 private ParseErrorList errors;
 @Mock
 private Document document;
 @Mock
 private Element element;
 @Mock
 private Node node;

 private Parser parser;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com";
 Mockito.when(treeBuilder.parse(html, baseUri, errors)).thenReturn(document);
 Mockito.when(document.body()).thenReturn(element);

 Document parsedDocument = parser.parseInput(html, baseUri);
 assertEquals(document, parsedDocument);
 Mockito.verify(errors, Mockito.never()).add(Mockito.any(ParseError.class));
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput(html, null);
 }

 @Test
 public void testParseInputWithErrorsTrackingDisabled() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com";

 Mockito.when(treeBuilder.parse(html, baseUri, Mockito.mock(ParseErrorList.class))).thenReturn(document);
 Mockito.when(document.body()).thenReturn(element);

 List<ParseError> errors = new ArrayList<>();
 parser.setTrackErrors(0);
 Document parsedDocument = parser.parseInput(html, baseUri);
 assertEquals(document, parsedDocument);
 Mockito.verify(errors, Mockito.never()).add(Mockito.any(ParseError.class));
 }

 @Test
 public void testParseInputWithErrorsTrackingEnabled() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com";

 Mockito.when(treeBuilder.parse(html, baseUri, errors)).thenReturn(document);
 Mockito.when(document.body()).thenReturn(element);

 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(Mockito.mock(ParseError.class));
 Mockito.when(errors.size()).thenReturn(1);
 Mockito.when(errors.get(0)).thenReturn(expectedErrors.get(0));

 parser.setTrackErrors(1);
 Document parsedDocument = parser.parseInput(html, baseUri);
 assertEquals(document, parsedDocument);
 Mockito.verify(errors, Mockito.times(1)).add(Mockito.any(ParseError.class));
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 Mockito.when(parser.getTreeBuilder()).thenReturn(expectedTreeBuilder);
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
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZeroArgument() {
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 if (parser.getErrors() == null) {
 assertNull(parser.getErrors());
 } else {
 List<ParseError> errors = parser.getErrors();
 assertEquals(0, errors.size());
 }
 }
}