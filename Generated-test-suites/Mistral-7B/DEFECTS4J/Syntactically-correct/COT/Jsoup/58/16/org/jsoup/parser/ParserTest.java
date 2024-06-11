import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
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
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testParseInputWithTrackErrorsEnabled() {
 parser.setTrackErrors(1);
 String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testParseInputWithTrackErrorsDisabled() {
 parser.setTrackErrors(0);
 String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
 String baseUri = "http://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertNull(errors);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 Assert.assertSame(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 Assert.assertSame(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(2);
 Assert.assertEquals(2, parser.getMaxErrors());
 parser.setTrackErrors(0);
 Assert.assertEquals(0, parser.getMaxErrors());
 }

 @Test
 public void testGetErrors() {
 List<ParseError> errors = parser.getErrors();
 Assert.assertNull(errors);
 }

 @Test
 public void testSettings() {
 ParseSettings settings = parser.settings();
 Assert.assertNotNull(settings);
 }

 @Test
 public void testSettingsWithCustomSettings() {
 ParseSettings customSettings = new ParseSettings();
 parser.settings(customSettings);
 ParseSettings actualSettings = parser.settings();
 Assert.assertSame(customSettings, actualSettings);
 }

 @Test
 public void testParse() {
 Document parsedDocument = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(parsedDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyInput() {
 Parser.parse("", "http://example.com");
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div class=\"content\"><p>Paragraph 1.</p><p>Paragraph 2.</p></div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(3, nodes.size());
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><data><value>Hello</value></data></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://example.com");
 Assert.assertNotNull(nodes);
 Assert.assertEquals(2, nodes.size());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div class=\"content\"><p>Paragraph 1.</p><p>Paragraph 2.</p></div>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Element body = doc.body();
 List<Node> nodes = body.children();
 Assert.assertNotNull(nodes);
 Assert.assertEquals(3, nodes.size());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String output = Parser.unescapeEntities(input, false);
 Assert.assertEquals("<p>Hello & World</p>", output);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String input = "<div class=\"class&name&value\"></div>";
 String output = Parser.unescapeEntities(input, true);
 Assert.assertEquals("<div class=\"class&name=value\"></div>", output);
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragmentRelaxed("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertNotNull(doc);
 }
}