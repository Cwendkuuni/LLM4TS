import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

 private Parser parser;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document document = parser.parseInput(html, null);
 Assert.assertNotNull(document);
 Assert.assertEquals(1, document.numHeadElements());
 Assert.assertEquals(1, document.body().numChildElements());
 }

 @Test
 public void testParseInput_TrackErrors() {
 String html = "<html><head></head><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
 parser.setTrackErrors(1);
 Document document = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unparsed tag [script]", errors.get(0).message());
 Assert.assertNull(document);
 }

 @Test
 public void testParseInput_DefaultSettings() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document document = Parser.parse(html, null);
 Assert.assertNotNull(document);
 Assert.assertEquals(1, document.numHeadElements());
 Assert.assertEquals(1, document.body().numChildElements());
 }

 @Test
 public void testParseInput_TrackErrors_DefaultSettings() {
 String html = "<html><head></head><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
 List<ParseError> errors = Parser.parse(html, null, Parser.ParseErrorList.tracking(1)).getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unparsed tag [script]", errors.get(0).message());
 Assert.assertNull(Parser.parse(html, null, Parser.ParseErrorList.tracking(1)).getDocument());
 }

 @Test
 public void testParseInput_CustomSettings() {
 String html = "<html><head></head><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
 ParseSettings settings = new ParseSettings();
 settings.setTrimWhitespace(false);
 Document document = parser.parseInput(html, null, null, settings);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unparsed tag [script]", errors.get(0).message());
 Assert.assertNull(document);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Hello World</h1>";
 Element context = Jsoup.parse("<div></div>").body();
 Document document = parser.parseInput(context.html(), null);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("h1", nodes.get(0).tagName());
 document.body().appendChild(nodes.get(0));
 }

 @Test
 public void testParseFragment_WithErrors() {
 String fragmentHtml = "<h1>Hello World</h1><script>alert('Hello')</script>";
 Element context = Jsoup.parse("<div></div>").body();
 Document document = parser.parseInput(context.html(), null);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("h1", nodes.get(0).tagName());
 document.body().appendChild(nodes.get(0));
 parser.parseInput(fragmentHtml, null);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unparsed tag [script]", errors.get(0).message());
 Assert.assertNull(parser.getDocument());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<root><child/></root>";
 Document document = Parser.parseXmlFragment(fragmentXml, null);
 Assert.assertEquals(1, document.numChildren());
 Assert.assertTrue(document.get(0) instanceof Element);
 Assert.assertEquals("child", document.get(0).tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div><p>Hello World</p></div>";
 Document document = Parser.parseBodyFragment(bodyHtml, null);
 Assert.assertEquals(1, document.numHeadElements());
 Assert.assertEquals(1, document.numChildElements());
 Assert.assertEquals("p", document.body().get(0).tagName());
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&amp; &lt; &gt; &quot; &apos; &eacute; &nbsp;";
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertEquals("&amp; &lt; &gt; &quot; &apos; é &nbsp;", unescapedString);
 }

 @Test(expected = RuntimeException.class)
 public void testParseInput_WithException() {
 parser.parseInput("", null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInput_WithNullDocument() {
 parser.parseInput(null, null);
 }
}