import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document document;

 @Test
 public void testParserWithDefaultConstructor() {
 Parser parser = new Parser(null);
 // Test empty constructor
 Assert.assertNotNull(parser);
 }

 @Test
 public void testParserWithTreeBuilderConstructor() {
 TreeBuilder treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 // Test constructor with TreeBuilder
 Assert.assertNotNull(parser);
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testParseInputWithHtml() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 parser = new Parser(new HtmlTreeBuilder());
 document = parser.parseInput(html, "http://www.example.com");
 // Test parseInput with valid HTML
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title().text());
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput(null, "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithEmptyHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("", "http://www.example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithInvalidBaseUri() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", null);
 }

 @Test(expected = ParseError.class)
 public void testParseInputWithInvalidHtml() {
 parser = new Parser(new HtmlTreeBuilder());
 document = parser.parseInput("<html><head><title>Test</title></head><body><p>Hello <script>World</script></p></body></html>", "http://www.example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).toString());
 }

 @Test
 public void testIsTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 Assert.assertEquals(1, parser.getMaxErrors());
 parser.setTrackErrors(0);
 Assert.assertEquals(0, parser.getMaxErrors());
 }

 @Test
 public void testGetErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("<html><head><title>Test</title></head><body><p>Hello <script>World</script></p></body></html>", "http://www.example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).toString());
 }

 @Test
 public void testParse() {
 Document document = Parser.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://www.example.com");
 // Test static parse function
 Assert.assertNotNull(document);
 Assert.assertEquals("Test", document.title().text());
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Hello World</p>";
 Element context = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://www.example.com");
 // Test parseFragment function
 Node node = nodes.get(0);
 Assert.assertEquals(Element.class, node.getClass());
 Assert.assertEquals("Hello World", node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<html><body><p>Hello World</p></body></html>";
 Document document = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
 // Test parseBodyFragment function
 Assert.assertNotNull(document);
 Assert.assertNotNull(document.body());
 Assert.assertEquals("Hello World", document.body().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<html><body><p>Hello World</p></body></html>";
 document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://www.example.com");
 // Test parseBodyFragmentRelaxed function (deprecated method)
 Assert.assertNotNull(document);
 Assert.assertNotNull(document.body());
 Assert.assertEquals("Hello World", document.body().text());
 }
}