import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 // Testing parseInput method
 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Test</h1></body></html>";
 String baseUri = "http://example.com";
 document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputNullDocument() {
 parser.setTreeBuilder(null);
 parser.parseInput("<html></html>", "http://example.com");
 }

 @Test(expected = ParseError.class)
 public void testParseInputWithError() {
 parser.setTrackErrors(1);
 String htmlWithError = "<html><body><h1>Test<script>alert('Hello')</script></h1></body></html>";
 document = parser.parseInput(htmlWithError, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Expected ';', but was 'script'", errors.get(0).getMessage());
 }

 // Testing getTreeBuilder method
 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertNotNull(treeBuilder);
 }

 // Testing setTreeBuilder method
 @Test
 public void testSetTreeBuilder() {
 parser.setTreeBuilder(new XmlTreeBuilder());
 TreeBuilder newTreeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(XmlTreeBuilder.class, newTreeBuilder.getClass());
 }

 // Testing isTrackErrors method
 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 // Testing getErrors method
 @Test
 public void testGetErrors() {
 Assert.assertNull(parser.getErrors());
 }

 // Testing parse method
 @Test
 public void testParse() {
 document = Parser.parse("<html><body><h1>Test</h1></body></html>", "http://example.com");
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title());
 }

 // Testing parseFragment method
 @Test
 public void testParseFragment() {
 String fragmentHtml = "<h1>Test</h1>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Node node = nodes.get(0);
 Assert.assertEquals(Element.class, node.getClass());
 Assert.assertEquals("Test", node.text());
 }

 @Test
 public void testParseFragmentWithError() {
 String fragmentHtml = "<h1>Test<script>alert('Hello')</script></h1>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Expected ';', but was 'script'", errors.get(0).getMessage());
 }

 // Testing parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Test</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertNull(doc.head());
 Assert.assertEquals("Test", doc.body().text());
 }

 // Testing parseBodyFragmentRelaxed method (deprecated)
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<h1>Test</h1>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertNull(doc.head());
 Assert.assertEquals("Test", doc.body().text());
 }

 // Testing htmlParser method
 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
 }

 // Testing xmlParser method
 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
 }
}