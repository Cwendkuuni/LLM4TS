import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = Mockito.spy(new HtmlTreeBuilder());
 parser = new Parser(treeBuilder);
 }

 // Test for Parser constructor
 @Test
 public void testParserConstructor() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 assertNotNull(parser);
 }

 // Test for parseInput method
 @Test
 public void testParseInputWithValidHtml() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, null);
 assertNotNull(document);
 assertSame(html, document.html());
 }

 // Test for parseInput method with error tracking
 @Test(expected = ParseError.class)
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
 parser.setTrackErrors(1);
 Document document = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertNotNull(document);
 assertEquals(1, errors.size());
 assertTrue(errors.get(0).getLine() > 0);
 }

 // Test for parseInput method with no error tracking
 @Test
 public void testParseInputWithNoErrorTracking() {
 String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
 parser.setTrackErrors(0);
 Document document = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertNotNull(document);
 assertNull(errors);
 }

 // Test for getTreeBuilder method
 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 // Test for setTreeBuilder method
 @Test
 public void testSetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(expectedTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 // Test for isTrackErrors method
 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 // Test for setTrackErrors method
 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertEquals(1, parser.getMaxErrors());
 parser.setTrackErrors(5);
 assertEquals(5, parser.getMaxErrors());
 }

 // Test for getErrors method
 @Test
 public void testGetErrors() {
 String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
 parser.setTrackErrors(1);
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(new ParseError("", 1, "Tag 'script' not allowed in prolog."));
 List<ParseError> actualErrors = parser.parseInput(html, null).getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 // Test for parse method
 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document document = Parser.parse(html, null);
 assertNotNull(document);
 assertSame(html, document.html());
 }

 // Test for parseFragment method
 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Hello World</p></div>";
 List<Node> expectedNodes = new ArrayList<>();
 expectedNodes.add(Jsoup.parse("<p>Hello World</p>").body());
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, null, null).get();
 assertEquals(expectedNodes, actualNodes);
 }

 // Test for parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, null);
 assertNotNull(document);
 assertNotNull(document.body());
 assertEquals(bodyHtml, document.outerHtml());
 }

 // Test for parseBodyFragmentRelaxed method
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
 assertNotNull(document);
 assertNotNull(document.body());
 assertEquals(bodyHtml, document.outerHtml());
 }

 // Test for htmlParser method
 @Test
 public void testHtmlParser() {
 Parser htmlParser = Parser.htmlParser();
 assertNotNull(htmlParser);
 assertSame(new HtmlTreeBuilder(), htmlParser.getTreeBuilder());
 }

 // Test for xmlParser method
 @Test
 public void testXmlParser() {
 Parser xmlParser = Parser.xmlParser();
 assertNotNull(xmlParser);
 assertSame(new XmlTreeBuilder(), xmlParser.getTreeBuilder());
 }
}