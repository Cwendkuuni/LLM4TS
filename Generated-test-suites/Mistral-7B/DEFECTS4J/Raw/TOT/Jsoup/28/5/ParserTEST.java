import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
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
 public void testParseInputWithValidHtml() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 Element h1 = (Element) doc.body().child(0);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseInputWithEmptyString() {
 Document doc = parser.parseInput("", "http://example.com");
 assertNull(doc);
 }

 @Test
 public void testParseInputWithNullHtml() {
 try {
 parser.parseInput(null, "http://example.com");
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 Document doc = parser.parseInput(html, "http://example.com");
 Element img = (Element) doc.selectFirst("img");
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsAndGetErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 parser.setTrackErrors(2);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputWithMultipleErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
 parser.setTrackErrors(2);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(2, errors.size());
 assertInstanceOf(ParseError.class, errors.get(0));
 assertInstanceOf(ParseError.class, errors.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNegativeMaxErrors() {
 parser.setTrackErrors(-1);
 String html = "<html><body></body></html>";
 parser.parseInput(html, "http://example.com");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element p = (Element) doc.body().child(0);
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Hello World</p>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Node node = nodes.get(0);
 assertEquals(Element.class, node.getClass());
 assertEquals("Hello World", node.text());
 }

 @Test
 public void testParseInputWithHtmlAndScripts() {
 String html = "<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
 parser.setTrackErrors(2);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertInstanceOf(ParseError.class, errors.get(0));
 }
}