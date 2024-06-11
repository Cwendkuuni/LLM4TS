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
 private Document document;

 @Test
 public void testParserConstructor() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertNotNull(parser);
 }

 @Test
 public void testGetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertSame(treeBuilder, parser.treeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertSame(newTreeBuilder, parser.treeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParseInputBasic() {
 parser = new Parser(new HtmlTreeBuilder());
 String html = "<html><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, null);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 public void testParseInputException() {
 parser = new Parser(new HtmlTreeBuilder());
 String malformedHtml = "<html<body>";
 try {
 parser.parseInput(malformedHtml, null);
 Assert.fail("Expected ParseError");
 } catch (ParseError e) {
 // Expected
 }
 }

 @Test
 public void testParseInputMaxErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(3);
 String malformedHtml = "<html<body>";
 List<ParseError> errors = parser.getErrors();
 parser.parseInput(malformedHtml, null);
 Assert.assertEquals(3, errors.size());
 }

 @Test
 public void testParseInputMaxErrorsWithErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(3);
 String malformedHtml = "<html<body><script>throw new Error('Script error')</script>";
 List<ParseError> errors = parser.getErrors();
 document = parser.parseInput(malformedHtml, null);
 Assert.assertEquals(3, errors.size());
 Assert.assertNull(document);
 }

 @Test
 public void testParseStatic() {
 document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", null);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 public void testParseFragmentBasic() {
 parser = new Parser(new HtmlTreeBuilder());
 Element context = Jsoup.parse("<div id=\"container\"></div>").body();
 String fragmentHtml = "<p>Hello World</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 Element p = (Element) nodes.get(0);
 context.appendChild(p);
 Assert.assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseFragmentException() {
 parser = new Parser(new HtmlTreeBuilder());
 Element context = Jsoup.parse("<div id=\"container\"></div>").body();
 String fragmentHtml = "<html<body>";
 try {
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 Assert.fail("Expected ParseError");
 } catch (ParseError e) {
 // Expected
 }
 }

 @Test
 public void testParseBodyFragment() {
 document = Parser.parseBodyFragment("<p>Hello World</p>", null);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 document = Parser.parseBodyFragment("<base href=\"/test/\">\n<p>Hello World</p>", "/test/index.html");
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseBodyFragmentWithNoBody() {
 document = Parser.parseBodyFragment("<html></html>", null);
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String htmlString = "&lt;p&gt;<b>Hello &amp; World</b>&gt;";
 String unescapedString = Parser.unescapeHtmlEntities(htmlString);
 Assert.assertEquals("<p><b>Hello & World</b></p>", unescapedString);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 document = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", null);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testHtmlParser() {
 parser = Parser.htmlParser();
 document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null);
 Assert.assertNotNull(document);
 Assert.assertEquals("Hello World", document.select("h1").text());
 }

 @Test
 public void testXmlParser() {
 parser = Parser.xmlParser();
 document = parser.parseInput("<root><name>John Doe</name></root>", null);
 Assert.assertNotNull(document);
 Assert.assertEquals("John Doe", document.select("name").text());
 }

}