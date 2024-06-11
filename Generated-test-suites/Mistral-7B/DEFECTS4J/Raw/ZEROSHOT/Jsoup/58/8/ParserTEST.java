package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.ParseErrorList;
import org.jsoup.settings.ParseSettings;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document document;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><div>Hello World</div></body></html>";
 String baseUri = "http://example.com/";
 document = parser.parseInput(html, baseUri);
 assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><div>Hello <script>World</script></div></body></html>";
 String baseUri = "http://example.com/";
 parser.setTrackErrors(1);
 List<ParseError> errors = parser.getErrors();
 document = parser.parseInput(html, baseUri);
 assertEquals("Hello ", document.body().text());
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Tag 'script' not allowed in content", error.getMessage());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
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
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><body><div>Hello World</div></body></html>";
 String baseUri = "http://example.com/";
 Document doc = Parser.parse(html, baseUri);
 assertEquals("Hello World", doc.body().text());

 Element context = new Element("html");
 List<Node> nodes = Parser.parseFragment(html, context, baseUri);
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertEquals("DIV", node.tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "http://example.com/";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testUnescapeEntities() {
 String escapedHtml = "&lt;html&gt;&amp;lt;body&gt;&amp;lt;div&gt;&amp;lt;strong&gt;Hello &amp;World&amp;lt;/strong&gt;&amp;lt;/div&gt;&amp;lt;/body&gt;&amp;lt;/html&gt;";
 String unescapedHtml = Parser.unescapeEntities(escapedHtml, false);
 assertEquals("<html><body><div><strong>Hello World</strong></div></body></html>", unescapedHtml);
 }
}