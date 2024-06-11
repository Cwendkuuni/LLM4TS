import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).errors();
 assertEquals(0, errors.size());
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertNotNull(doc.title());
 assertEquals("Hello World", doc.title().text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1<script>alert('Hello')</script>World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).errors();
 assertTrue(errors.size() > 0);
 Document doc = parser.parseInput(html, baseUri);
 assertNull(doc);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(treeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parserWithNewTreeBuilder = parser.setTreeBuilder(new HtmlTreeBuilder());
 TreeBuilder newTreeBuilder = parserWithNewTreeBuilder.getTreeBuilder();
 assertEquals(new HtmlTreeBuilder(), newTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Parser parserWithTrackErrors = parser.setTrackErrors(5);
 assertTrue(parserWithTrackErrors.isTrackErrors());
 assertEquals(5, parserWithTrackErrors.maxErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = parser.parseInput("<html><body><script>alert('Hello')</script></body></html>", "http://example.com").errors();
 Parser parserWithErrors = parser.setTrackErrors(5);
 parserWithErrors.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 List<ParseError> actualErrors = parserWithErrors.getErrors();
 assertEquals(2, actualErrors.size());
 }

 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 assertEquals(settings, treeBuilder.defaultSettings());
 }

 @Test
 public void testSettingsWithNewSettings() {
 ParseSettings newSettings = new ParseSettings.Builder().build();
 Parser parserWithNewSettings = parser.settings(newSettings);
 ParseSettings actualSettings = parserWithNewSettings.settings();
 assertEquals(newSettings, actualSettings);
 }
}