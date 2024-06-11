package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = Mockito.mock(TreeBuilder.class);
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = null;
 List<ParseError> expectedErrors = new ArrayList<>();
 ParseErrorList errors = ParseErrorList.tracking(expectedErrors.size());
 Document doc = parser.parseInput(html, baseUri);
 assertTrue(doc.body().text().equals("Hello World"));
 assertEquals(0, expectedErrors.size());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = null;
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(new ParseError("Error message", 1, 10, ParseError.PARSER_ERR));
 ParseErrorList errors = ParseErrorList.tracking(expectedErrors.size());
 Document doc = parser.parseInput(html, baseUri);
 assertEquals(1, expectedErrors.size());
 assertTrue(doc.body().text().isEmpty());
 }

 @Test
 public void testGetTreeBuilder() {
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = Mockito.mock(TreeBuilder.class);
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(new ParseError("Error message", 1, 10, ParseError.PARSER_ERR));
 ParseErrorList errors = ParseErrorList.tracking(expectedErrors.size());
 parser.setErrors(errors);
 assertEquals(expectedErrors, parser.getErrors());
 }

 @Test
 public void testSettings() {
 ParseSettings settings = Mockito.mock(ParseSettings.class);
 parser.settings(settings);
 assertEquals(settings, parser.settings());
 }
}