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
 private Document document;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, "http://example.com");

 Assert.assertNotNull(document);
 Assert.assertEquals("http://example.com", document.baseUri());
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertNotNull(treeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);

 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(newTreeBuilder, treeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 boolean isTrackingErrors = parser.isTrackErrors();
 Assert.assertTrue(isTrackingErrors);
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(2);
 int maxErrors = parser.getMaxErrors();
 Assert.assertEquals(2, maxErrors);
 }

 @Test
 public void testGetErrors() {
 String invalidHtml = "<html><body><script>alert('Hello')</script></body></html>";
 document = parser.parseInput(invalidHtml, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertNotNull(errors);
 Assert.assertEquals(1, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 parser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyInput() {
 parser.parseInput("", "http://example.com");
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div id='content'>Hello World</div>";
 document = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 Assert.assertNotNull(document);
 Assert.assertNotNull(document.body());
 Element content = document.body().getElementById("content");
 Assert.assertNotNull(content);
 Assert.assertEquals("Hello World", content.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentNullInput() {
 Parser.parseBodyFragment(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentEmptyInput() {
 Parser.parseBodyFragment("", "http://example.com");
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<ul><li>Apple</li><li>Banana</li></ul>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");

 Assert.assertNotNull(nodes);
 Assert.assertEquals(2, nodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 Parser.parseFragment(null, null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentEmptyInput() {
 Parser.parseFragment("", null, "http://example.com");
 }
}