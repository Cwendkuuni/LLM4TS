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

 @Test
 public void testParseInput() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "http://test.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><body><script>alert('XSS')</script></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "http://test.com");
 List<ParseError> errors = parser.getErrors();

 Assert.assertNotNull(doc);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Malformed script tag", errors.get(0).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testGetTreeBuilder() {
 Assert.assertSame(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertNotSame(treeBuilder, parser.getTreeBuilder());
 Assert.assertSame(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithPositiveValue() {
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><script>alert('XSS')</script></body></html>";
 parser.parseInput(html, "http://test.com");
 List<ParseError> errors = parser.getErrors();

 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Malformed script tag", errors.get(0).toString());
 }

 @Test
 public void testParse() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser.parse(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Test</h1>";
 Element context = Jsoup.parse("<div></div>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://test.com");

 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Assert.assertInstanceOf(Element.class, nodes.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullFragmentHtml() {
 Parser.parseFragment(null, context, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullContext() {
 Parser.parseFragment("<h1>Test</h1>", null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 Parser.parseFragment("<h1>Test</h1>", context, null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Test</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.select("h1").text());
 Assert.assertEquals(0, doc.select("head > *").size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://test.com");
 }

 @Test
 public void testParseBodyFragmentWithNullBaseUri() {
 Parser.parseBodyFragment("<h1>Test</h1>", null);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<html><body><h1>Test</h1></body></html>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.select("h1").text());
 }

 @Test(expected = DeprecationWarning.class)
 public void testParseBodyFragmentRelaxedWithNullBodyHtml() {
 Parser.parseBodyFragmentRelaxed(null, "http://test.com");
 }

 @Test(expected = DeprecationWarning.class)
 public void testParseBodyFragmentRelaxedWithNullBaseUri() {
 Parser.parseBodyFragmentRelaxed("<html><body><h1>Test</h1></body></html>", null);
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 Assert.assertInstanceOf(HtmlTreeBuilder.class, parser.getTreeBuilder());
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 Assert.assertInstanceOf(XmlTreeBuilder.class, parser.getTreeBuilder());
 }
}