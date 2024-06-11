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
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();
 Document doc = parser.parseInput(html, baseUri);
 List<ParseError> actualErrors = parser.getErrors();
 Assert.assertEquals(errors, actualErrors);
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullHtml() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputNullBaseUri() {
 parser.parseInput("<html></html>", null);
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = new ArrayList<>();
 errors.add(new ParseError("", 1, 1, "Error message", "Scanner"));
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, baseUri);
 List<ParseError> actualErrors = parser.getErrors();
 Assert.assertEquals(errors.size() + 1, actualErrors.size());
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseInputWithNoTrackingErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(0);
 Document doc = parser.parseInput(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(0, errors.size());
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsNegativeNumber() {
 parser.setTrackErrors(-1);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseWithTrackingErrors() {
 String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
 List<ParseError> errors = new ArrayList<>();
 errors.add(new ParseError("", 1, 1, "Error message", "Scanner"));
 Parser.setTrackErrors(1);
 Document doc = Parser.parse(html, "http://example.com");
 List<ParseError> actualErrors = Parser.getErrors();
 Assert.assertEquals(errors.size() + 1, actualErrors.size());
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseWithNoTrackingErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 List<ParseError> errors = Parser.getErrors();
 Assert.assertEquals(0, errors.size());
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("div", nodes.get(0).tagName());
 Assert.assertEquals("Hello World", nodes.get(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Parser.parseFragment(null, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment("<div>Hello World</div>", null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 Parser.parseFragment("<div>Hello World</div>", null, null);
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><data>Hello World</data></xml>";
 String baseUri = "http://example.com";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("xml", nodes.get(0).tagName());
 Assert.assertEquals("data", nodes.get(0).tagName(1).tagName());
 Assert.assertEquals("Hello World", nodes.get(0).tagName(1).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentNullFragmentXml() {
 Parser.parseXmlFragment(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentNullBaseUri() {
 Parser.parseXmlFragment("<xml></xml>", null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 String baseUri = "http://example.com";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals("<html><body><p>Hello World</p></body></html>", doc.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullBaseUri() {
 Parser.parseBodyFragment("<body></body>", null);
 }

 @Test
 public void testUnescapeEntities() {
 String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String unescaped = Parser.unescapeEntities(html, false);
 Assert.assertEquals("<p>Hello & World</p>", unescaped);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String html = "<div data-test=\"&lt;p&gt;Hello &amp; World&lt;/p&gt;\"></div>";
 String unescaped = Parser.unescapeEntities(html, true);
 Assert.assertEquals("<div data-test=\"<p>Hello & World</p>\"></div>", unescaped);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeEntitiesNullString() {
 Parser.unescapeEntities(null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeEntitiesNullInAttribute() {
 Parser.unescapeEntities("<div></div>", true);
 }
}