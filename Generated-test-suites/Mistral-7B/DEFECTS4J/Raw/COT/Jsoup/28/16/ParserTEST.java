import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parserHtml;
 private Parser parserXml;

 @Before
 public void setUp() {
 parserHtml = new Parser(new HtmlTreeBuilder());
 parserXml = new Parser(new XmlTreeBuilder());
 }

 // Test for parseInput method of Parser class
 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com/";
 Document docHtml = parserHtml.parseInput(html, baseUri);
 Document docXml = parserXml.parseInput(html, baseUri);

 assertEquals("HTML document", docHtml.tagName(), "html");
 assertEquals("XML document", docXml.tagName(), "Document");

 // Test for getTreeBuilder method of Parser class
 TreeBuilder treeBuilder = parserHtml.getTreeBuilder();
 assertEquals("HtmlTreeBuilder", treeBuilder.getClass().getName(), "HtmlTreeBuilder");
 }

 // Test for parse method of Parser class
 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com/");
 assertEquals("HTML document", doc.tagName(), "html");
 }

 // Test for parseFragment method of Parser class with valid input
 @Test
 public void testParseFragmentValidInput() {
 String fragmentHtml = "<p><strong>Bold text</strong></p>";
 Document doc = Jsoup.parseBodyFragment(fragmentHtml, "http://example.com/");
 Element p = doc.body();
 Element strong = p.getChild(0);
 assertEquals("p", p.tagName(), "p");
 assertEquals("strong", strong.tagName(), "strong");
 }

 // Test for parseFragment method of Parser class with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentEmptyInput() {
 String fragmentHtml = "";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com/");
 }

 // Test for parseFragment method of Parser class with invalid input
 @Test(expected = org.jsoup.parser.Parser.MalformedAttributeException.class)
 public void testParseFragmentInvalidInput() {
 String fragmentHtml = "<div <p>foo='bar'</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com/");
 }

 // Test for parseBodyFragment method of Parser class with valid input
 @Test
 public void testParseBodyFragmentValidInput() {
 String bodyHtml = "<body><p><strong>Bold text</strong></p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com/");
 Element p = doc.body().getChild(0);
 Element strong = p.getChild(0);
 assertEquals("body", doc.body().tagName(), "body");
 assertEquals("p", p.tagName(), "p");
 assertEquals("strong", strong.tagName(), "strong");
 }

 // Test for parseBodyFragment method of Parser class with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentEmptyInput() {
 String bodyHtml = "";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com/");
 }

 // Test for parseBodyFragment method of Parser class with invalid input
 @Test(expected = org.jsoup.parser.Parser.MalformedAttributeException.class)
 public void testParseBodyFragmentInvalidInput() {
 String bodyHtml = "<body><p <strong>foo='bar'</strong></p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com/");
 }

 // Test for unescapeHtml method of Parser class
 @Test
 public void testUnescapeHtml() {
 String htmlEscaped = "&lt;p&gt;<strong>&amp;lt;em&gt;This is some &lt;strong&gt;un&amp;amp;amp;escaped&lt;/em&gt;&lt;/strong&gt;&lt;/p&gt;";
 String htmlUnescaped = parserHtml.unescapeHtml(htmlEscaped);
 String expected = "<p><em>This is some <strong>unescaped</strong></em></p>";
 assertEquals(expected, htmlUnescaped);
 }

 // Test for isTrackErrors method of Parser class
 @Test
 public void testIsTrackErrors() {
 assertTrue(parserHtml.isTrackErrors());
 int maxErrors = parserHtml.getMaxErrors();
 assertEquals(0, maxErrors);
 }

 // Test for setTrackErrors method of Parser class
 @Test
 public void testSetTrackErrors() {
 parserHtml.setTrackErrors(5);
 int maxErrors = parserHtml.getMaxErrors();
 assertEquals(5, maxErrors);
 }

 // Test for getErrors method of Parser class
 @Test
 public void testGetErrors() {
 String html = "<html><body><p>foo<bar></body></html>";
 List<ParseError> errors = parserHtml.parseInput(html, "http://example.com/").errors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Tag 'bar' not closed", error.toString());
 }
}