import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInputBasic() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, "http://example.com");
 assertNotNull(document);
 assertEquals("Test", document.title().text());
 assertEquals("Hello World", document.body().select("h1").text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><head><title>Test</title></head><body><script>alert('Hello');</script></body></html>";
 parser.setTrackErrors(1);
 document = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected '<script>', expecting '<!doctype'" +
         " html' (position: 3:11 in document)", errors.get(0).getMessage());
 assertNull(document);
 }

 @Test
 public void testParseInputTrackMultipleErrors() {
 String html = "<html><head><title>Test</title></head><body><script>alert('Hello');</script>" +
         "<div id='foo'></div><script>alert('World');</script></body></html>";
 parser.setTrackErrors(2);
 document = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(2, errors.size());
 assertTrue(errors.get(0).getMessage().contains("'Hello'"));
 assertTrue(errors.get(1).getMessage().contains("'World'"));
 assertNull(document);
 }

 @Test
 public void testParseInputEmpty() {
 document = parser.parseInput("", "http://example.com");
 assertNotNull(document);
 assertNull(document.body());
 assertNull(document.title());
 }

 @Test
 public void testParseInputNull() {
 document = parser.parseInput(null, "http://example.com");
 assertNull(document);
 }

 @Test
 public void testParseBodyFragmentBasic() {
 String bodyHtml = "<h1>Hello World</h1>";
 document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNotNull(document);
 assertEquals("Hello World", document.body().select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithErrors() {
 String bodyHtml = "<script>alert('Hello');</script>";
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(1);
 document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected '<script>', expecting '<!doctype'" +
         " html' (position: 1:1 in document)", errors.get(0).getMessage());
 assertNull(document);
 }

 @Test
 public void testParseBodyFragmentTrackMultipleErrors() {
 String bodyHtml = "<script>alert('Hello');</script>" +
         "<div id='foo'></div><script>alert('World');</script>";
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(2);
 document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(2, errors.size());
 assertTrue(errors.get(0).getMessage().contains("'Hello'"));
 assertTrue(errors.get(1).getMessage().contains("'World'"));
 assertNull(document);
 }

 @Test
 public void testParseFragmentBasic() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = Jsoup.parseBodyElements(Jsoup.connect("http://example.com").get()).get(0);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Node node = nodes.get(0);
 assertEquals("h1", node.tagName());
 assertEquals("Hello World", node.text());
 }

 @Test
 public void testParseFragmentWithErrors() {
 String fragmentHtml = "<script>alert('Hello');</script>";
 Element context = Jsoup.parseBodyElements(Jsoup.connect("http://example.com").get()).get(0);
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(1);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Unexpected '<script>', expecting '<!doctype'" +
         " html' (position: 1:1 in document)", errors.get(0).getMessage());
 assertNull(nodes);
 }

 @Test
 public void testParseFragmentTrackMultipleErrors() {
 String fragmentHtml = "<script>alert('Hello');</script>" +
         "<div id='foo'></div><script>alert('World');</script>";
 Element context = Jsoup.parseBodyElements(Jsoup.connect("http://example.com").get()).get(0);
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(2);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 assertEquals(2, errors.size());
 assertTrue(errors.get(0).getMessage().contains("'Hello'"));
 assertTrue(errors.get(1).getMessage().contains("'World'"));
 assertNull(nodes);
 }
}