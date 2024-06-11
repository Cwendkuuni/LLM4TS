import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import java.util.List;
import static org.junit.Assert.*;

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
 String html = "<html><body><h1>Test</h1></body></html>";
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals(1, doc.numberOfElements());
 assertEquals("Test", doc.select("h1").text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Test</h1><script>alert('Hello World')</script></body></html>";
 String baseUri = "http://example.com/";
 Parser parserWithErrors = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = parserWithErrors.parseInput(html, baseUri).getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected char '(' [char code = 0x28]", errors.get(0).message());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<html><body><h1>Test</h1><script>alert('Hello World')</script><script>alert('Hello Again')</script></body></html>";
 String baseUri = "http://example.com/";
 Parser parserWithErrors = new Parser(treeBuilder).setTrackErrors(2);
 List<ParseError> errors = parserWithErrors.parseInput(html, baseUri).getErrors();
 assertEquals(2, errors.size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullHtml() {
 String baseUri = "http://example.com/";
 parser.parseInput(null, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullBaseUri() {
 String html = "<html><body><h1>Test</h1></body></html>";
 parser.parseInput(html, null);
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
 assertTrue(parser.isTrackErrors());
 Parser parserWithoutErrors = new Parser(treeBuilder).setTrackErrors(0);
 assertFalse(parserWithoutErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><h1>Test</h1><script>alert('Hello World')</script></body></html>";
 String baseUri = "http://example.com/";
 List<ParseError> errors = parserWithErrors.parseInput(html, baseUri).getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected char '(' [char code = 0x28]", errors.get(0).message());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Hello World</p>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "http://example.com/";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 assertEquals(1, nodes.size());
 assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World</p>";
 String baseUri = "http://example.com/";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(doc);
 assertEquals(1, doc.numberOfElements());
 assertEquals("Hello World", doc.select("p").text());
 }
}