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
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, null);
 Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\">";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 String imgSrc = doc.getElementsByTag("img").attr("src");
 Assert.assertEquals("image.jpg", imgSrc);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 Parser parserWithNewTreeBuilder = parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parserWithNewTreeBuilder.getTreeBuilder();
 Assert.assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 Parser parserWithTrackErrors = parser.setTrackErrors(1);
 Assert.assertTrue(parserWithTrackErrors.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Assert.assertEquals(Parser.DEFAULT_MAX_ERRORS, parser.getMaxErrors());
 Parser parserWithMaxErrors = parser.setTrackErrors(5);
 Assert.assertEquals(5, parserWithMaxErrors.getMaxErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> expectedErrors = ParseErrorList.noTracking();
 List<ParseError> actualErrors = parser.getErrors();
 Assert.assertEquals(expectedErrors, actualErrors);
 }

 @Test
 public void testParseWithErrors() {
 String htmlWithError = "<html><body><h1>Hello <b>World</b></h1></body></html>"; // missing end tag for b tag
 TreeBuilder treeBuilderWithErrors = new HtmlTreeBuilder();
 Parser parserWithErrors = new Parser(treeBuilderWithErrors);
 List<ParseError> errors = parserWithErrors.getErrors();
 Assert.assertEquals(1, errors.size());
 Document docWithError = parserWithErrors.parseInput(htmlWithError, null);
 List<Element> h1Elements = docWithError.getElementsByTag("h1");
 Assert.assertEquals(1, h1Elements.size());
 Assert.assertEquals("Hello ", h1Elements.get(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 parser.parseInput(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
 Element context = Jsoup.parse("<div></div>").body();
 Document doc = parser.parseInput(fragmentHtml, context.toString());
 List<Node> nodes = doc.getElements();
 Element list = (Element) nodes.get(0);
 Assert.assertEquals("ul", list.tagName());
 List<Element> liElements = list.getElementsByTag("li");
 Assert.assertEquals(2, liElements.size());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><element1>Element 1</element1><element2>Element 2</element2></xml>";
 Document doc = Parser.parseXmlFragment(fragmentXml, null);
 List<Node> nodes = doc.getElements();
 Element element1 = (Element) nodes.get(0);
 Assert.assertEquals("element1", element1.tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, null);
 List<Element> pElements = doc.getElementsByTag("p");
 Assert.assertEquals(2, pElements.size());
 }

 @Test
 public void testUnescapeEntities() {
 String htmlEscaped = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String htmlUnescaped = Parser.unescapeEntities(htmlEscaped, false);
 Document doc = Jsoup.parse(htmlUnescaped);
 List<Element> pElements = doc.getElementsByTag("p");
 Assert.assertEquals(1, pElements.size());
 Assert.assertEquals("&lt;p&gt;Hello &amp; World&lt;/p&gt;", pElements.get(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
 List<Element> pElements = doc.getElementsByTag("p");
 Assert.assertEquals(2, pElements.size());
 }

 @Test
 public void testHtmlParser() {
 Parser parser = Parser.htmlParser();
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
 }

 @Test
 public void testXmlParser() {
 Parser parser = Parser.xmlParser();
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
 }
}