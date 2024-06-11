import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private Parser parser;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://www.example.com";
 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertNotNull(treeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder currentTreeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(newTreeBuilder, currentTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputEmptyHtml() {
 parser.parseInput("", "http://www.example.com");
 }

 @Test
 public void testParseFragmentValid() {
 String fragmentHtml = "<p>This is a test</p>";
 Element context = Jsoup.parse("").body();
 String baseUri = "http://www.example.com";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.assertFalse(nodes.isEmpty());
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, null, "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment("<p>This is a test</p>", null, "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment("<p>This is a test</p>", null, null);
 }

 @Test
 public void testParseXmlFragmentValid() {
 String fragmentXml = "<xml><tag>Test</tag></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://www.example.com");
 Assert.assertFalse(nodes.isEmpty());
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentNullFragmentXml() {
 Parser.parseXmlFragment(null, "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentValid() {
 String bodyHtml = "<p>This is a test</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
 Assert.assertNotNull(doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 Parser.parseBodyFragment("<p>This is a test</p>", null);
 }

 @Test
 public void testUnescapeEntitiesValid() {
 String string = "&amp;lt;p&amp;gt;This is a test&amp;lt;/p&amp;gt;";
 String result = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<p>This is a test</p>", result);
 }

 @Test
 public void testUnescapeEntitiesInAttributeValid() {
 String string = "<input type=\"text\" value=\"&amp;quot;This is a test&amp;quot;\" />";
 String result = Parser.unescapeEntities(string, true);
 Assert.assertEquals("<input type=\"text\" value=\"\\\"This is a test\\\"\" />", result);
 }
}