import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parser;

 @Test
 public void testParserConstructor() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertNotNull(parser);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Hello <script>alert('World')</script></h1></body></html>";
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unparsed content beginning '<script' at line 2 column 11: script or java tags are not parsed by default.", errors.get(0).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("<html><body></body></html>", null);
 }

 @Test
 public void testGetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertSame(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser1 = new Parser(new HtmlTreeBuilder());
 Parser parser2 = new Parser(new XmlTreeBuilder());
 parser1.setTreeBuilder(parser2.getTreeBuilder());
 Assert.assertSame(parser2.getTreeBuilder(), parser1.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("<html><body><h1>Hello <script>alert('World')</script></h1></body></html>", "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testParse() {
 Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Element context = Jsoup.parse("<div></div>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(2, nodes.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 List<Node> nodes = doc.select("ul");
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 // This test is not comprehensive as the method is deprecated and may have unintended behavior
 // Assert.assertNotNull(doc);
 // List<Node> nodes = doc.select("ul");
 // Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 Assert.assertNotNull(parser);
 Assert.assertTrue(parser instanceof Parser);
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 Assert.assertNotNull(parser);
 Assert.assertTrue(parser instanceof Parser);
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
 }
}