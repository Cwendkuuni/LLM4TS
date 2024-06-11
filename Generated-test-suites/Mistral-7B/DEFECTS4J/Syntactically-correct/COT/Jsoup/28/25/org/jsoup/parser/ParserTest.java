import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Element;
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
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document expected = Jsoup.parse(html);
 Document actual = parser.parseInput(html, null);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" />";
 String baseUri = "http://example.com/";
 Document expected = Jsoup.parse(html, baseUri);
 Document actual = parser.parseInput(html, baseUri);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expected = treeBuilder;
 TreeBuilder actual = parser.getTreeBuilder();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actual = parser.getTreeBuilder();
 Assert.assertEquals(newTreeBuilder, actual);
 }

 @Test
 public void testIsTrackErrors() {
 // default test for false
 Parser parser1 = new Parser(treeBuilder);
 Assert.assertFalse(parser1.isTrackErrors());
 // test for true
 Parser parser2 = new Parser(treeBuilder);
 parser2.setTrackErrors(1);
 Assert.assertTrue(parser2.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Parser parser = new Parser(treeBuilder);
 int maxErrors = 2;
 parser.setTrackErrors(maxErrors);
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(maxErrors, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullParser() {
 Parser nullParser = null;
 nullParser.parseInput("", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithEmptyHtml() {
 parser.parseInput("", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithFragments() {
 parser.parseInput("<script src=\"script.js\"></script>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithScriptsInsideScripts() {
 parser.parseInput("<script src=\"script1.js\"><script src=\"script2.js\"></script>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithInvalidHtml() {
 parser.parseInput("</html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentRelaxedWithNullBodyHtml() {
 Parser.parseBodyFragmentRelaxed(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentRelaxedWithNullBaseUri() {
 Parser.parseBodyFragmentRelaxed("", null);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Fragment</p>";
 Element context = new Element("div");
 Document expected = Jsoup.parse(fragmentHtml, null);
 List<Node> actualNodes = Parser.parseFragment(fragmentHtml, context, null);
 Document actual = new Document(context);
 for (Node node : actualNodes) {
 actual.appendChild(node);
 }
 Assert.assertEquals(expected.rootElement(), actual.rootElement());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Body Fragment</p>";
 Document expected = Jsoup.parseBodyFragment(bodyHtml, null);
 Document actual = Parser.parseBodyFragment(bodyHtml, null);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testParseBodyFragmentWithNullBodyHtml() {
 Parser.parseBodyFragment(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithEmptyBodyHtml() {
 Parser.parseBodyFragment("", null);
 }

 @Test(expected = NullPointerException.class)public void testParseBodyFragmentWithNullBaseUri() {
 Parser.parseBodyFragment("", null);
 }
}