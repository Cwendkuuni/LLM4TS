import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import java.util.List;

public class ParserTest {

 private Parser parserHtml;
 private Parser parserXml;

 @Test
 public void testParserConstructor() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Assertions.assertNotNull(parser);
 }

 @Test
 public void testParseInputHtml() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Parser parser = Parser.htmlParser();
 Document doc = parser.parseInput(html, "https://example.com");
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("Test", doc.select("h1").text());
 }

 @Test
 public void testParseInputHtmlWithErrors() {
 String html = "<html><body><h1>Test<script>alert('Hello')</script></h1></body></html>";
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertEquals(1, errors.size());
 Assertions.assertSame(errors.get(0).getLine(), 2);
 }

 @Test
 public void testParseInputHtmlWithMaxErrorsReached() {
 String html = "<html><body><h1>Test</h1><h1>Test</h1><h1>Test</h1></body></html>";
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(2);
 Document doc = parser.parseInput(html, "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputXml() {
 String xml = "<xml><item>Test</item></xml>";
 Parser parser = Parser.xmlParser();
 Document doc = parser.parseInput(xml, "https://example.com");
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("Test", doc.select("item").first().text());
 }

 @Test
 public void testParseInputXmlWithErrors() {
 String xml = "<xml><item>Test<script>alert('Hello')</script></item></xml>";
 Parser parser = Parser.xmlParser();
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(xml, "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertEquals(1, errors.size());
 Assertions.assertSame(errors.get(0).getLine(), 2);
 }

 @Test
 public void testParseInputXmlWithMaxErrorsReached() {
 String xml = "<xml><item>Test</item><item>Test</item><item>Test</item></xml>";
 Parser parser = Parser.xmlParser();
 parser.setTrackErrors(2);
 Document doc = parser.parseInput(xml, "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertEquals(2, errors.size());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Test</h1>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Parser parser = Parser.htmlParser();
 List<Node> nodes = parser.parseFragment(fragmentHtml, context, "https://example.com");
 Node node = nodes.get(0);
 Assertions.assertInstanceOf(Element.class, node);
 Assertions.assertEquals("Test", node.text());
 }

 @Test
 public void testParseInputHtmlWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Test\">";
 Parser parser = Parser.htmlParser();
 Document doc = parser.parseInput(html, "https://example.com/test");
 String imgSrc = doc.select("img").attr("src");
 Assertions.assertEquals("https://example.com/test/image.jpg", imgSrc);
 }

 @Test
 public void testParseInputHtmlWithContext() {
 String html = "<div class=\"test\">Test</div>";
 Element context = Jsoup.parse("<html><body><div class=\"test\"></div></body></html>").selectFirst(".test");
 Parser parser = Parser.htmlParser();
 List<Node> nodes = parser.parseFragment(html, context, "https://example.com");
 Node node = nodes.get(0);
 Assertions.assertEquals("Test", node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Test</h1>";
 Parser parser = Parser.htmlParser();
 Document doc = parser.parseBodyFragment(bodyHtml, "https://example.com");
 Assertions.assertEquals("Test", doc.select("h1").text());
 }

 @Test
 public void testParseBodyFragmentWithErrors() {
 String bodyHtml = "<h1>Test</h1><script>alert('Hello')</script>";
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(1);
 Document doc = parser.parseBodyFragment(bodyHtml, "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertEquals(1, errors.size());
 Assertions.assertSame(errors.get(0).getLine(), 2);
 }

 @Test
 public void testParseBodyFragmentWithMaxErrorsReached() {
 String bodyHtml = "<h1>Test</h1><h1>Test</h1><h1>Test</h1>";
 Parser parser = Parser.htmlParser();
 parser.setTrackErrors(2);
 Document doc = parser.parseBodyFragment(bodyHtml, "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputHtmlWithDeprecatedParseBodyFragmentRelaxed() {
 String bodyHtml = "<h1>Test</h1>";
 Parser parser = Parser.htmlParser();
 Document doc = parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");
 Assertions.assertEquals("Test", doc.select("h1").text());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assertions.assertSame(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetMaxErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setMaxErrors(5);
 Assertions.assertEquals(5, parser.getMaxErrors());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setMaxErrors(3);
 Assertions.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrorsWhenTrackErrorsIsFalse() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 Document doc = parser.parseInput("<html><body></body></html>", "https://example.com");
 Assertions.assertNull(parser.getErrors());
 }

 @Test
 public void testGetErrorsWhenTrackErrorsIsTrue() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setMaxErrors(1);
 Document doc = parser.parseInput("<html><body><h1>Test</h1></body></html>", "https://example.com");
 List<ParseError> errors = parser.getErrors();
 Assertions.assertNotNull(errors);
 Assertions.assertEquals(1, errors.size());
 }
}