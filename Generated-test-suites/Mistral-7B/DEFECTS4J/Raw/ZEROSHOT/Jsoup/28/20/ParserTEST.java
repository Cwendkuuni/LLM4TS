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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 @Mock
 private TreeBuilder treeBuilder;

 @InjectMocks
 private Parser parser;

 @Before
 public void setup() {
 parser.setTreeBuilder(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";

 Document document = parser.parseInput(html, baseUri);
 assertNotNull(document);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = mock(TreeBuilder.class);
 parser.setTreeBuilder(newTreeBuilder);
 verify(treeBuilder).release();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrorsEmpty() {
 List<ParseError> expectedErrors = new ArrayList<>();
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 public void testGetErrorsWithErrors() {
 ParseError error1 = mock(ParseError.class);
 ParseError error2 = mock(ParseError.class);
 List<ParseError> errors = new ArrayList<>();
 errors.add(error1);
 errors.add(error2);

 parser.setTrackErrors(2);
 ParseErrorList parseErrorList = mock(ParseErrorList.class);
 when(parseErrorList.add(anyString(), anyInt(), anyInt())).thenReturn(parseErrorList);
 when(parseErrorList.size()).thenReturn(2);
 when(parseErrorList.get(0)).thenReturn(error1);
 when(parseErrorList.get(1)).thenReturn(error2);

 parser.parseInput("<html></html>", "http://example.com");
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(errors, actualErrors);
 }

 @Test
 public void testParseInputWithErrors() {
 ParseError error = mock(ParseError.class);
 ParseErrorList parseErrorList = mock(ParseErrorList.class);
 when(parseErrorList.size()).thenReturn(1);
 when(parseErrorList.get(0)).thenReturn(error);

 parser.setTrackErrors(1);
 when(treeBuilder.parse(anyString(), anyString(), any(ParseErrorList.class))).thenReturn(mock(Document.class));

 parser.parseInput("<html></html>", "http://example.com");
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(parseErrorList, actualErrors);
 }

 @Test
 public void testParse() {
 Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 assertNotNull(document);
 assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = mock(Element.class);
 String baseUri = "http://example.com";

 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertNotNull(nodes);
 assertTrue(nodes.size() > 0);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "http://example.com";

 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(document);
 assertNotNull(document.body());
 assertEquals("<div>Hello World</div>", document.body().html());
 }
}