import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
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
 String baseUri = "http://example.com";
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html><body><h1>Test</h1></body></html>", null);
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
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String htmlWithError = "<html><body><h1>Test</h1></body></html><script>throw new Error('Test Error')</script>";
 parser.parseInput(htmlWithError, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Test Error", errors.get(0).getMessage());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div><p>Test</p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 Document document = Parser.parseFragment(fragmentHtml, context, "http://example.com").get(0).document();
 Element div = document.getElementById("0");
 Assert.assertEquals("<div><p>Test</p></div>", div.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment("<div><p>Test</p></div>", null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment("<div><p>Test</p></div>", null, null);
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><tag>Test</tag></xml>";
 Document document = Parser.parseXmlFragment(fragmentXml, "http://example.com");
 Element tag = document.getElementsByTagName("tag").get(0);
 Assert.assertEquals("<tag>Test</tag>", tag.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentNullFragmentXml() {
 Parser.parseXmlFragment(null, "http://example.com");
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Test</p></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Element p = document.getElementsByTagName("p").get(0);
 Assert.assertEquals("<p>Test</p>", p.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test
 public void testUnescapeEntities() {
 String escapedString = "&lt;<script&gt;alert('Test')&lt;/script&gt;";
 String unescapedString = Parser.unescapeEntities(escapedString, false);
 Assert.assertEquals("<script>alert('Test')</script>", unescapedString);
 }

 @Test
 public void testParseInputWithErrors() {
 String htmlWithErrors = "<html><body><h1>Test</h1><script>throw new Error('Test Error 1')</script><script>throw new Error('Test Error 2')</script></body></html>";
 parser.parseInput(htmlWithErrors, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String htmlWithErrors = "<html><body><h1>Test</h1>";
 for (int i = 0; i < Parser.DEFAULT_MAX_ERRORS + 1; i++) {
 String script = "<script>throw new Error('Test Error " + i + "')</script>";
 htmlWithErrors += script;
 }
 parser.parseInput(htmlWithErrors, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(Parser.DEFAULT_MAX_ERRORS, errors.size());
 }

}