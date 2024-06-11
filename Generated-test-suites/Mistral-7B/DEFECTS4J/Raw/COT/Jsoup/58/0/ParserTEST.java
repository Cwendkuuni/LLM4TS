import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.util.ParseErrorList;

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
 String baseUri = "https://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertTrue(errors.isEmpty());
 Document document = parser.parseInput(html, baseUri);
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.head().title().text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><h1>Test</h1><script>alert('Hello World')</script></body></html>";
 String baseUri = "https://example.com";
 List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
 Assert.assertFalse(errors.isEmpty());
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unexpected <script> tag", errors.get(0).getMessage());
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
 parser.setTrackErrors(1);
 boolean isTrackErrors = parser.isTrackErrors();
 Assert.assertTrue(isTrackErrors);
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 boolean isTrackErrors = parser.isTrackErrors();
 Assert.assertFalse(isTrackErrors);
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
 Assert.assertSame(customSettings, parser.settings());
 }

 @Test
 public void testParseStatic() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Document document = Parser.parse(html);
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.head().title().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticWithNullHtml() {
 Parser.parse(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticWithNullBaseUri() {
 String html = "<html></html>";
 Parser.parse(html, null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Test</div>";
 Element context = Jsoup.parse("<html></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Element node = (Element) nodes.get(0);
 Assert.assertEquals("div", node.tagName());
 Assert.assertEquals("Test", node.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullFragmentHtml() {
 Parser.parseFragment(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullContext() {
 String fragmentHtml = "<div>Test</div>";
 Parser.parseFragment(fragmentHtml, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 String fragmentHtml = "<div>Test</div>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
 Assert.assertNotNull(nodes);
 Element node = (Element) nodes.get(0);
 node.remove(); // remove node from list to test for exception
 Parser.parseFragment(fragmentHtml, null, null);
 }

 @Test
 public void testParseXmlFragment() {
 String fragmentXml = "<xml><test>Test</test></xml>";
 List<Node> nodes = Parser.parseXmlFragment(fragmentXml);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Element node = (Element) nodes.get(0);
 Assert.assertEquals("test", node.tagName());
 Assert.assertEquals("Test", node.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentWithNullFragmentXml() {
 Parser.parseXmlFragment(null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test</h1></body>";
 Document document = Parser.parseBodyFragment(bodyHtml, null);
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.head().title().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNullBodyHtml() {
 Parser.parseBodyFragment(null, null);
 }

 @Test
 public void testUnescapeEntities() {
 String string = "&lt;test&gt;";
 String unescapedString = Parser.unescapeEntities(string, false);
 Assert.assertEquals("<test>", unescapedString);
 }

 @Test
 public void testUnescapeEntitiesInAttribute() {
 String attribute = "<script src=\"&amp;script.js\"></script>";
 String unescapedAttribute = Parser.unescapeEntities(attribute, true);
 Assert.assertEquals("<script src=\"script.js\"></script>", unescapedAttribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeEntitiesWithNullString() {
 Parser.unescapeEntities(null, false);
 }
}