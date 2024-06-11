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
 private TreeBuilder treeBuilder;
 private ParseErrorList errors;

 @Before
 public void setup() {
 treeBuilder = Mockito.mock(TreeBuilder.class);
 parser = new Parser(treeBuilder);
 errors = ParseErrorList.noTracking();
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 maxErrors = 1;
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 errors = parser.getErrors();
 assertEquals(0, errors.size());
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
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
 public void testParseInputWithErrors() {
 maxErrors = 5;
 String html = "<html><head></head><body><h1>Hello<script>World</script></h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unescaped script in body", errors.get(0).getMessage());
 assertEquals("<html><head></head><body><h1>Hello</h1></body></html>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
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
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(Mockito.mock(ParseError.class));
 parser.setErrors(expectedErrors);
 List<ParseError> actualErrors = parser.getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse("<html><head></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = Mockito.mock(Element.class);
 Document doc = Parser.parseFragment(fragmentHtml, context, "http://example.com").get(0).parent();
 assertEquals("<h1>Hello World</h1>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 Document doc = Parser.parseBodyFragmentRelaxed("<h1>Hello World</h1>", "http://example.com");
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.title());
 assertNull(doc.titleText());
 }
}