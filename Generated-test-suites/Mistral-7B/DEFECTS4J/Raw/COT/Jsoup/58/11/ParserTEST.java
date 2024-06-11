import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Test
 public void testParserConstructor() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertNotNull(parser);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 @Test
 public void testGetTreeBuilder() {
 Assert.assertEquals(parser.getTreeBuilder(), parser.treeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(parser.getTreeBuilder(), newTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 ParseErrorList errors = new ParseErrorList();
 String invalidHtml = "<html><body><script>alert('Hello')</script></body></html>";
 parser.errors = errors;
 doc = parser.parseInput(invalidHtml, "http://example.com");
 List<ParseError> errorsList = parser.getErrors();
 Assert.assertNotNull(errorsList);
 Assert.assertEquals(1, errorsList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullParseErrorList() {
 parser.parseInput("", "", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithEmptyHtml() {
 parser.parseInput("", "http://example.com");
 }

 @Test
 public void testParseInputWithEmptyBaseUri() {
 parser.parseInput("", "");
 }

 @Test
 public void testStaticParseFunctionWithNullHtml() {
 Parser.parse(null, "");
 }

 @Test
 public void testParseFunctionWithEmptyHtml() {
 String emptyHtml = "";
 doc = Parser.parse(emptyHtml, "");
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.childNodeSize());
 }

 @Test
 public void testParseFragmentWithNullFragmentHtml() {
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(null, context, "");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithEmptyFragmentHtml() {
 String emptyFragmentHtml = "";
 List<Node> nodes = Parser.parseFragment(emptyFragmentHtml, null, "");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseXmlFragmentWithNullFragmentXml() {
 List<Node> nodes = Parser.parseXmlFragment(null, "");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testParseBodyFragmentWithNullBodyHtml() {
 String bodyHtml = "";
 doc = Parser.parseBodyFragment(bodyHtml, "");
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.childNodeSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNullBaseUri() {
 String bodyHtml = "<html><body></body></html>";
 doc = Parser.parseBodyFragment(bodyHtml, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithEmptyBodyHtml() {
 doc = Parser.parseBodyFragment("", "");
 }

 @Test
 public void testStaticParseBodyFragmentWithNullBodyHtml() {
 doc = Parser.parseBodyFragmentRelaxed(null, "");
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.childNodeSize());
 }

 @Test
 public void testUnescapeEntitiesWithNullString() {
 String string = null;
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertNull(unescapedString);
 }

 @Test
 public void testUnescapeEntitiesWithEmptyString() {
 String unescapedString = Parser.unescapeEntities("", false);
 Assert.assertEquals("", unescapedString);
 }

 @Test
 public void testUnescapeEntitiesWithInvalidEntities() {
 String string = "<div>&#x1F603;&#x1F604;</div>";
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<div>&#x1F603;&#x1F604;</div>", unescapedString);
 }

 @Test
 public void testUnescapeEntitiesWithEntitiesInAttribute() {
 String string = "<div data-foo=\"<strong>Hello</strong>\"></div>";
 String unescapedString = Parser.unescapeEntities(string, true);
 Assert.assertEquals("<div data-foo=\"<strong>Hello</strong>\"></div>", unescapedString);
 }

 @Test
 public void testUnescapeEntitiesWithEntitiesInString() {
 String string = "<div>&lt;strong&gt;Hello&lt;/strong&gt;</div>";
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<div><strong>Hello</strong></div>", unescapedString);
 }
}