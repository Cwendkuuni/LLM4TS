import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<script>alert('Hello World');</script>";
 String baseUri = "https://example.com";
 parser.setTrackErrors(1);
 Document document = parser.parseInput(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertNotNull(errors.get(0).getLine());
 Assert.assertEquals("Malformed HTML", errors.get(0).getMessage());
 Assert.assertNull(document);
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<script>alert('Hello World');</script>";
 String baseUri = "https://example.com";
 parser.setTrackErrors(1);
 Document document1 = parser.parseInput(html, baseUri);
 List<ParseError> errors1 = parser.getErrors();
 Assert.assertEquals(1, errors1.size());

 parser.setTreeBuilder(new HtmlTreeBuilder()); // create a new TreeBuilder to test parseInput with a new instance
 Document document2 = parser.parseInput(html, baseUri);
 List<ParseError> errors2 = parser.getErrors();
 Assert.assertEquals(1, errors2.size());

 // test that getting errors after maxErrors is reached returns an empty list
 List<ParseError> errors3 = parser.getErrors();
 Assert.assertEquals(0, errors3.size());
 }

 @Test
 public void testParseInputWithNullHtml() {
 String baseUri = "https://example.com";
 parser.setTreeBuilder(new HtmlTreeBuilder());
 Document document = parser.parseInput(null, baseUri);
 Assert.assertNull(document);
 }

 @Test
 public void testParseInputWithEmptyHtml() {
 String baseUri = "https://example.com";
 Document document = parser.parseInput("", baseUri);
 Assert.assertNull(document);
 }

 @Test
 public void testParseInputWithNullBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 parser.setTreeBuilder(new HtmlTreeBuilder());
 Document document = parser.parseInput(html, null);
 Assert.assertNull(document);
 }

 @Test
 public void testParseInputWithInvalidBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 parser.setTreeBuilder(new HtmlTreeBuilder());
 Document document = parser.parseInput(html, "invalid_base_uri");
 Assert.assertNull(document);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 String baseUri = "https://example.com";
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithErrors() {
 String bodyHtml = "<script>alert('Hello World');</script>";
 String baseUri = "https://example.com";
 parser.setTrackErrors(1);
 Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertNotNull(errors.get(0).getLine());
 Assert.assertEquals("Malformed HTML", errors.get(0).getMessage());
 Assert.assertNull(document);
 }

 @Test
 public void testParseXmlFragment() {
String xml = "<xml><tag>Hello World</tag></xml>";
String baseUri = "https://example.com";
List<Node> nodes = Parser.parseXmlFragment(xml, baseUri);
Assert.assertNotNull(nodes);
Assert.assertEquals(1, nodes.size());
Assert.assertTrue(nodes.get(0) instanceof Element);
 }

 @Test
 public void testUnescapeEntities() {
 String html = "&lt;h1&gt;Hello World&amp;amp;&#x26;#10&nbsp;&#xA0;&#x2013;&#x2014;";
 String unescapedString = Parser.unescapeEntities(html, false);
 Assert.assertEquals("<h1>Hello World&amp; &nbsp;&#160;&#8201;&#8203;</h1>", unescapedString);
 }

 @Test
 public void testParseInputWithParseSettings() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 ParseSettings settings = ParseSettings.parser().tagTextParser(new SimpleTagTextParser());
 Document document = parser.parseInput(html, baseUri, settings);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 public void testParseInputWithParseErrorList() {
 String html = "<script>alert('Hello World');</script>";
 String baseUri = "https://example.com";
 Parser parserWithTrackingErrors = new Parser(treeBuilder);
 ParseErrorList errors = parserWithTrackingErrors.parseInput(html, baseUri, ParseErrorList.tracking(2));
 Assert.assertEquals(2, errors.size());
 }
}