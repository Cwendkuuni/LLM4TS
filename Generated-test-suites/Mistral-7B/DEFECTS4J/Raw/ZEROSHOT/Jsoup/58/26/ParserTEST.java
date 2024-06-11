package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.tree.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document document;
 private Element element;
 private String htmlInput;
 private String baseUri;

 @Before
 public void setup() {
 treeBuilder = Mockito.mock(TreeBuilder.class);
 parser = new Parser(treeBuilder);
 htmlInput = "<html><body><p>Hello</p></body></html>";
 baseUri = "http://example.com";
 }

 @Test
 public void testNewParserWithTreeBuilder() {
 assertNotNull(parser);
 assertSame(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testParseInput() {
 document = parser.parseInput(htmlInput, baseUri);
 assertNotNull(document);
 assertEquals(htmlInput, document.html());
 assertSame(treeBuilder, document.treeBuilder());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullHtmlInput() {
 parser.parseInput(null, baseUri);
 }

 @Test
 public void testParseInputEmptyHtmlInput() {
 document = parser.parseInput("", baseUri);
 assertEquals("", document.html());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = new ArrayList<>();
 errors.add(new ParseError("Test error", 1, 1));
 parser.setTreeBuilder(Mockito.mock(TreeBuilder.class));
 parser.setTrackErrors(1);
 parser.parseInput(htmlInput, baseUri);
 List<ParseError> parsedErrors = parser.getErrors();
 assertSame(errors, parsedErrors);
 }

 @Test
 public void testIsTrackErrors() {
 assertEquals(false, parser.isTrackErrors());
 parser.setTrackErrors(5);
 assertEquals(true, parser.isTrackErrors());
 }

 @Test
 public void testSetAndGetTreeBuilder() {
 TreeBuilder newTreeBuilder = Mockito.mock(TreeBuilder.class);
 parser.setTreeBuilder(newTreeBuilder);
 assertSame(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testParse() {
 document = Parser.parse(htmlInput, baseUri);
 assertEquals(htmlInput, document.html());
 }

 @Test
 public void testParseFragment() {
 List<Node> parsedNodes = Parser.parseFragment("<p>Test</p>", null, baseUri);
 assertNotNull(parsedNodes);
 assertEquals(1, parsedNodes.size());
 assertSame(Element.class, parsedNodes.get(0).getClass());
 }

 @Test
 public void testParseXmlFragment() {
 List<Node> parsedNodes = Parser.parseXmlFragment("<p xmlns:test='http://test'>Test</p>", baseUri);
 assertNotNull(parsedNodes);
 assertEquals(1, parsedNodes.size());
 assertSame(Element.class, parsedNodes.get(0).getClass());
 }

 @Test
 public void testParseBodyFragment() {
 document = Parser.parseBodyFragment("<body><p>Test</p></body>", baseUri);
 assertNotNull(document);
 assertNotNull(document.body());
 assertEquals("<body><p>Test</p></body>", document.html());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&lt;p&gt;Test&amp;amp;&apos; &lt;Special&gt; &amp;#35;";
 String output = Parser.unescapeEntities(input, false);
 assertEquals("<p>Test&amp; &lt;Special&gt; &#35;", output);
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 document = Parser.parseBodyFragmentRelaxed(htmlInput, baseUri);
 assertNotNull(document);
 }
}