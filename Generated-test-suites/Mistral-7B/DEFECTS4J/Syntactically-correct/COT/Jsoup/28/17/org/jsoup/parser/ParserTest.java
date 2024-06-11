import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private Parser htmlParser;
 private Parser xmlParser;

 @Before
 public void setUp() {
 htmlParser = Parser.htmlParser();
 xmlParser = Parser.xmlParser();
 }

 // Testing method: parseInput
 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = htmlParser.parseInput(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());

 String xml = "<root><element>Test</element></root>";
 doc = xmlParser.parseInput(xml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.select("element").text());
 }

 // Testing method: getTreeBuilder
 @Test
 public void testGetTreeBuilder() {
 Assert.assertSame(htmlParser.getTreeBuilder(), htmlParser.treeBuilder);
 }

 // Testing method: setTreeBuilder
 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 htmlParser.setTreeBuilder(newTreeBuilder);
 Assert.assertNotSame(htmlParser.getTreeBuilder(), htmlParser.treeBuilder);
 Assert.assertSame(htmlParser.treeBuilder, newTreeBuilder);
 }

 // Testing method: isTrackErrors
 @Test
 public void testIsTrackErrors() {
 Assert.assertFalse(htmlParser.isTrackErrors());
 }

 // Testing method: setTrackErrors
 @Test
 public void testSetTrackErrors() {
 htmlParser.setTrackErrors(5);
 Assert.assertTrue(htmlParser.isTrackErrors());
 }

 // Testing method: getErrors
 @Test
 public void testGetErrors() {
 String invalidHtml = "<html><body><script>"; // this HTML will cause a parse error
 Document doc = htmlParser.parseInput(invalidHtml, "http://example.com");
 List<ParseError> errors = htmlParser.getErrors();
 Assert.assertEquals(1, errors.size());
 Assert.assertNotNull(errors.get(0).getMessage());
 }

 // Testing method: static parse function
 @Test
 public void testStaticParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing method: parseFragment
 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Test</div>";
 Document doc = Parser.parseBodyFragment(fragmentHtml, "http://example.com");
 Element div = doc.select("div").first();
 Assert.assertNotNull(div);
 Assert.assertEquals("Test", div.text());
 }

 // Testing method: parseBodyFragmentRelaxed
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.select("h1").text());
 }

 // Testing edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 htmlParser.parseInput(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 htmlParser.parseInput("<html><body></body></html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxErrors() {
 htmlParser.setTrackErrors(-1);
 }

 @Test
 public void testParseErrorsWithMaxErrors() {
 String htmlWithErrors = "<html><body><script><script>";
 htmlParser.setTrackErrors(1);
 Document doc = htmlParser.parseInput(htmlWithErrors, "http://example.com");
 List<ParseError> errors = htmlParser.getErrors();
 Assert.assertEquals(2, errors.size());
 }
}