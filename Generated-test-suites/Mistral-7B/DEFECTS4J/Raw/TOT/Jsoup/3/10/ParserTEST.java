import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setUp() {
 doc = Document.newDocument();
 parser = new Parser("");
 parser.setBaseUri("http://example.com/");
 }

 @Test
 public void testParseEmptyHtml() {
 String html = "<html/>";
 Document result = Parser.parse(html, parser.getBaseUri());
 assertEquals(doc, result);
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 Document result = Parser.parse(html, parser.getBaseUri());
 assertNotNull(result);
 assertEquals("Hello", result.select("h1").text());
 }

 @Test
 public void testParseHtmlWithScriptAndStyle() {
 String html = "<html><head><title>Test</title>" +
 "<script src=\"script.js\"></script>" +
 "<style type=\"text/css\">.red { color: red; }</style></head>" +
 "<body><div class=\"red\">Test</div></body></html>";
 Document result = Parser.parse(html, parser.getBaseUri());
 assertNotNull(result);
 assertEquals("Test", result.select("div.red").text());
 }

 @Test
 public void testParseHtmlWithComments() {
 String html = "<html><!-- This is a comment --><head><title>Test</title></head>" +
 "<body><!-- Another comment --><h1>Hello</h1></body></html>";
 Document result = Parser.parse(html, parser.getBaseUri());
 assertNotNull(result);
 assertEquals("Hello", result.select("h1").text());
 }

 @Test
 public void testParseHtmlWithXmlDeclaration() {
 String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\"><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 Document result = Parser.parse(html, parser.getBaseUri());
 assertNotNull(result);
 assertEquals("Hello", result.select("h1").text());
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 String html = "<html><head><base href=\"/test/\"><title>Test</title></head><body><h1>Hello</h1></body></html>";
 Document result = Parser.parse(html, parser.getBaseUri());
 assertEquals("http://example.com/test/", result.baseUri());
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<h1>Hello</h1>";
 Document result = Parser.parseBodyFragment(html, parser.getBaseUri());
 assertNotNull(result);
 assertEquals("Hello", result.select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, parser.getBaseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyBaseUri() {
 Parser.parse("", null);
 }
}