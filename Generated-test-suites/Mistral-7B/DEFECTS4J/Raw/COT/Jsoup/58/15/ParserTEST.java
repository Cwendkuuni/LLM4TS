import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;
import java.util.ArrayList;
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
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 Document document = parser.parseInput(html, baseUri);
 assertEquals("Hello World", document.getElementsByTag("h1").text());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><body><script>alert('Hello World')</script></body></html>"; // malformed HTML with script tag
 String baseUri = "https://example.com";
 parser = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput(html, null);
 }

 @Test
 public void testGetTreeBuilder() {
 assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser = new Parser(treeBuilder).setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(3);
 assertEquals(3, parser.maxErrors);
 }

 @Test
 public void testGetErrors() {
 String malformedHtml = "<html><body> <script>alert('Hello World')</script></body></html>";
 parser = new Parser(treeBuilder).setTrackErrors(1);
 Document document = parser.parseInput(malformedHtml, baseUri);
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getMessage());
 }

 @Test
 public void testParseWithEmptyInput() {
 Document document = parser.parseInput("", baseUri);
 assertTrue(document.outputContent().isEmpty());
 }

 @Test
 public void testParseWithEmptyBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, "");
 assertEquals("Hello World", document.getElementsByTag("h1").text());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body><script>alert('Hello World')</script></body></html>";
 String baseUri = "https://example.com";
 parser = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = new ArrayList<>();
 for (int i = 0; i < Parser.DEFAULT_MAX_ERRORS + 1; i++) {
 Document document = parser.parseInput(html, baseUri);
 errors.addAll(parser.getErrors());
 parser.getErrors().clear();
 }
 assertEquals(Parser.DEFAULT_MAX_ERRORS + 1, errors.size());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src='image.jpg'/>";
 String baseUri = "https://example.com";
 Document document = parser.parseInput(html, baseUri);
 String expectedImageUrl = baseUri + "/image.jpg";
 assertEquals(expectedImageUrl, document.select("img").attr("src"));
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 Document document = Parser.parse(html, baseUri);
 assertEquals("Hello World", document.getElementsByTag("h1").text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Fragement</div>";
 Document document = Parser.parseFragment(fragmentHtml, null, baseUri).get(0).getParent();
 assertEquals("Fragment", document.getElementsByTag("div").text());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><name>John Doe</name></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 assertEquals(1, nodes.size());
 assertEquals("John Doe", nodes.get(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertEquals("Hello World", document.getElementsByTag("h1").text());
 }

 @Test
 public void testUnescapeEntities() {
 String escapedString = "&lt;Hello&gt; &amp;World&nbsp; &copy;2023";
 String unescapedString = Parser.unescapeEntities(escapedString, false);
 assertEquals("<Hello> & World\u00A0 \u00A92023", unescapedString);
 }
}