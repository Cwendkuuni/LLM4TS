import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
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
 String html = "<html><head></head><body></body></html>";
 String baseUri = "https://example.com";
 List<ParseError> errors = new ArrayList<>();

 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><body><script>alert('XSS')</script></body></html>";
 String baseUri = "https://example.com";
 List<ParseError> errors = new ArrayList<>();

 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertFalse(errors.isEmpty());
 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals("Error parsing starting tag : script", error.getMessage());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<html><body><script>alert('XSS')</script></body><script>alert('XSS')</script></html>";
 String baseUri = "https://example.com";
 List<ParseError> errors = new ArrayList<>();
 parser.setTrackErrors(2);

 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 boolean isTrackingErrors = parser.isTrackErrors();
 Assert.assertTrue(isTrackingErrors);
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 boolean isTrackingErrors = parser.isTrackErrors();
 Assert.assertFalse(isTrackingErrors);
 }

 @Test
 public void testGetErrors() {
 String html = "<html><body><script>alert('XSS')</script></body></html>";
 String baseUri = "https://example.com";
 List<ParseError> errors = new ArrayList<>();
 parser.setTrackErrors(1);

 Document doc = parser.parseInput(html, baseUri);
 Assert.assertNotNull(doc);
 Assert.assertFalse(errors.isEmpty());
 Assert.assertEquals(1, errors.size());
 List<ParseError> parseErrors = parser.getErrors();
 Assert.assertEquals(parseErrors, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 String baseUri = "https://example.com";
 parser.parseInput(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 String html = "<html><head></head><body></body></html>";
 parser.parseInput(html, null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullParser() {
 String html = "<html><head></head><body></body></html>";
 String baseUri = "https://example.com";
 Parser parser = null;
 List<ParseError> errors = new ArrayList<>();

 Document doc = parser.parseInput(html, baseUri);
 }

 @Test
 public void testParseStaticFunctionParse() {
 String html = "<html><head></head><body></body></html>";
 String baseUri = "https://example.com";
 Document doc = Parser.parse(html, baseUri);
 Assert.assertNotNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testParseStaticFunctionParseNullHtml() {
 String baseUri = "https://example.com";
 Parser.parse(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticFunctionParseEmptyHtml() {
 String baseUri = "https://example.com";
 Parser.parse("", baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStaticFunctionParseNullBaseUri() {
 String html = "<html><head></head><body></body></html>";
 Parser.parse(html, null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "https://example.com";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragmentHtml() {
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "https://example.com";
 Parser.parseFragment(null, context, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentEmptyFragmentHtml() {
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "https://example.com";
 Parser.parseFragment("", context, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 String fragmentHtml = "<div>Hello World</div>";
 String baseUri = "https://example.com";
 Parser.parseFragment(fragmentHtml, null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "https://example.com";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 Assert.assertNotNull(doc);
 }

 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentNullBodyHtml() {
 String baseUri = "https://example.com";
 Parser.parseBodyFragment(null, baseUri);
 }

 @Test
 public void testParseBodyFragmentRelaxedDeprecated() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "https://example.com";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 Assert.assertNotNull(doc);
 }

 @Test(expected = DeprecationException.class)
 public void testParseBodyFragmentRelaxedDeprecatedWithContext() {
 String bodyHtml = "<div>Hello World</div>";
 Element context = Jsoup.parse("<html><body></body></html>").body();
 String baseUri = "https://example.com";
 Parser.parseBodyFragmentRelaxed(bodyHtml, context, baseUri);
 }

 @Test(expected = DeprecationException.class)
 public void testParseBodyFragmentRelaxedDeprecatedWithoutContext() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "https://example.com";
 Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
 }
}