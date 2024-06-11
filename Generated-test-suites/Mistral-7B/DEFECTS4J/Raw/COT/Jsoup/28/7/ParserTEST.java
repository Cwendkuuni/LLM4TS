import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Test</h1></body></html>";
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.title());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><h1>Test</h1><script>alert('XSS');</script></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://example.com/");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unsafe 'alert' in <script>", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><body><h1>Test</h1><script>alert('XSS');</script></body></html>";
 parser.setTrackErrors(1);
 Document doc1 = parser.parseInput(html, "http://example.com/");
 Document doc2 = parser.parseInput(html, "http://example.com/");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder newTreeBuilder2 = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeTreeBuilder2);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 boolean isTracking = parser.isTrackErrors();
 Assert.assertTrue(isTracking);
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 boolean isTracking = parser.isTrackErrors();
 Assert.assertFalse(isTracking);
 }

 @Test
 public void testParseStaticFunctions() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com/");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.title());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullFragmentHtml() {
 List<Node> nodes = Parser.parseFragment(null, null, "http://example.com/");
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullContext() {
 List<Node> nodes = Parser.parseFragment("<html></html>", null, "http://example.com/");
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Test</p>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com/");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com/");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Test</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com/");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.body().text());
 }
}