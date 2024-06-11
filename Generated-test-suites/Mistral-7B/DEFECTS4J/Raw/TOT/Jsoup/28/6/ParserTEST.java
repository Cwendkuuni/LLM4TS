import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
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
 Document doc = parser.parseInput(html, "http://example.com");
 Element h1 = doc.getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><script>alert('Hello')</script></body></html>";
 Parser parserWithErrors = new Parser(treeBuilder).setTrackErrors(1);
 List<ParseError> errors = parserWithErrors.getErrors();
 Document doc = parserWithErrors.parseInput(html, "http://example.com");
 assertEquals(1, errors.size());
 assertEquals("Unexpected <script> tag", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body>"; // malformed HTML
 Parser parserWithErrors = new Parser(treeBuilder).setTrackErrors(2);
 List<ParseError> errors = parserWithErrors.getErrors();
 parserWithErrors.parseInput(html, "http://example.com");
 assertEquals(2, errors.size());
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element h1 = doc.getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticFunctionsWithNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticFunctionsWithNullBaseUri() {
 Parser.parse("<html><body></body></html>", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Hello</p>";
 Element context = new Element("div");
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Node node = nodes.get(0);
 assertEquals("Hello", node.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullFragmentHtml() {
 Parser.parseFragment(null, new Element("div"), "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullContext() {
 Parser.parseFragment("<p>Hello</p>", null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 Parser.parseFragment("<p>Hello</p>", new Element("div"), null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test
 public void testParseBodyFragmentWithNullBaseUri() {
 Parser.parseBodyFragment("<p>Hello</p>", null);
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String htmlEscaped = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String unescaped = Parser.unescapeHtmlEntities(htmlEscaped);
 assertEquals("<p>Hello & World</p>", unescaped);
 }

 @Test
 public void testParseInputWithDefaults() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, null);
 Element h1 = doc.getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 }
}