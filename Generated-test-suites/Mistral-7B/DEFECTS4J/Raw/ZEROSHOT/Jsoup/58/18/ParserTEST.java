package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.settings.ParseSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private ParseSettings settings;

 @Before
 public void setUp() {
 treeBuilder = Mockito.mock(TreeBuilder.class);
 settings = Mockito.mock(ParseSettings.class);
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://test.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 assertNotNull(errors);
 assertEquals(0, errors.size());
 Document document = parser.parseInput(html, baseUri);
 assertNotNull(document);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullHtml() {
 String baseUri = "http://test.com";
 parser.parseInput(null, baseUri);
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
 String baseUri = "http://test.com";
 List<ParseError> expectedErrors = ParseErrorList.tracking(1).getAll();
 ParseError firstError = expectedErrors.get(0);
 firstError.setLine(1);
 firstError.setColumn(6);
 firstError.setMessage("The start tag opened in the script element at line 1 and column 6 is not closed by the end tag of script");
 List<ParseError> errors = parser.setTrackErrors(1).parseInput(html, baseUri).getErrors();
 assertEquals(expectedErrors, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNegativeMaxErrors() {
 Parser parserWithNegativeMaxErrors = new Parser(treeBuilder).setTrackErrors(-1);
 parserWithNegativeMaxErrors.parseInput("", "");
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = Mockito.mock(TreeBuilder.class);
 Parser parserWithNewTreeBuilder = parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parserWithNewTreeBuilder.getTreeBuilder();
 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 Parser parserWithZeroMaxErrors = parser.setTrackErrors(0);
 assertFalse(parserWithZeroMaxErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 assertEquals(0, parser.getMaxErrors());
 parser.setTrackErrors(5);
 assertEquals(5, parser.getMaxErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> noErrors = ParseErrorList.noTracking().getAll();
 assertNotNull(noErrors);
 assertEquals(0, noErrors.size());
 List<ParseError> trackedErrors = ParseErrorList.tracking(1).getAll();
 assertNotNull(trackedErrors);
 assertEquals(1, trackedErrors.size());
 parser.parseInput("<script></script>", "");
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(trackedErrors, actualErrors);
 }

 @Test
 public void testSettings() {
 ParseSettings actualSettings = parser.settings();
 assertEquals(settings, actualSettings);
 }

 @Test
 public void testSettingsWithNewSettings() {
 ParseSettings newSettings = Mockito.mock(ParseSettings.class);
 Parser parserWithNewSettings = parser.settings(newSettings);
 ParseSettings actualSettings = parserWithNewSettings.settings();
 assertEquals(newSettings, actualSettings);
 }
}