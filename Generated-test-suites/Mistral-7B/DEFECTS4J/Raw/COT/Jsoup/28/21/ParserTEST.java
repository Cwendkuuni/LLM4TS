import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 // Test Case 1: Test parseInput() method with valid HTML and baseUri
 @Test
 public void testParseInputValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><p>Hello World!</p></body></html>";
 doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test Page", doc.title().text());
 Assert.assertEquals("Hello World!", doc.body().text());
 }

 // Test Case 2: Test parseInput() method with invalid HTML and no error tracking
 @Test(expected = org.jsoup.ParseError.class)
 public void testParseInputInvalidHtmlNoTrackErrors() {
 String html = "<html><body><script>alert('Hello')</script></body></html>";
 parser.setTrackErrors(0);
 doc = parser.parseInput(html, "http://example.com");
 }

 // Test Case 3: Test parseInput() method with invalid HTML and error tracking
 @Test
 public void testParseInputInvalidHtmlTrackErrors() {
 String html = "<html><body><script>alert('Hello')</script></body></html>";
 List<ParseError> errors = parser.getErrors();
 doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }

 // Test Case 4: Test static parse() method with valid HTML
 @Test
 public void testStaticParseValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><p>Hello World!</p></body></html>";
 doc = Parser.parse(html);
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test Page", doc.title().text());
 Assert.assertEquals("Hello World!", doc.body().text());
 }

 // Test Case 5: Test static parseFragment() method with valid HTML
 @Test
 public void testStaticParseFragmentValidHtml() {
 String fragmentHtml = "<p>Hello World!</p>";
 List<Node> nodes = Parser.parseFragment(fragmentHtml);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 // Test Case 6: Test static parseBodyFragment() method with valid HTML
 @Test
 public void testStaticParseBodyFragmentValidHtml() {
 String bodyHtml = "<p>Hello World!</p>";
 doc = Parser.parseBodyFragment(bodyHtml);
 Assert.assertNotNull(doc);
 Assert.assertEquals("", doc.head().toString());
 Assert.assertEquals("Hello World!", doc.body().text());
 }

 // Test Case 7: Test parseInput() method with null input
 @Test(expected = NullPointerException.class)
 public void testParseInputNullInput() {
 parser.parseInput(null, "http://example.com");
 }

 // Test Case 8: Test parseInput() method with empty input
 @Test
 public void testParseInputEmptyInput() {
 String html = "";
 doc = parser.parseInput(html, "http://example.com");
 Assert.assertNull(doc);
 }

 // Test Case 9: Test parseInput() method with very large input
 @Test
 public void testParseInputLargeInput() {
 String html = "<html lang=\"en\">\n" +
 " <head>\n" +
 " <meta charset=\"UTF-8\">\n" +
 " <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
 " <title>Document Title</title>\n" +
 " <link rel=\"stylesheet\" href=\"styles.css\">\n" +
 " </head>\n" +
 " <body>\n" +
 " <p id=\"p1\">Hello World!</p>\n" +
 " </body>\n" +
 " </html>\n";
 doc = parser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 }

 // Test Case 10: Test getTreeBuilder() method
 @Test
 public void testGetTreeBuilder() {
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertNotNull(treeBuilder);
 }

 // Test Case 11: Test setTreeBuilder() method
 @Test
 public void testSetTreeBuilder() {
 TreeBuilder treeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(treeBuilder);
 Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
 }

 // Test Case 12: Test isTrackErrors() method
 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 Assert.assertTrue(parser.isTrackErrors());
 }

 // Test Case 13: Test setTrackErrors() method
 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 Assert.assertEquals(5, parser.getMaxErrors());
 }

 // Test Case 14: Test getErrors() method with no errors
 @Test
 public void testGetErrorsNoErrors() {
 Assert.assertEquals(0, parser.getErrors().size());
 }

 // Test Case 15: Test getErrors() method with errors
 @Test
 public void testGetErrorsWithErrors() {
 String html = "<html><body><script>alert('Hello')</script></body></html>";
 parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
 }
}