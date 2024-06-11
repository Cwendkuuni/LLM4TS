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
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 // Testing the constructor 
 @Test
 public void testParserConstructor() {
 Parser parser = new Parser(treeBuilder);
 Assert.assertNotNull(parser);
 }

 // Testing parseInput method
 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing getTreeBuilder method
 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 // Testing setTreeBuilder method
 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 // Testing isTrackErrors method
 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 // Testing setTrackErrors method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsNegativeValue() {
 parser.setTrackErrors(-1);
 }

 // Testing getErrors method with no errors
 @Test
 public void testGetErrorsNoErrors() {
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(0, errors.size());
 }

 // Testing parse method with no baseUri
 @Test
 public void testParseNoBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing parse method with baseUri
 @Test
 public void testParseWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);
 String imgSrc = doc.select("img[src]").attr("src");
 Assert.assertEquals("image.jpg", imgSrc);
 }

 // Testing parseFragment method with no context
 @Test
 public void testParseFragmentNoContext() {
 String html = "<div><p>Hello World</p></div>";
 List<Node> nodes = Parser.parseFragment(html, null, baseUri);
 Assert.assertEquals(2, nodes.size());
 }

 // Testing parseFragment method with context
 @Test
 public void testParseFragmentWithContext() {
 String html = "<div><p>Hello World</p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(html, context, baseUri);
 Assert.assertEquals(1, nodes.size());
 }

 // Testing parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parseBodyFragment(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing parseBodyFragmentRelaxed method (deprecated)
 @Deprecated
 @Test
 public void testParseBodyFragmentRelaxed() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing htmlParser method
 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
 }

 // Testing xmlParser method
 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
 }
}